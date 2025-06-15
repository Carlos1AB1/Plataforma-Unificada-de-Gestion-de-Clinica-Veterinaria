package com.veterinary.clinic.reportservice.dto;

import java.time.LocalDateTime;

public class AppointmentReportDTO {

    private Long appointmentId;
    private String patientName;
    private String patientSpecies;
    private String ownerName;
    private String veterinarianName;
    private LocalDateTime appointmentDate;
    private String status;
    private String appointmentType;
    private String observations;

    // Constructors
    public AppointmentReportDTO() {}

    public AppointmentReportDTO(Long appointmentId, String patientName, String patientSpecies,
                                String ownerName, String veterinarianName, LocalDateTime appointmentDate,
                                String status, String appointmentType, String observations) {
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.patientSpecies = patientSpecies;
        this.ownerName = ownerName;
        this.veterinarianName = veterinarianName;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.appointmentType = appointmentType;
        this.observations = observations;
    }

    // Getters and Setters
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientSpecies() { return patientSpecies; }
    public void setPatientSpecies(String patientSpecies) { this.patientSpecies = patientSpecies; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getVeterinarianName() { return veterinarianName; }
    public void setVeterinarianName(String veterinarianName) { this.veterinarianName = veterinarianName; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAppointmentType() { return appointmentType; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
}