package com.veterinary.patient.dto;

import com.veterinary.patient.entity.Patient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientDTO {

    private Long id;
    private String name;
    private Long clientId;
    private String clientName; // Filled from client service
    private String species;
    private String breed;
    private String color;
    private Patient.Gender gender;
    private LocalDate birthDate;
    private BigDecimal weight;
    private String microchipNumber;
    private Boolean isSterilized;
    private Boolean isVaccinated;
    private String allergies;
    private String medicalConditions;
    private String notes;
    private LocalDate registrationDate;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private Integer ageInYears;
    private String fullDescription;

    public PatientDTO() {}

    public PatientDTO(Patient patient) {
        this.id = patient.getId();
        this.name = patient.getName();
        this.clientId = patient.getClientId();
        this.species = patient.getSpecies();
        this.breed = patient.getBreed();
        this.color = patient.getColor();
        this.gender = patient.getGender();
        this.birthDate = patient.getBirthDate();
        this.weight = patient.getWeight();
        this.microchipNumber = patient.getMicrochipNumber();
        this.isSterilized = patient.getIsSterilized();
        this.isVaccinated = patient.getIsVaccinated();
        this.allergies = patient.getAllergies();
        this.medicalConditions = patient.getMedicalConditions();
        this.notes = patient.getNotes();
        this.registrationDate = patient.getRegistrationDate();
        this.isActive = patient.getIsActive();
        this.createdAt = patient.getCreatedAt();
        this.updatedAt = patient.getUpdatedAt();
        this.createdBy = patient.getCreatedBy();
        this.updatedBy = patient.getUpdatedBy();
        this.ageInYears = patient.getAgeInYears();
        this.fullDescription = patient.getFullDescription();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getAgeInYears() {
        return ageInYears;
    }

    public void setAgeInYears(Integer ageInYears) {
        this.ageInYears = ageInYears;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
}