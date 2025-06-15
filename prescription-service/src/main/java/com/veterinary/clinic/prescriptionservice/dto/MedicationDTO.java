package com.veterinary.clinic.prescriptionservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MedicationDTO {

    private Long id;

    @NotBlank(message = "Medication name is required")
    @Size(max = 255, message = "Medication name cannot exceed 255 characters")
    private String medicationName;

    @NotBlank(message = "Dosage is required")
    @Size(max = 100, message = "Dosage cannot exceed 100 characters")
    private String dosage;

    @NotBlank(message = "Frequency is required")
    @Size(max = 100, message = "Frequency cannot exceed 100 characters")
    private String frequency;

    @NotBlank(message = "Duration is required")
    @Size(max = 100, message = "Duration cannot exceed 100 characters")
    private String duration;

    @Size(max = 500, message = "Instructions cannot exceed 500 characters")
    private String instructions;

    // Constructors
    public MedicationDTO() {}

    public MedicationDTO(String medicationName, String dosage, String frequency,
                         String duration, String instructions) {
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.instructions = instructions;
    }

    public MedicationDTO(Long id, String medicationName, String dosage, String frequency,
                         String duration, String instructions) {
        this.id = id;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.instructions = instructions;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMedicationName() { return medicationName; }
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
}