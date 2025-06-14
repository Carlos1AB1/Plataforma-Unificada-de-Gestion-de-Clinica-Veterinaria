package com.veterinary.clinic.medicalhistoryservice.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MedicalHistoryResponseDto {

    private Long id;
    private Long patientId;
    private String patientName;
    private Long veterinarianId;
    private String veterinarianName;
    private String diagnosis;
    private String treatment;
    private String notes;
    private LocalDateTime consultationDate;
    private List<MedicalDocumentDto> documents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public MedicalHistoryResponseDto() {}

    public MedicalHistoryResponseDto(Long id, Long patientId, String patientName, Long veterinarianId, String veterinarianName,
                                     String diagnosis, String treatment, String notes, LocalDateTime consultationDate,
                                     List<MedicalDocumentDto> documents, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.veterinarianId = veterinarianId;
        this.veterinarianName = veterinarianName;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
        this.consultationDate = consultationDate;
        this.documents = documents;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getVeterinarianId() {
        return veterinarianId;
    }

    public void setVeterinarianId(Long veterinarianId) {
        this.veterinarianId = veterinarianId;
    }

    public String getVeterinarianName() {
        return veterinarianName;
    }

    public void setVeterinarianName(String veterinarianName) {
        this.veterinarianName = veterinarianName;
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

    public List<MedicalDocumentDto> getDocuments() {
        return documents;
    }

    public void setDocuments(List<MedicalDocumentDto> documents) {
        this.documents = documents;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}