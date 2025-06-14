package com.veterinary.clinic.medicalhistoryservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class MedicalHistoryCreateDto {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Veterinarian ID is required")
    private Long veterinarianId;

    @NotNull(message = "Diagnosis is required")
    @Size(min = 5, max = 1000, message = "Diagnosis must be between 5 and 1000 characters")
    private String diagnosis;

    @Size(max = 2000, message = "Treatment must not exceed 2000 characters")
    private String treatment;

    @Size(max = 2000, message = "Notes must not exceed 2000 characters")
    private String notes;

    @NotNull(message = "Consultation date is required")
    private LocalDateTime consultationDate;

    // Constructors
    public MedicalHistoryCreateDto() {}

    public MedicalHistoryCreateDto(Long patientId, Long veterinarianId, String diagnosis, String treatment, String notes, LocalDateTime consultationDate) {
        this.patientId = patientId;
        this.veterinarianId = veterinarianId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
        this.consultationDate = consultationDate;
    }

    // Getters and Setters
    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getVeterinarianId() {
        return veterinarianId;
    }

    public void setVeterinarianId(Long veterinarianId) {
        this.veterinarianId = veterinarianId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getConsultationDate() {
        return consultationDate;
    }

    public void setConsultationDate(LocalDateTime consultationDate) {
        this.consultationDate = consultationDate;
    }
}