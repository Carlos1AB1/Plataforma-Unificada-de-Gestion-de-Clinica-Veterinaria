package com.veterinary.clinic.reportservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "prescription-service")
public interface PrescriptionClient {

    @GetMapping("/api/prescriptions")
    List<PrescriptionResponseDTO> getAllPrescriptions();

    @GetMapping("/api/prescriptions/veterinarian/{veterinarianId}")
    List<PrescriptionResponseDTO> getPrescriptionsByVeterinarian(@PathVariable("veterinarianId") Long veterinarianId);

    @GetMapping("/api/prescriptions/date-range")
    List<PrescriptionResponseDTO> getPrescriptionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate);

    @GetMapping("/api/prescriptions/count/total")
    Long getTotalPrescriptionsCount();

    class PrescriptionResponseDTO {
        private Long id;
        private Long patientId;
        private Long veterinarianId;
        private String patientName;
        private String veterinarianName;
        private LocalDateTime prescriptionDate;
        private String status;

        // Constructors
        public PrescriptionResponseDTO() {}

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getPatientId() { return patientId; }
        public void setPatientId(Long patientId) { this.patientId = patientId; }

        public Long getVeterinarianId() { return veterinarianId; }
        public void setVeterinarianId(Long veterinarianId) { this.veterinarianId = veterinarianId; }

        public String getPatientName() { return patientName; }
        public void setPatientName(String patientName) { this.patientName = patientName; }

        public String getVeterinarianName() { return veterinarianName; }
        public void setVeterinarianName(String veterinarianName) { this.veterinarianName = veterinarianName; }

        public LocalDateTime getPrescriptionDate() { return prescriptionDate; }
        public void setPrescriptionDate(LocalDateTime prescriptionDate) { this.prescriptionDate = prescriptionDate; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}