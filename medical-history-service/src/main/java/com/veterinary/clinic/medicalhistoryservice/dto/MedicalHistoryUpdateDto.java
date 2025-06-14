package com.veterinary.clinic.medicalhistoryservice.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class MedicalHistoryUpdateDto {

    @Size(min = 5, max = 1000, message = "Diagnosis must be between 5 and 1000 characters")
    private String diagnosis;

    @Size(max = 2000, message = "Treatment must not exceed 2000 characters")
    private String treatment;

    @Size(max = 2000, message = "Notes must not exceed 2000 characters")
    private String notes;

    private LocalDateTime consultationDate;

    // Constructors
    public MedicalHistoryUpdateDto() {}

    public MedicalHistoryUpdateDto(String diagnosis, String treatment, String notes, LocalDateTime consultationDate) {
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
        this.consultationDate = consultationDate;
    }

    // Getters and Setters
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