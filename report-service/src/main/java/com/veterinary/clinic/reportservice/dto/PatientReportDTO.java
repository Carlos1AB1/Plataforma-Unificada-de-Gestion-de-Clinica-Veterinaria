package com.veterinary.clinic.reportservice.dto;

import java.time.LocalDateTime;

public class PatientReportDTO {

    private Long patientId;
    private String patientName;
    private String species;
    private String breed;
    private Integer age;
    private Double weight;
    private String ownerName;
    private String ownerDocument;
    private String ownerPhone;
    private String ownerEmail;
    private LocalDateTime registrationDate;
    private Long totalAppointments;
    private Long totalPrescriptions;
    private LocalDateTime lastVisit;

    // Constructors
    public PatientReportDTO() {}

    // Getters and Setters
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getOwnerDocument() { return ownerDocument; }
    public void setOwnerDocument(String ownerDocument) { this.ownerDocument = ownerDocument; }

    public String getOwnerPhone() { return ownerPhone; }
    public void setOwnerPhone(String ownerPhone) { this.ownerPhone = ownerPhone; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public Long getTotalAppointments() { return totalAppointments; }
    public void setTotalAppointments(Long totalAppointments) { this.totalAppointments = totalAppointments; }

    public Long getTotalPrescriptions() { return totalPrescriptions; }
    public void setTotalPrescriptions(Long totalPrescriptions) { this.totalPrescriptions = totalPrescriptions; }

    public LocalDateTime getLastVisit() { return lastVisit; }
    public void setLastVisit(LocalDateTime lastVisit) { this.lastVisit = lastVisit; }
}