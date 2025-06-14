package com.veterinary.appointmentservice.service;

import com.veterinary.appointmentservice.client.PatientServiceClient;
import com.veterinary.appointmentservice.client.UserServiceClient;
import com.veterinary.appointmentservice.dto.*;
import com.veterinary.appointmentservice.entity.Appointment;
import com.veterinary.appointmentservice.exception.AppointmentException;
import com.veterinary.appointmentservice.repository.AppointmentRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientServiceClient patientServiceClient;

    @Autowired
    private UserServiceClient userServiceClient;

    public AppointmentResponse createAppointment(CreateAppointmentRequest request, String currentUser, String authHeader) {
        // Validar que el paciente existe
        try {
            var patientResponse = patientServiceClient.getPatientById(request.getPatientId(), authHeader);
            if (patientResponse.getBody() == null) {
                throw new AppointmentException("Patient not found with id: " + request.getPatientId());
            }
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new AppointmentException("Patient not found with id: " + request.getPatientId());
            }
            throw new AppointmentException("Error validating patient: " + e.getMessage());
        }

        // Validar que el veterinario existe y tiene el rol correcto
        try {
            var veterinarianResponse = userServiceClient.getUserById(request.getVeterinarianId(), authHeader);
            if (veterinarianResponse.getBody() == null) {
                throw new AppointmentException("Veterinarian not found with id: " + request.getVeterinarianId());
            }
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new AppointmentException("Veterinarian not found with id: " + request.getVeterinarianId());
            }
            throw new AppointmentException("Error validating veterinarian: " + e.getMessage());
        }

        // Validar disponibilidad del veterinario
        LocalTime endTime = request.getAppointmentTime().plusMinutes(request.getDurationMinutes());
        List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(
                request.getVeterinarianId(),
                request.getAppointmentDate(),
                request.getAppointmentTime(),
                endTime
        );

        if (!conflicts.isEmpty()) {
            throw new AppointmentException("Veterinarian is not available at the requested time");
        }

        // Validar que la fecha no sea en el pasado
        if (request.getAppointmentDate().isBefore(LocalDate.now()) ||
                (request.getAppointmentDate().equals(LocalDate.now()) && request.getAppointmentTime().isBefore(LocalTime.now()))) {
            throw new AppointmentException("Appointment date and time must be in the future");
        }

        // Crear nueva cita
        Appointment appointment = new Appointment();
        appointment.setPatientId(request.getPatientId());
        appointment.setVeterinarianId(request.getVeterinarianId());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setNotes(request.getNotes());
        appointment.setDurationMinutes(request.getDurationMinutes());
        appointment.setStatus(Appointment.Status.SCHEDULED);
        appointment.setCreatedBy(currentUser);
        appointment.setUpdatedBy(currentUser);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        AppointmentDTO appointmentDTO = new AppointmentDTO(savedAppointment);

        // Enriquecer con información del paciente y veterinario
        enrichWithExternalInfo(appointmentDTO, authHeader);

        return new AppointmentResponse("Appointment created successfully", appointmentDTO);
    }

    public AppointmentResponse getAppointmentById(Long id, String authHeader) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentException("Appointment not found with id: " + id));

        AppointmentDTO appointmentDTO = new AppointmentDTO(appointment);
        enrichWithExternalInfo(appointmentDTO, authHeader);

        return new AppointmentResponse("Appointment found", appointmentDTO);
    }

    public AppointmentResponse updateAppointment(Long id, UpdateAppointmentRequest request, String currentUser, String authHeader) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentException("Appointment not found with id: " + id));

        // No permitir modificar citas completadas o canceladas
        if (appointment.getStatus() == Appointment.Status.COMPLETED ||
                appointment.getStatus() == Appointment.Status.CANCELLED) {
            throw new AppointmentException("Cannot modify completed or cancelled appointments");
        }

        // Validar veterinario si se está cambiando
        if (request.getVeterinarianId() != null && !request.getVeterinarianId().equals(appointment.getVeterinarianId())) {
            try {
                userServiceClient.getUserById(request.getVeterinarianId(), authHeader);
            } catch (FeignException e) {
                if (e.status() == 404) {
                    throw new AppointmentException("Veterinarian not found with id: " + request.getVeterinarianId());
                }
            }
        }

        // Validar disponibilidad si se cambia fecha, hora o veterinario
        if (request.getAppointmentDate() != null || request.getAppointmentTime() != null ||
                request.getVeterinarianId() != null || request.getDurationMinutes() != null) {

            Long veterinarianId = request.getVeterinarianId() != null ? request.getVeterinarianId() : appointment.getVeterinarianId();
            LocalDate appointmentDate = request.getAppointmentDate() != null ? request.getAppointmentDate() : appointment.getAppointmentDate();
            LocalTime appointmentTime = request.getAppointmentTime() != null ? request.getAppointmentTime() : appointment.getAppointmentTime();
            Integer duration = request.getDurationMinutes() != null ? request.getDurationMinutes() : appointment.getDurationMinutes();

            LocalTime endTime = appointmentTime.plusMinutes(duration);
            List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(
                    veterinarianId, appointmentDate, appointmentTime, endTime
            );

            // Excluir la cita actual de los conflictos
            conflicts = conflicts.stream()
                    .filter(a -> !a.getId().equals(appointment.getId()))
                    .collect(Collectors.toList());

            if (!conflicts.isEmpty()) {
                throw new AppointmentException("Veterinarian is not available at the requested time");
            }
        }

        // Actualizar campos
        if (request.getVeterinarianId() != null) {
            appointment.setVeterinarianId(request.getVeterinarianId());
        }
        if (request.getAppointmentDate() != null) {
            appointment.setAppointmentDate(request.getAppointmentDate());
        }
        if (request.getAppointmentTime() != null) {
            appointment.setAppointmentTime(request.getAppointmentTime());
        }
        if (request.getReason() != null) {
            appointment.setReason(request.getReason());
        }
        if (request.getStatus() != null) {
            appointment.setStatus(request.getStatus());
        }
        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }
        if (request.getDurationMinutes() != null) {
            appointment.setDurationMinutes(request.getDurationMinutes());
        }

        appointment.setUpdatedBy(currentUser);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        AppointmentDTO appointmentDTO = new AppointmentDTO(updatedAppointment);

        enrichWithExternalInfo(appointmentDTO, authHeader);

        return new AppointmentResponse("Appointment updated successfully", appointmentDTO);
    }

    public AppointmentResponse cancelAppointment(Long id, String currentUser, String authHeader) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentException("Appointment not found with id: " + id));

        if (appointment.getStatus() == Appointment.Status.COMPLETED) {
            throw new AppointmentException("Cannot cancel completed appointment");
        }

        appointment.setStatus(Appointment.Status.CANCELLED);
        appointment.setUpdatedBy(currentUser);
        appointmentRepository.save(appointment);

        return new AppointmentResponse("Appointment cancelled successfully");
    }

    public AppointmentResponse getAllAppointments(int page, int size, String sortBy, String sortDir, String authHeader) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Appointment> appointmentPage = appointmentRepository.findAll(pageable);

        List<AppointmentDTO> appointments = appointmentPage.getContent().stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        // Enriquecer con información externa
        appointments.forEach(dto -> enrichWithExternalInfo(dto, authHeader));

        Map<String, Object> response = new HashMap<>();
        response.put("appointments", appointments);
        response.put("totalElements", appointmentPage.getTotalElements());
        response.put("totalPages", appointmentPage.getTotalPages());
        response.put("currentPage", page);
        response.put("pageSize", size);

        return new AppointmentResponse("Appointments retrieved successfully", response);
    }

    public AppointmentResponse getAppointmentsByPatient(Long patientId, String authHeader) {
        List<Appointment> appointments = appointmentRepository.findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(patientId);

        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        appointmentDTOs.forEach(dto -> enrichWithExternalInfo(dto, authHeader));

        return new AppointmentResponse("Appointments found for patient", appointmentDTOs);
    }

    public AppointmentResponse getAppointmentsByVeterinarian(Long veterinarianId, String authHeader) {
        List<Appointment> appointments = appointmentRepository.findByVeterinarianIdOrderByAppointmentDateDescAppointmentTimeDesc(veterinarianId);

        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        appointmentDTOs.forEach(dto -> enrichWithExternalInfo(dto, authHeader));

        return new AppointmentResponse("Appointments found for veterinarian", appointmentDTOs);
    }

    public AppointmentResponse getAppointmentsByDate(LocalDate date, String authHeader) {
        List<Appointment> appointments = appointmentRepository.findByAppointmentDateOrderByAppointmentTime(date);

        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        appointmentDTOs.forEach(dto -> enrichWithExternalInfo(dto, authHeader));

        return new AppointmentResponse("Appointments found for date", appointmentDTOs);
    }

    public AppointmentResponse getTodaysAppointments(String authHeader) {
        List<Appointment> appointments = appointmentRepository.findTodaysAppointments();

        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        appointmentDTOs.forEach(dto -> enrichWithExternalInfo(dto, authHeader));

        return new AppointmentResponse("Today's appointments retrieved", appointmentDTOs);
    }

    public AppointmentResponse getWeeklyAppointments(LocalDate startDate, String authHeader) {
        LocalDate endDate = startDate.plusDays(6);
        List<Appointment> appointments = appointmentRepository.findWeeklyAppointments(startDate, endDate);

        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        appointmentDTOs.forEach(dto -> enrichWithExternalInfo(dto, authHeader));

        return new AppointmentResponse("Weekly appointments retrieved", appointmentDTOs);
    }

    public AppointmentResponse getMonthlyAppointments(int year, int month, String authHeader) {
        List<Appointment> appointments = appointmentRepository.findMonthlyAppointments(year, month);

        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        appointmentDTOs.forEach(dto -> enrichWithExternalInfo(dto, authHeader));

        return new AppointmentResponse("Monthly appointments retrieved", appointmentDTOs);
    }

    public AppointmentResponse getUpcomingAppointments(String authHeader) {
        List<Appointment> appointments = appointmentRepository.findUpcomingAppointments();

        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        appointmentDTOs.forEach(dto -> enrichWithExternalInfo(dto, authHeader));

        return new AppointmentResponse("Upcoming appointments retrieved", appointmentDTOs);
    }

    public AppointmentResponse getAppointmentStats(String authHeader) {
        Long totalAppointments = appointmentRepository.count();
        Long scheduledCount = appointmentRepository.countByStatus(Appointment.Status.SCHEDULED);
        Long confirmedCount = appointmentRepository.countByStatus(Appointment.Status.CONFIRMED);
        Long inProgressCount = appointmentRepository.countByStatus(Appointment.Status.IN_PROGRESS);
        Long completedCount = appointmentRepository.countByStatus(Appointment.Status.COMPLETED);
        Long cancelledCount = appointmentRepository.countByStatus(Appointment.Status.CANCELLED);
        Long noShowCount = appointmentRepository.countByStatus(Appointment.Status.NO_SHOW);
        Long todaysCount = appointmentRepository.countTodaysAppointments();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAppointments", totalAppointments);
        stats.put("scheduledCount", scheduledCount);
        stats.put("confirmedCount", confirmedCount);
        stats.put("inProgressCount", inProgressCount);
        stats.put("completedCount", completedCount);
        stats.put("cancelledCount", cancelledCount);
        stats.put("noShowCount", noShowCount);
        stats.put("todaysCount", todaysCount);

        return new AppointmentResponse("Appointment statistics retrieved", stats);
    }

    public AppointmentResponse searchAppointments(String query, String authHeader) {
        List<Appointment> appointments = appointmentRepository.searchAppointments(query);

        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());

        appointmentDTOs.forEach(dto -> enrichWithExternalInfo(dto, authHeader));

        return new AppointmentResponse("Search completed", appointmentDTOs);
    }

    // Helper method to enrich AppointmentDTO with external service information
    private void enrichWithExternalInfo(AppointmentDTO appointmentDTO, String authHeader) {
        try {
            if (authHeader != null) {
                // Obtener información del paciente
                if (appointmentDTO.getPatientId() != null) {
                    try {
                        var patientResponse = patientServiceClient.getPatientById(appointmentDTO.getPatientId(), authHeader);
                        if (patientResponse.getBody() != null) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> patientData = (Map<String, Object>) patientResponse.getBody().get("patient");
                            if (patientData != null) {
                                String patientName = (String) patientData.get("name");
                                if (patientName != null) {
                                    appointmentDTO.setPatientName(patientName);
                                }
                            }
                        }
                    } catch (Exception e) {
                        // Si falla, continúa sin la información del paciente
                    }
                }

                // Obtener información del veterinario
                if (appointmentDTO.getVeterinarianId() != null) {
                    try {
                        var veterinarianResponse = userServiceClient.getUserById(appointmentDTO.getVeterinarianId(), authHeader);
                        if (veterinarianResponse.getBody() != null) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> veterinarianData = (Map<String, Object>) veterinarianResponse.getBody().get("user");
                            if (veterinarianData != null) {
                                String firstName = (String) veterinarianData.get("firstName");
                                String lastName = (String) veterinarianData.get("lastName");
                                if (firstName != null && lastName != null) {
                                    appointmentDTO.setVeterinarianName(firstName + " " + lastName);
                                }
                            }
                        }
                    } catch (Exception e) {
                        // Si falla, continúa sin la información del veterinario
                    }
                }
            }
        } catch (Exception e) {
            // Si falla la comunicación, simplemente no se enriquece la información
        }
    }
}