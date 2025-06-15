package com.veterinary.clinic.prescriptionservice.dto;


import com.veterinary.clinic.prescriptionservice.entity.Prescription;
import java.time.LocalDateTime;
import java.util.List;

public class PrescriptionResponseDTO {

    private Long id;
    private Long medicalHistoryId;
    private Long patientId;
    private Long veterinarianId;
    private String patientName;
    private String veterinarianName;
    private LocalDateTime prescriptionDate;
    private String observations;
    private String status;
    private List<MedicationDTO> medications;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public PrescriptionResponseDTO() {}

    public PrescriptionResponseDTO(Long id, Long medicalHistoryId, Long patientId,
                                   Long veterinarianId, String patientName, String veterinarianName,
                                   LocalDateTime prescriptionDate, String observations, String status,
                                   List<MedicationDTO> medications, LocalDateTime createdAt,
                                   LocalDateTime updatedAt) {
        this.id = id;
        this.medicalHistoryId = medicalHistoryId;
        this.patientId = patientId;
        this.veterinarianId = veterinarianId;
        this.patientName = patientName;
        this.veterinarianName = veterinarianName;
        this.prescriptionDate = prescriptionDate;
        this.observations = observations;
        this.status = status;
        this.medications = medications;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getVeterinarianName() { return veterinarianName; }
    public void setVeterinarianName(String veterinarianName) { this.veterinarianName = veterinarianName; }

    public LocalDateTime getPrescriptionDate() { return prescriptionDate; }
    public void setPrescriptionDate(LocalDateTime prescriptionDate) { this.prescriptionDate = prescriptionDate; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<MedicationDTO> getMedications() { return medications; }
    public void setMedications(List<MedicationDTO> medications) { this.medications = medications; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
