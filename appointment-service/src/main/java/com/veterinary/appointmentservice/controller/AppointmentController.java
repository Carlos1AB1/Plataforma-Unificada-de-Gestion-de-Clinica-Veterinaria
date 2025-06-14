package com.veterinary.appointmentservice.controller;

import com.veterinary.appointmentservice.dto.*;
import com.veterinary.appointmentservice.entity.Appointment;
import com.veterinary.appointmentservice.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/appointments")
@Tag(name = "Appointment Management", description = "Appointment management operations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "Create a new appointment")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest request,
            Authentication authentication,
            @RequestHeader("Authorization") String authHeader) {
        AppointmentResponse response = appointmentService.createAppointment(request, authentication.getName(), authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> getAppointmentById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        AppointmentResponse response = appointmentService.getAppointmentById(id, authHeader);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update appointment")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentRequest request,
            Authentication authentication,
            @RequestHeader("Authorization") String authHeader) {
        AppointmentResponse response = appointmentService.updateAppointment(id, request, authentication.getName(), authHeader);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel appointment")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> cancelAppointment(
            @PathVariable Long id,
            Authentication authentication,
            @RequestHeader("Authorization") String authHeader) {
        AppointmentResponse response = appointmentService.cancelAppointment(id, authentication.getName(), authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all appointments with pagination")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> getAllAppointments(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "appointmentDate") String sortBy,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestHeader("Authorization") String authHeader) {
        AppointmentResponse response = appointmentService.getAllAppointments(page, size, sortBy, sortDir, authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get appointments by patient ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> getAppointmentsByPatient(
            @PathVariable Long patientId,
            @RequestHeader("Authorization") String authHeader) {
        AppointmentResponse response = appointmentService.getAppointmentsByPatient(patientId, authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/veterinarian/{veterinarianId}")
    @Operation(summary = "Get appointments by veterinarian ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> getAppointmentsByVeterinarian(
            @PathVariable Long veterinarianId,
            @RequestHeader("Authorization") String authHeader) {
        AppointmentResponse response = appointmentService.getAppointmentsByVeterinarian(veterinarianId, authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get appointments by date")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> getAppointmentsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestHeader("Authorization") String authHeader) {
        AppointmentResponse response = appointmentService.getAppointmentsByDate(date, authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/today")
    @Operation(summary = "Get today's appointments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> getTodaysAppointments(
            @RequestHeader("Authorization") String authHeader) {
        AppointmentResponse response = appointmentService.getTodaysAppointments(authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/week")
    @Operation(summary = "Get weekly appointments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> getWeeklyAppointments(
            @Parameter(description = "Start date of the week")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestHeader("Authorization") String authHeader) {
        AppointmentResponse response = appointmentService.getWeeklyAppointments(startDate, authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/month")
    @Operation(summary = "Get monthly appointments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> getMonthlyAppointments(
            @Parameter(description = "Year")
            @RequestParam int year,
            @Parameter(description = "Month (1-12)")
            @RequestParam int month,
            @RequestHeader("Authorization") String authHeader) {
        AppointmentResponse response = appointmentService.getMonthlyAppointments(year, month, authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming appointments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> getUpcomingAppointments(
            @RequestHeader("Authorization") String authHeader) {
        AppointmentResponse response = appointmentService.getUpcomingAppointments(authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search appointments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<AppointmentResponse> searchAppointments(
            @Parameter(description = "Search query")
            @RequestParam String query,
            @RequestHeader("Authorization") String authHeader) {
        AppointmentResponse response = appointmentService.searchAppointments(query, authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get appointment statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<AppointmentResponse> getAppointmentStats(
            @RequestHeader("Authorization") String authHeader) {
        AppointmentResponse response = appointmentService.getAppointmentStats(authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Appointment Service is running!");
    }
}