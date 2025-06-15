package com.veterinary.clinic.prescriptionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "medical-history-service")
public interface MedicalHistoryClient {

    @GetMapping("/api/medical-history/{id}")
    MedicalHistoryResponseDTO getMedicalHistoryById(@PathVariable("id") Long id);

    class MedicalHistoryResponseDTO {
        private Long id;
        private Long patientId;
        private Long veterinarianId;
        private String consultationType;
        private String diagnosis;
        private String treatment;
        private String observations;

        // Constructors
        public MedicalHistoryResponseDTO() {}

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getPatientId() { return patientId; }
        public void setPatientId(Long patientId) { this.patientId = patientId; }

        public Long getVeterinarianId() { return veterinarianId; }
        public void setVeterinarianId(Long veterinarianId) { this.veterinarianId = veterinarianId; }

        public String getConsultationType() { return consultationType; }
        public void setConsultationType(String consultationType) { this.consultationType = consultationType; }

        public String getDiagnosis() { return diagnosis; }
        public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

        public String getTreatment() { return treatment; }
        public void setTreatment(String treatment) { this.treatment = treatment; }

        public String getObservations() { return observations; }
        public void setObservations(String observations) { this.observations = observations; }
    }
}