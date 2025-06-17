package com.veterinary.appointmentservice.dto;

import com.veterinary.appointmentservice.entity.Appointment;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Schema(description = "Appointment Data Transfer Object")
public class AppointmentDTO {

    @Schema(description = "Appointment ID", example = "1")
    private Long id;

    @Schema(description = "Patient ID", example = "1")
    private Long patientId;

    @Schema(description = "Patient name", example = "Max")
    private String patientName; // Filled from patient service

    @Schema(description = "Veterinarian ID", example = "1")
    private Long veterinarianId;

    @Schema(description = "Veterinarian name", example = "Dr. Smith")
    private String veterinarianName; // Filled from user service

    @Schema(description = "Appointment date", example = "2024-06-15")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @Schema(description = "Appointment time", example = "10:30")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime appointmentTime;

    @Schema(description = "Reason for appointment", example = "Regular checkup")
    private String reason;

    @Schema(description = "Appointment status", example = "SCHEDULED")
    private Appointment.Status status;

    @Schema(description = "Additional notes", example = "Patient seems anxious")
    private String notes;

    @Schema(description = "Duration in minutes", example = "30")
    private Integer durationMinutes;

    @Schema(description = "Creation timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "Created by user", example = "admin")
    private String createdBy;

    @Schema(description = "Last updated by user", example = "admin")
    private String updatedBy;

    @Schema(description = "Appointment date and time combined")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentDateTime;

    @Schema(description = "Appointment end time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentEndTime;

    @Schema(description = "Is appointment in the past", example = "false")
    private boolean isInPast;

    @Schema(description = "Is appointment today", example = "true")
    private boolean isToday;

    public AppointmentDTO() {}

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.patientId = appointment.getPatientId();
        this.veterinarianId = appointment.getVeterinarianId();
        this.appointmentDate = appointment.getAppointmentDate();
        this.appointmentTime = appointment.getAppointmentTime();
        this.reason = appointment.getReason();
        this.status = appointment.getStatus();
        this.notes = appointment.getNotes();
        this.durationMinutes = appointment.getDurationMinutes();
        this.createdAt = appointment.getCreatedAt();
        this.updatedAt = appointment.getUpdatedAt();
        this.createdBy = appointment.getCreatedBy();
        this.updatedBy = appointment.getUpdatedBy();
        this.appointmentDateTime = appointment.getAppointmentDateTime() != null ? appointment.getAppointmentDateTime() : LocalDateTime.of(appointmentDate, appointmentTime);
        this.appointmentEndTime = appointment.getAppointmentEndTime() != null ? appointment.getAppointmentEndTime() : appointmentDateTime.plusMinutes(durationMinutes);
        this.isInPast = appointment.isInPast();
        this.isToday = appointment.isToday();
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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getVeterinarianId() {
        return veterinarianId;
    }

    public void setVeterinarianId(Long veterinarianId) {
        this.veterinarianId = veterinarianId;
    }

    public String getVeterinarianName() {
        return veterinarianName;
    }

    public void setVeterinarianName(String veterinarianName) {
        this.veterinarianName = veterinarianName;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Appointment.Status getStatus() {
        return status;
    }

    public void setStatus(Appointment.Status status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
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

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public LocalDateTime getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public void setAppointmentEndTime(LocalDateTime appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public boolean isInPast() {
        return isInPast;
    }

    public void setInPast(boolean inPast) {
        isInPast = inPast;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    @Override
    public String toString() {
        return "AppointmentDTO{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", veterinarianId=" + veterinarianId +
                ", veterinarianName='" + veterinarianName + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                ", notes='" + notes + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}