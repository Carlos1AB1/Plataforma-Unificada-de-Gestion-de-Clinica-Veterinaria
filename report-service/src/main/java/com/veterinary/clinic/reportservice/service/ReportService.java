package com.veterinary.clinic.reportservice.service;

import com.veterinary.clinic.reportservice.dto.*;
import com.veterinary.clinic.reportservice.entity.ReportHistory;
import com.veterinary.clinic.reportservice.repository.ReportHistoryRepository;
import com.veterinary.clinic.reportservice.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportService {

    @Autowired
    private ReportHistoryRepository reportHistoryRepository;

    @Autowired
    private AppointmentClient appointmentClient;

    @Autowired
    private PatientClient patientClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private PrescriptionClient prescriptionClient;

    @Autowired
    private ClientClient clientClient;

    @Transactional(readOnly = true)
    public DashboardMetricsDTO getDashboardMetrics() {
        DashboardMetricsDTO metrics = new DashboardMetricsDTO();

        try {
            // Métricas básicas
            metrics.setTotalPatients(patientClient.getTotalPatientsCount());
            metrics.setTotalUsers(userClient.getTotalUsersCount());
            metrics.setTotalPrescriptions(prescriptionClient.getTotalPrescriptionsCount());

            // Métricas de citas
            metrics.setTodayAppointments(appointmentClient.getTodayAppointmentsCount());
            metrics.setThisWeekAppointments(appointmentClient.getWeekAppointmentsCount());
            metrics.setThisMonthAppointments(appointmentClient.getMonthAppointmentsCount());

            // Pacientes activos y nuevos
            metrics.setNewPatientsThisMonth(patientClient.getNewPatientsThisMonthCount());
            metrics.setActivePatients(patientClient.getTotalPatientsCount()); // Simplificado

            // Estadísticas por categorías
            metrics.setAppointmentsByStatus(getAppointmentsByStatus());
            metrics.setPatientsBySpecies(getPatientsBySpecies());
            metrics.setAppointmentsTrend(getAppointmentsTrend());
            metrics.setPrescriptionsByVeterinarian(getPrescriptionsByVeterinarian());

        } catch (Exception e) {
            // En caso de error, devolver métricas vacías
            System.err.println("Error getting dashboard metrics: " + e.getMessage());
        }

        return metrics;
    }

    @Transactional(readOnly = true)
    public List<AppointmentReportDTO> getAppointmentReport(LocalDate startDate, LocalDate endDate, Long userId) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<AppointmentClient.AppointmentResponseDTO> appointments =
                appointmentClient.getAppointmentsByDateRange(startDateTime, endDateTime);

        List<AppointmentReportDTO> reportData = appointments.stream()
                .map(this::convertToAppointmentReportDTO)
                .collect(Collectors.toList());

        // Guardar historial del reporte
        saveReportHistory("APPOINTMENT_REPORT", userId, startDate, endDate, "EXCEL", reportData.size());

        return reportData;
    }

    @Transactional(readOnly = true)
    public List<PatientReportDTO> getPatientReport(Long userId) {
        List<PatientClient.PatientResponseDTO> patients = patientClient.getAllPatients();

        List<PatientReportDTO> reportData = patients.stream()
                .map(this::convertToPatientReportDTO)
                .collect(Collectors.toList());

        // Guardar historial del reporte
        saveReportHistory("PATIENT_REPORT", userId, null, null, "EXCEL", reportData.size());

        return reportData;
    }

    @Transactional(readOnly = true)
    public FinancialReportDTO getFinancialReport(LocalDate startDate, LocalDate endDate, Long userId) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<AppointmentClient.AppointmentResponseDTO> appointments =
                appointmentClient.getAppointmentsByDateRange(startDateTime, endDateTime);

        FinancialReportDTO report = generateFinancialReport(appointments, startDate, endDate);

        // Guardar historial del reporte
        saveReportHistory("FINANCIAL_REPORT", userId, startDate, endDate, "PDF", appointments.size());

        return report;
    }

    @Transactional(readOnly = true)
    public List<ReportHistory> getReportHistory(Long userId) {
        return reportHistoryRepository.findByGeneratedByUserIdOrderByGeneratedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getReportStatistics() {
        List<Object[]> stats = reportHistoryRepository.getReportStatistics();
        return stats.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));
    }

    private AppointmentReportDTO convertToAppointmentReportDTO(AppointmentClient.AppointmentResponseDTO appointment) {
        AppointmentReportDTO dto = new AppointmentReportDTO();
        dto.setAppointmentId(appointment.getId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setStatus(appointment.getStatus());
        dto.setAppointmentType(appointment.getAppointmentType());
        dto.setObservations(appointment.getObservations());
        dto.setVeterinarianName(appointment.getVeterinarianName());

        // Obtener datos del paciente
        try {
            PatientClient.PatientResponseDTO patient = patientClient.getPatientById(appointment.getPatientId());
            dto.setPatientName(patient.getName());
            dto.setPatientSpecies(patient.getSpecies());

            // Obtener datos del propietario
            if (patient.getOwnerId() != null) {
                ClientClient.ClientResponseDTO owner = clientClient.getClientById(patient.getOwnerId());
                dto.setOwnerName(owner.getFullName());
            }
        } catch (Exception e) {
            dto.setPatientName("Unknown Patient");
            dto.setPatientSpecies("Unknown");
            dto.setOwnerName("Unknown Owner");
        }

        return dto;
    }

    private PatientReportDTO convertToPatientReportDTO(PatientClient.PatientResponseDTO patient) {
        PatientReportDTO dto = new PatientReportDTO();
        dto.setPatientId(patient.getId());
        dto.setPatientName(patient.getName());
        dto.setSpecies(patient.getSpecies());
        dto.setBreed(patient.getBreed());
        dto.setAge(patient.getAge());
        dto.setWeight(patient.getWeight());
        dto.setRegistrationDate(patient.getCreatedAt());

        // Obtener datos del propietario
        try {
            if (patient.getOwnerId() != null) {
                ClientClient.ClientResponseDTO owner = clientClient.getClientById(patient.getOwnerId());
                dto.setOwnerName(owner.getFullName());
                dto.setOwnerDocument(owner.getDocument());
                dto.setOwnerPhone(owner.getPhone());
                dto.setOwnerEmail(owner.getEmail());
            }

            // Obtener estadísticas del paciente
            List<AppointmentClient.AppointmentResponseDTO> appointments =
                    appointmentClient.getAppointmentsByPatient(patient.getId());
            dto.setTotalAppointments((long) appointments.size());

            if (!appointments.isEmpty()) {
                dto.setLastVisit(appointments.get(0).getAppointmentDate());
            }

        } catch (Exception e) {
            dto.setOwnerName("Unknown Owner");
            dto.setTotalAppointments(0L);
        }

        return dto;
    }

    private FinancialReportDTO generateFinancialReport(List<AppointmentClient.AppointmentResponseDTO> appointments,
                                                       LocalDate startDate, LocalDate endDate) {
        FinancialReportDTO report = new FinancialReportDTO(startDate, endDate);

        report.setTotalAppointments((long) appointments.size());

        // Valores simulados para el reporte financiero
        BigDecimal basePrice = new BigDecimal("50.00");
        BigDecimal totalRevenue = basePrice.multiply(new BigDecimal(appointments.size()));
        report.setTotalRevenue(totalRevenue);

        if (appointments.size() > 0) {
            report.setAverageAppointmentValue(totalRevenue.divide(new BigDecimal(appointments.size())));
        } else {
            report.setAverageAppointmentValue(BigDecimal.ZERO);
        }

        // Agrupar por tipo de cita
        Map<String, Long> appointmentsByType = appointments.stream()
                .collect(Collectors.groupingBy(
                        appointment -> appointment.getAppointmentType() != null ?
                                appointment.getAppointmentType() : "General",
                        Collectors.counting()
                ));
        report.setAppointmentsByType(appointmentsByType);

        // Ingresos por veterinario (simulado)
        Map<String, BigDecimal> revenueByVet = appointments.stream()
                .collect(Collectors.groupingBy(
                        appointment -> appointment.getVeterinarianName() != null ?
                                appointment.getVeterinarianName() : "Unknown",
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                count -> basePrice.multiply(new BigDecimal(count))
                        )
                ));
        report.setRevenueByVeterinarian(revenueByVet);

        // Ingresos diarios
        Map<LocalDate, BigDecimal> dailyRevenue = appointments.stream()
                .collect(Collectors.groupingBy(
                        appointment -> appointment.getAppointmentDate().toLocalDate(),
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                count -> basePrice.multiply(new BigDecimal(count))
                        )
                ));
        report.setDailyRevenue(dailyRevenue);

        return report;
    }

    private Map<String, Long> getAppointmentsByStatus() {
        try {
            List<AppointmentClient.AppointmentResponseDTO> appointments = appointmentClient.getAllAppointments();
            return appointments.stream()
                    .collect(Collectors.groupingBy(
                            appointment -> appointment.getStatus() != null ? appointment.getStatus() : "Unknown",
                            Collectors.counting()
                    ));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private Map<String, Long> getPatientsBySpecies() {
        try {
            List<PatientClient.SpeciesStatisticsDTO> stats = patientClient.getPatientsBySpeciesStatistics();
            return stats.stream()
                    .collect(Collectors.toMap(
                            PatientClient.SpeciesStatisticsDTO::getSpecies,
                            PatientClient.SpeciesStatisticsDTO::getCount
                    ));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private Map<LocalDate, Long> getAppointmentsTrend() {
        try {
            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minus(30, ChronoUnit.DAYS);

            List<AppointmentClient.AppointmentResponseDTO> appointments =
                    appointmentClient.getAppointmentsByDateRange(startDate, endDate);

            return appointments.stream()
                    .collect(Collectors.groupingBy(
                            appointment -> appointment.getAppointmentDate().toLocalDate(),
                            Collectors.counting()
                    ));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private Map<String, Long> getPrescriptionsByVeterinarian() {
        try {
            List<PrescriptionClient.PrescriptionResponseDTO> prescriptions = prescriptionClient.getAllPrescriptions();
            return prescriptions.stream()
                    .collect(Collectors.groupingBy(
                            prescription -> prescription.getVeterinarianName() != null ?
                                    prescription.getVeterinarianName() : "Unknown",
                            Collectors.counting()
                    ));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private void saveReportHistory(String reportType, Long userId, LocalDate startDate,
                                   LocalDate endDate, String format, int recordCount) {
        ReportHistory history = new ReportHistory(reportType, userId, format);
        history.setStartDate(startDate);
        history.setEndDate(endDate);
        history.setFileName(generateFileName(reportType, format));
        history.setFileSize((long) (recordCount * 100)); // Estimación simple

        reportHistoryRepository.save(history);
    }

    private String generateFileName(String reportType, String format) {
        String timestamp = LocalDateTime.now().toString().replace(":", "-");
        return reportType.toLowerCase() + "_" + timestamp + "." + format.toLowerCase();
    }
}