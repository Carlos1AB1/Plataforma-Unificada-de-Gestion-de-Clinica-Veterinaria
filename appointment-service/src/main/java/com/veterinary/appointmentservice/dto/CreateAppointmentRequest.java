package com.veterinary.appointmentservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Request to create a new appointment")
public class CreateAppointmentRequest {

    @NotNull(message = "Patient ID is required")
    @Positive(message = "Patient ID must be a positive number")
    @Schema(description = "Patient ID", example = "1", required = true)
    private Long patientId;

    @NotNull(message = "Veterinarian ID is required")
    @Positive(message = "Veterinarian ID must be a positive number")
    @Schema(description = "Veterinarian ID", example = "1", required = true)
    private Long veterinarianId;

    @NotNull(message = "Appointment date is required")
    // @Future(message = "Appointment date must be in the future") // TEMPORALMENTE COMENTADO PARA PRUEBAS
    @Schema(description = "Appointment date", example = "2024-06-15", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    @Schema(description = "Appointment time", example = "10:30", required = true)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime appointmentTime;

    @NotBlank(message = "Reason is required")
    @Size(min = 5, max = 500, message = "Reason must be between 5 and 500 characters")
    @Schema(description = "Reason for appointment", example = "Regular checkup", required = true)
    private String reason;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    @Schema(description = "Additional notes", example = "Patient seems anxious")
    private String notes;

    @Min(value = 15, message = "Duration must be at least 15 minutes")
    @Max(value = 240, message = "Duration cannot exceed 240 minutes")
    @Schema(description = "Duration in minutes", example = "30")
    private Integer durationMinutes = 30;

    public CreateAppointmentRequest() {}

    public CreateAppointmentRequest(Long patientId, Long veterinarianId, LocalDate appointmentDate,
                                    LocalTime appointmentTime, String reason) {
        this.patientId = patientId;
        this.veterinarianId = veterinarianId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.reason = reason;
    }

    // Getters and Setters
    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

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

    @Override
    public String toString() {
        return "CreateAppointmentRequest{" +
                "patientId=" + patientId +
                ", veterinarianId=" + veterinarianId +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", reason='" + reason + '\'' +
                ", notes='" + notes + '\'' +
                ", durationMinutes=" + durationMinutes +
                '}';
    }
}