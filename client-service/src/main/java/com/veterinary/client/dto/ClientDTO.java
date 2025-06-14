package com.veterinary.client.dto;

import com.veterinary.client.entity.Client;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClientDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String documentNumber;
    private Client.DocumentType documentType;
    private String email;
    private String phoneNumber;
    private String alternativePhone;
    private String address;
    private String city;
    private String postalCode;
    private LocalDate birthDate;
    private Client.Gender gender;
    private String occupation;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private String fullName;
    private String fullAddress;

    public ClientDTO() {}

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.documentNumber = client.getDocumentNumber();
        this.documentType = client.getDocumentType();
        this.email = client.getEmail();
        this.phoneNumber = client.getPhoneNumber();
        this.alternativePhone = client.getAlternativePhone();
        this.address = client.getAddress();
        this.city = client.getCity();
        this.postalCode = client.getPostalCode();
        this.birthDate = client.getBirthDate();
        this.gender = client.getGender();
        this.occupation = client.getOccupation();
        this.emergencyContactName = client.getEmergencyContactName();
        this.emergencyContactPhone = client.getEmergencyContactPhone();
        this.notes = client.getNotes();
        this.isActive = client.getIsActive();
        this.createdAt = client.getCreatedAt();
        this.updatedAt = client.getUpdatedAt();
        this.createdBy = client.getCreatedBy();
        this.updatedBy = client.getUpdatedBy();
        this.fullName = client.getFullName();
        this.fullAddress = client.getFullAddress();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Client.DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Client.DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAlternativePhone() {
        return alternativePhone;
    }

    public void setAlternativePhone(String alternativePhone) {
        this.alternativePhone = alternativePhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Client.Gender getGender() {
        return gender;
    }

    public void setGender(Client.Gender gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}