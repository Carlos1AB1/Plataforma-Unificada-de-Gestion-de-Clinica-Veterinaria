package com.veterinary.patient.controller;

import com.veterinary.patient.dto.*;
import com.veterinary.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient Management", description = "Patient (pets) management operations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    @Operation(summary = "Create a new patient (pet)")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<PatientResponse> createPatient(
            @Valid @RequestBody CreatePatientRequest request,
            Authentication authentication,
            @RequestHeader("Authorization") String authHeader) {
        PatientResponse response = patientService.createPatient(request, authentication.getName(), authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<PatientResponse> getPatientById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        PatientResponse response = patientService.getPatientById(id, authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get patients by client ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<PatientResponse> getPatientsByClientId(
            @PathVariable Long clientId,
            @RequestHeader("Authorization") String authHeader) {
        PatientResponse response = patientService.getPatientsByClientId(clientId, authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/microchip/{microchipNumber}")
    @Operation(summary = "Get patient by microchip number")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<PatientResponse> getPatientByMicrochip(
            @PathVariable String microchipNumber,
            @RequestHeader("Authorization") String authHeader) {
        PatientResponse response = patientService.getPatientByMicrochip(microchipNumber, authHeader);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update patient information")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePatientRequest request,
            Authentication authentication,
            @RequestHeader("Authorization") String authHeader) {
        PatientResponse response = patientService.updatePatient(id, request, authentication.getName(), authHeader);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate patient")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<PatientResponse> activatePatient(
            @PathVariable Long id,
            Authentication authentication) {
        PatientResponse response = patientService.activatePatient(id, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate patient")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<PatientResponse> deactivatePatient(
            @PathVariable Long id,
            Authentication authentication) {
        PatientResponse response = patientService.deactivatePatient(id, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all patients with pagination")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<PatientResponse> getAllPatients(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestHeader("Authorization") String authHeader) {
        PatientResponse response = patientService.getAllPatients(page, size, sortBy, sortDir, authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active patients")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<PatientResponse> getActivePatients(
            @RequestHeader("Authorization") String authHeader) {
        PatientResponse response = patientService.getActivePatients(authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search patients by multiple criteria")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<PatientResponse> searchPatients(
            @Parameter(description = "Search query (name, species, breed, microchip)")
            @RequestParam String query,
            @RequestHeader("Authorization") String authHeader) {
        PatientResponse response = patientService.searchPatients(query, authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/species/{species}")
    @Operation(summary = "Get patients by species")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<PatientResponse> getPatientsBySpecies(
            @PathVariable String species,
            @RequestHeader("Authorization") String authHeader) {
        PatientResponse response = patientService.getPatientsBySpecies(species, authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get patient statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<PatientResponse> getPatientStats(
            @RequestHeader("Authorization") String authHeader) {
        PatientResponse response = patientService.getPatientStats(authHeader);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/species")
    @Operation(summary = "Get all species list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<String>> getAllSpecies() {
        List<String> species = patientService.getAllSpecies();
        return ResponseEntity.ok(species);
    }

    @GetMapping("/breeds")
    @Operation(summary = "Get all breeds list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<String>> getAllBreeds() {
        List<String> breeds = patientService.getAllBreeds();
        return ResponseEntity.ok(breeds);
    }

    @GetMapping("/breeds/{species}")
    @Operation(summary = "Get breeds by species")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<String>> getBreedsBySpecies(@PathVariable String species) {
        List<String> breeds = patientService.getBreedsBySpecies(species);
        return ResponseEntity.ok(breeds);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient (deactivate)")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<PatientResponse> deletePatient(
            @PathVariable Long id,
            Authentication authentication) {
        PatientResponse response = patientService.deletePatient(id, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Patient Service is running!");
    }
}