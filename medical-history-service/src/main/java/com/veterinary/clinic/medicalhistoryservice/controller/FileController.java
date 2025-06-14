package com.veterinary.clinic.medicalhistoryservice.controller;

import com.veterinary.clinic.medicalhistoryservice.dto.MedicalDocumentDto;
import com.veterinary.clinic.medicalhistoryservice.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/medical-history")
@Tag(name = "Medical Documents", description = "Medical document file management operations")
@SecurityRequirement(name = "bearerAuth")
public class FileController {

    @Autowired
    private FileService fileService;

    @Operation(summary = "Upload medical document", description = "Uploads a medical document file (PDF, JPG, PNG) to a medical history record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File uploaded successfully",
                    content = @Content(schema = @Schema(implementation = MedicalDocumentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file or file size exceeds limit"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Medical history not found")
    })
    @PostMapping("/{medicalHistoryId}/documents")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<MedicalDocumentDto> uploadFile(
            @Parameter(description = "Medical history ID") @PathVariable Long medicalHistoryId,
            @Parameter(description = "File to upload (PDF, JPG, PNG, max 10MB)")
            @RequestParam("file") MultipartFile file) {
        MedicalDocumentDto response = fileService.uploadFile(medicalHistoryId, file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Download medical document", description = "Downloads a medical document file by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File downloaded successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @GetMapping("/documents/{documentId}/download")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<Resource> downloadFile(
            @Parameter(description = "Document ID") @PathVariable Long documentId) {

        MedicalDocumentDto document = fileService.getDocumentById(documentId);
        Resource resource = fileService.downloadFile(documentId);

        String contentType = "application/octet-stream";
        if (document.getFileType().equalsIgnoreCase("pdf")) {
            contentType = "application/pdf";
        } else if (document.getFileType().equalsIgnoreCase("jpg") || document.getFileType().equalsIgnoreCase("jpeg")) {
            contentType = "image/jpeg";
        } else if (document.getFileType().equalsIgnoreCase("png")) {
            contentType = "image/png";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                .body(resource);
    }

    @Operation(summary = "Get document info", description = "Retrieves information about a medical document by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document info retrieved successfully",
                    content = @Content(schema = @Schema(implementation = MedicalDocumentDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @GetMapping("/documents/{documentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<MedicalDocumentDto> getDocumentById(
            @Parameter(description = "Document ID") @PathVariable Long documentId) {
        MedicalDocumentDto response = fileService.getDocumentById(documentId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get documents by medical history", description = "Retrieves all documents associated with a medical history record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documents retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{medicalHistoryId}/documents")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<MedicalDocumentDto>> getDocumentsByMedicalHistoryId(
            @Parameter(description = "Medical history ID") @PathVariable Long medicalHistoryId) {
        List<MedicalDocumentDto> response = fileService.getDocumentsByMedicalHistoryId(medicalHistoryId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete medical document", description = "Deletes a medical document file and its database record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @DeleteMapping("/documents/{documentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<Void> deleteDocument(
            @Parameter(description = "Document ID") @PathVariable Long documentId) {
        fileService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }
}