package com.veterinary.appointmentservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.veterinary.appointmentservice.entity.Appointment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Request to update an existing appointment")
public class UpdateAppointmentRequest {

    @Positive(message = "Veterinarian ID must be a positive number")
    @Schema(description = "Veterinarian ID", example = "1")
    private Long veterinarianId;

    @Schema(description = "Appointment date", example = "2024-06-15")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @Schema(description = "Appointment time", example = "10:30")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime appointmentTime;

    @Size(min = 5, max = 500, message = "Reason must be between 5 and 500 characters")
    @Schema(description = "Reason for appointment", example = "Regular checkup")
    private String reason;

    @Schema(description = "Appointment status", example = "CONFIRMED")
    private Appointment.Status status;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    @Schema(description = "Additional notes", example = "Patient seems anxious")
    private String notes;

    @Min(value = 15, message = "Duration must be at least 15 minutes")
    @Max(value = 240, message = "Duration cannot exceed 240 minutes")
    @Schema(description = "Duration in minutes", example = "30")
    private Integer durationMinutes;

    public UpdateAppointmentRequest() {}

    // Getters and Setters
    public Long getVeterinarianId() {
        return veterinarianId;
    }

    public void setVeterinarianId(Long veterinarianId) {
        this.veterinarianId = veterinarianId;
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

    // Validation methods
    public boolean hasAnyFieldToUpdate() {
        return veterinarianId != null ||
                appointmentDate != null ||
                appointmentTime != null ||
                reason != null ||
                status != null ||
                notes != null ||
                durationMinutes != null;
    }

    public boolean hasSchedulingChanges() {
        return veterinarianId != null ||
                appointmentDate != null ||
                appointmentTime != null ||
                durationMinutes != null;
    }

    @Override
    public String toString() {
        return "UpdateAppointmentRequest{" +
                "veterinarianId=" + veterinarianId +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                ", notes='" + notes + '\'' +
                ", durationMinutes=" + durationMinutes +
                '}';
    }
}