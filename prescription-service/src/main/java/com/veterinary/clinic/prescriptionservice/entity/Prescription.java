package com.veterinary.clinic.prescriptionservice.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "medical_history_id")
    private Long medicalHistoryId;

    @NotNull
    @Column(name = "patient_id")
    private Long patientId;

    @NotNull
    @Column(name = "veterinarian_id")
    private Long veterinarianId;

    @NotNull
    @Column(name = "prescription_date")
    private LocalDateTime prescriptionDate;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status = PrescriptionStatus.ACTIVE;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Medication> medications;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Prescription() {}

    public Prescription(Long medicalHistoryId, Long patientId, Long veterinarianId,
                        LocalDateTime prescriptionDate, String observations) {
        this.medicalHistoryId = medicalHistoryId;
        this.patientId = patientId;
        this.veterinarianId = veterinarianId;
        this.prescriptionDate = prescriptionDate;
        this.observations = observations;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMedicalHistoryId() { return medicalHistoryId; }
    public void setMedicalHistoryId(Long medicalHistoryId) { this.medicalHistoryId = medicalHistoryId; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getVeterinarianId() { return veterinarianId; }
    public void setVeterinarianId(Long veterinarianId) { this.veterinarianId = veterinarianId; }

    public LocalDateTime getPrescriptionDate() { return prescriptionDate; }
    public void setPrescriptionDate(LocalDateTime prescriptionDate) { this.prescriptionDate = prescriptionDate; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public PrescriptionStatus getStatus() { return status; }
    public void setStatus(PrescriptionStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<Medication> getMedications() { return medications; }
    public void setMedications(List<Medication> medications) { this.medications = medications; }

    public enum PrescriptionStatus {
        ACTIVE, COMPLETED, CANCELLED
    }
}