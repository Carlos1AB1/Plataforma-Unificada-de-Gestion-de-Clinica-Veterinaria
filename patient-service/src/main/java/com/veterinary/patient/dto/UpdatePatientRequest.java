package com.veterinary.patient.dto;

import com.veterinary.patient.entity.Patient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdatePatientRequest {

    @Size(min = 2, max = 50, message = "Patient name must be between 2 and 50 characters")
    private String name;

    private String species;
    private String breed;
    private String color;
    private Patient.Gender gender;
    private LocalDate birthDate;

    @DecimalMin(value = "0.0", message = "Weight must be positive")
    private BigDecimal weight;

    private String microchipNumber;
    private Boolean isSterilized;
    private Boolean isVaccinated;
    private String allergies;
    private String medicalConditions;
    private String notes;

    public UpdatePatientRequest() {}

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Patient.Gender getGender() {
        return gender;
    }

    public void setGender(Patient.Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getMicrochipNumber() {
        return microchipNumber;
    }

    public void setMicrochipNumber(String microchipNumber) {
        this.microchipNumber = microchipNumber;
    }

    public Boolean getIsSterilized() {
        return isSterilized;
    }

    public void setIsSterilized(Boolean isSterilized) {
        this.isSterilized = isSterilized;
    }

    public Boolean getIsVaccinated() {
        return isVaccinated;
    }

    public void setIsVaccinated(Boolean isVaccinated) {
        this.isVaccinated = isVaccinated;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(String medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}