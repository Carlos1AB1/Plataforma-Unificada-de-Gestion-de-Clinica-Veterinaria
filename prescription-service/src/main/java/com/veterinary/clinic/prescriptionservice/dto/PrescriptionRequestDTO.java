package com.veterinary.clinic.prescriptionservice.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class PrescriptionRequestDTO {

    @NotNull(message = "Medical history ID is required")
    private Long medicalHistoryId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Veterinarian ID is required")
    private Long veterinarianId;

    @NotNull(message = "Prescription date is required")
    private LocalDateTime prescriptionDate;

    @Size(max = 1000, message = "Observations cannot exceed 1000 characters")
    private String observations;

    @NotEmpty(message = "At least one medication is required")
    @Valid
    private List<MedicationDTO> medications;

    // Constructors
    public PrescriptionRequestDTO() {}

    public PrescriptionRequestDTO(Long medicalHistoryId, Long patientId, Long veterinarianId,
                                  LocalDateTime prescriptionDate, String observations,
                                  List<MedicationDTO> medications) {
        this.medicalHistoryId = medicalHistoryId;
        this.patientId = patientId;
        this.veterinarianId = veterinarianId;
        this.prescriptionDate = prescriptionDate;
        this.observations = observations;
        this.medications = medications;
    }

    // Getters and Setters
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

    public List<MedicationDTO> getMedications() { return medications; }
    public void setMedications(List<MedicationDTO> medications) { this.medications = medications; }
}
