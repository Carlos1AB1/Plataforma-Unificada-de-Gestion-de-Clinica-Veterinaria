package com.veterinary.clinic.reportservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "appointment-service")
public interface AppointmentClient {

    @GetMapping("/api/appointments")
    List<AppointmentResponseDTO> getAllAppointments();

    @GetMapping("/api/appointments/{id}")
    AppointmentResponseDTO getAppointmentById(@PathVariable("id") Long id);

    @GetMapping("/api/appointments/date-range")
    List<AppointmentResponseDTO> getAppointmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate);

    @GetMapping("/api/appointments/patient/{patientId}")
    List<AppointmentResponseDTO> getAppointmentsByPatient(@PathVariable("patientId") Long patientId);

    @GetMapping("/api/appointments/veterinarian/{veterinarianId}")
    List<AppointmentResponseDTO> getAppointmentsByVeterinarian(@PathVariable("veterinarianId") Long veterinarianId);

    @GetMapping("/api/appointments/count/today")
    Long getTodayAppointmentsCount();

    @GetMapping("/api/appointments/count/week")
    Long getWeekAppointmentsCount();

    @GetMapping("/api/appointments/count/month")
    Long getMonthAppointmentsCount();

    class AppointmentResponseDTO {
        private Long id;
        private Long patientId;
        private Long veterinarianId;
        private LocalDateTime appointmentDate;
        private String status;
        private String appointmentType;
        private String observations;
        private String patientName;
        private String veterinarianName;

        // Constructors
        public AppointmentResponseDTO() {}

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getPatientId() { return patientId; }
        public void setPatientId(Long patientId) { this.patientId = patientId; }

        public Long getVeterinarianId() { return veterinarianId; }
        public void setVeterinarianId(Long veterinarianId) { this.veterinarianId = veterinarianId; }

        public LocalDateTime getAppointmentDate() { return appointmentDate; }
        public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getAppointmentType() { return appointmentType; }
        public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }

        public String getObservations() { return observations; }
        public void setObservations(String observations) { this.observations = observations; }

        public String getPatientName() { return patientName; }
        public void setPatientName(String patientName) { this.patientName = patientName; }

        public String getVeterinarianName() { return veterinarianName; }
        public void setVeterinarianName(String veterinarianName) { this.veterinarianName = veterinarianName; }
    }
}
