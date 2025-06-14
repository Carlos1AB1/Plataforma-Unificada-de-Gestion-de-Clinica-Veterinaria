package com.veterinary.clinic.medicalhistoryservice.controller;

import com.veterinary.clinic.medicalhistoryservice.dto.MedicalHistoryCreateDto;
import com.veterinary.clinic.medicalhistoryservice.dto.MedicalHistoryResponseDto;
import com.veterinary.clinic.medicalhistoryservice.dto.MedicalHistoryUpdateDto;
import com.veterinary.clinic.medicalhistoryservice.service.MedicalHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/medical-history")
@Tag(name = "Medical History", description = "Medical History management operations")
@SecurityRequirement(name = "bearerAuth")
public class MedicalHistoryController {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @Operation(summary = "Create new medical history record", description = "Creates a new medical history record for a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medical history created successfully",
                    content = @Content(schema = @Schema(implementation = MedicalHistoryResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Patient or veterinarian not found")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<MedicalHistoryResponseDto> createMedicalHistory(
            @Valid @RequestBody MedicalHistoryCreateDto createDto) {
        MedicalHistoryResponseDto response = medicalHistoryService.createMedicalHistory(createDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get medical history by ID", description = "Retrieves a specific medical history record by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical history found",
                    content = @Content(schema = @Schema(implementation = MedicalHistoryResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Medical history not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<MedicalHistoryResponseDto> getMedicalHistoryById(
            @Parameter(description = "Medical history ID") @PathVariable Long id) {
        MedicalHistoryResponseDto response = medicalHistoryService.getMedicalHistoryById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get medical history by patient ID", description = "Retrieves all medical history records for a specific patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical histories retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<MedicalHistoryResponseDto>> getMedicalHistoryByPatientId(
            @Parameter(description = "Patient ID") @PathVariable Long patientId) {
        List<MedicalHistoryResponseDto> response = medicalHistoryService.getMedicalHistoryByPatientId(patientId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get medical history by patient ID with pagination", description = "Retrieves medical history records for a specific patient with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical histories retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/patient/{patientId}/paginated")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<Page<MedicalHistoryResponseDto>> getMedicalHistoryByPatientIdPaginated(
            @Parameter(description = "Patient ID") @PathVariable Long patientId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MedicalHistoryResponseDto> response = medicalHistoryService.getMedicalHistoryByPatientId(patientId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get medical history by veterinarian ID", description = "Retrieves all medical history records created by a specific veterinarian")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical histories retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/veterinarian/{veterinarianId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<List<MedicalHistoryResponseDto>> getMedicalHistoryByVeterinarianId(
            @Parameter(description = "Veterinarian ID") @PathVariable Long veterinarianId) {
        List<MedicalHistoryResponseDto> response = medicalHistoryService.getMedicalHistoryByVeterinarianId(veterinarianId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get medical history by date range", description = "Retrieves medical history records within a specific date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical histories retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Invalid date format")
    })
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<List<MedicalHistoryResponseDto>> getMedicalHistoryByDateRange(
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<MedicalHistoryResponseDto> response = medicalHistoryService.getMedicalHistoryByDateRange(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get medical history by patient and date range", description = "Retrieves medical history records for a specific patient within a date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical histories retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Invalid date format")
    })
    @GetMapping("/patient/{patientId}/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<MedicalHistoryResponseDto>> getMedicalHistoryByPatientAndDateRange(
            @Parameter(description = "Patient ID") @PathVariable Long patientId,
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<MedicalHistoryResponseDto> response = medicalHistoryService.getMedicalHistoryByPatientAndDateRange(patientId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search medical history by diagnosis", description = "Searches medical history records by diagnosis keyword")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical histories retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<List<MedicalHistoryResponseDto>> searchByDiagnosis(
            @Parameter(description = "Diagnosis keyword to search") @RequestParam String diagnosis) {
        List<MedicalHistoryResponseDto> response = medicalHistoryService.searchByDiagnosis(diagnosis);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update medical history", description = "Updates an existing medical history record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical history updated successfully",
                    content = @Content(schema = @Schema(implementation = MedicalHistoryResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Medical history not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<MedicalHistoryResponseDto> updateMedicalHistory(
            @Parameter(description = "Medical history ID") @PathVariable Long id,
            @Valid @RequestBody MedicalHistoryUpdateDto updateDto) {
        MedicalHistoryResponseDto response = medicalHistoryService.updateMedicalHistory(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete medical history", description = "Deletes a medical history record and its associated files")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medical history deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Medical history not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMedicalHistory(
            @Parameter(description = "Medical history ID") @PathVariable Long id) {
        medicalHistoryService.deleteMedicalHistory(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Count medical histories by patient", description = "Returns the total number of medical history records for a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/patient/{patientId}/count")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<Long> countByPatientId(
            @Parameter(description = "Patient ID") @PathVariable Long patientId) {
        long count = medicalHistoryService.countByPatientId(patientId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get last medical history by patient", description = "Retrieves the most recent medical history record for a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Last medical history retrieved successfully",
                    content = @Content(schema = @Schema(implementation = MedicalHistoryResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "No medical history found for patient")
    })
    @GetMapping("/patient/{patientId}/last")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<MedicalHistoryResponseDto> getLastMedicalHistoryByPatientId(
            @Parameter(description = "Patient ID") @PathVariable Long patientId) {
        MedicalHistoryResponseDto response = medicalHistoryService.getLastMedicalHistoryByPatientId(patientId);
        return ResponseEntity.ok(response);
    }
}