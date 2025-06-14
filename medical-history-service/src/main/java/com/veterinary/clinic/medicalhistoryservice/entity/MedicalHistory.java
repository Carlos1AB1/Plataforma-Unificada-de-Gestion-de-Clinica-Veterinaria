package com.veterinary.clinic.medicalhistoryservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medical_histories")
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @NotNull
    @Column(name = "veterinarian_id", nullable = false)
    private Long veterinarianId;

    @NotNull
    @Size(min = 5, max = 1000, message = "Diagnosis must be between 5 and 1000 characters")
    @Column(name = "diagnosis", nullable = false, length = 1000)
    private String diagnosis;

    @Size(max = 2000, message = "Treatment must not exceed 2000 characters")
    @Column(name = "treatment", length = 2000)
    private String treatment;

    @Size(max = 2000, message = "Notes must not exceed 2000 characters")
    @Column(name = "notes", length = 2000)
    private String notes;

    @Column(name = "consultation_date", nullable = false)
    private LocalDateTime consultationDate;

    @OneToMany(mappedBy = "medicalHistory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MedicalDocument> documents = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public MedicalHistory() {}

    public MedicalHistory(Long patientId, Long veterinarianId, String diagnosis, String treatment, String notes, LocalDateTime consultationDate) {
        this.patientId = patientId;
        this.veterinarianId = veterinarianId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
        this.consultationDate = consultationDate;
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

    public List<MedicalDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<MedicalDocument> documents) {
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