package com.veterinary.clinic.prescriptionservice.controller;

import com.veterinary.clinic.prescriptionservice.dto.PrescriptionRequestDTO;
import com.veterinary.clinic.prescriptionservice.dto.PrescriptionResponseDTO;
import com.veterinary.clinic.prescriptionservice.entity.Prescription;
import com.veterinary.clinic.prescriptionservice.service.PrescriptionService;
import com.veterinary.clinic.prescriptionservice.service.PdfGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@Tag(name = "Prescription Management", description = "Operations related to prescription management")
@SecurityRequirement(name = "bearerAuth")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @PostMapping
    @Operation(summary = "Create new prescription", description = "Create a new prescription for a patient")
    @PreAuthorize("hasRole('VETERINARIO') or hasRole('ADMIN')")
    public ResponseEntity<PrescriptionResponseDTO> createPrescription(@Valid @RequestBody PrescriptionRequestDTO requestDTO) {
        PrescriptionResponseDTO prescription = prescriptionService.createPrescription(requestDTO);
        return new ResponseEntity<>(prescription, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get prescription by ID", description = "Retrieve a specific prescription by its ID")
    @PreAuthorize("hasRole('VETERINARIO') or hasRole('ADMIN') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<PrescriptionResponseDTO> getPrescriptionById(@PathVariable Long id) {
        PrescriptionResponseDTO prescription = prescriptionService.getPrescriptionById(id);
        return ResponseEntity.ok(prescription);
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get prescriptions by patient", description = "Retrieve all prescriptions for a specific patient")
    @PreAuthorize("hasRole('VETERINARIO') or hasRole('ADMIN') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<PrescriptionResponseDTO>> getPrescriptionsByPatient(@PathVariable Long patientId) {
        List<PrescriptionResponseDTO> prescriptions = prescriptionService.getPrescriptionsByPatient(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/veterinarian/{veterinarianId}")
    @Operation(summary = "Get prescriptions by veterinarian", description = "Retrieve all prescriptions created by a specific veterinarian")
    @PreAuthorize("hasRole('VETERINARIO') or hasRole('ADMIN')")
    public ResponseEntity<List<PrescriptionResponseDTO>> getPrescriptionsByVeterinarian(@PathVariable Long veterinarianId) {
        List<PrescriptionResponseDTO> prescriptions = prescriptionService.getPrescriptionsByVeterinarian(veterinarianId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/medical-history/{medicalHistoryId}")
    @Operation(summary = "Get prescriptions by medical history", description = "Retrieve all prescriptions for a specific medical history")
    @PreAuthorize("hasRole('VETERINARIO') or hasRole('ADMIN') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<PrescriptionResponseDTO>> getPrescriptionsByMedicalHistory(@PathVariable Long medicalHistoryId) {
        List<PrescriptionResponseDTO> prescriptions = prescriptionService.getPrescriptionsByMedicalHistory(medicalHistoryId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping
    @Operation(summary = "Get all prescriptions", description = "Retrieve all prescriptions with pagination")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<Page<PrescriptionResponseDTO>> getAllPrescriptions(Pageable pageable) {
        Page<PrescriptionResponseDTO> prescriptions = prescriptionService.getAllPrescriptions(pageable);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get prescriptions by date range", description = "Retrieve prescriptions within a specific date range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<PrescriptionResponseDTO>> getPrescriptionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<PrescriptionResponseDTO> prescriptions = prescriptionService.getPrescriptionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(prescriptions);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update prescription status", description = "Update the status of a prescription")
    @PreAuthorize("hasRole('VETERINARIO') or hasRole('ADMIN')")
    public ResponseEntity<PrescriptionResponseDTO> updatePrescriptionStatus(
            @PathVariable Long id,
            @RequestParam Prescription.PrescriptionStatus status) {
        PrescriptionResponseDTO prescription = prescriptionService.updatePrescriptionStatus(id, status);
        return ResponseEntity.ok(prescription);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete prescription", description = "Delete a prescription by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pdf")
    @Operation(summary = "Generate prescription PDF", description = "Generate a PDF prescription document")
    @PreAuthorize("hasRole('VETERINARIO') or hasRole('ADMIN') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<byte[]> generatePrescriptionPdf(@PathVariable Long id) {
        PrescriptionResponseDTO prescription = prescriptionService.getPrescriptionById(id);
        byte[] pdfBytes = pdfGeneratorService.generatePrescriptionPdf(prescription);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "prescription_" + id + ".pdf");
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/veterinarian/{veterinarianId}/today-count")
    @Operation(summary = "Get today's prescription count by veterinarian", description = "Get the count of prescriptions created today by a veterinarian")
    @PreAuthorize("hasRole('VETERINARIO') or hasRole('ADMIN')")
    public ResponseEntity<Long> getTodayPrescriptionsCount(@PathVariable Long veterinarianId) {
        Long count = prescriptionService.getTodayPrescriptionsCountByVeterinarian(veterinarianId);
        return ResponseEntity.ok(count);
    }
}