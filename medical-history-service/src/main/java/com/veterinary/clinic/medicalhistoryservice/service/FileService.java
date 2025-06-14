package com.veterinary.clinic.medicalhistoryservice.service;

import com.veterinary.clinic.medicalhistoryservice.dto.MedicalDocumentDto;
import com.veterinary.clinic.medicalhistoryservice.entity.MedicalDocument;
import com.veterinary.clinic.medicalhistoryservice.entity.MedicalHistory;
import com.veterinary.clinic.medicalhistoryservice.exception.FileStorageException;
import com.veterinary.clinic.medicalhistoryservice.exception.ResourceNotFoundException;
import com.veterinary.clinic.medicalhistoryservice.repository.MedicalDocumentRepository;
import com.veterinary.clinic.medicalhistoryservice.repository.MedicalHistoryRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileService {

    private final Path fileStorageLocation;
    private final List<String> allowedFileTypes = Arrays.asList("pdf", "jpg", "jpeg", "png");
    private final long maxFileSize = 10 * 1024 * 1024; // 10MB

    @Autowired
    private MedicalDocumentRepository documentRepository;

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    public FileService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public MedicalDocumentDto uploadFile(Long medicalHistoryId, MultipartFile file) {
        // Validar el historial médico
        MedicalHistory medicalHistory = medicalHistoryRepository.findById(medicalHistoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical history not found with id: " + medicalHistoryId));

        // Validar el archivo
        validateFile(file);

        // Generar nombre único para el archivo
        String originalFileName = file.getOriginalFilename();
        String fileExtension = FilenameUtils.getExtension(originalFileName);
        String fileName = UUID.randomUUID().toString() + "." + fileExtension;

        try {
            // Guardar el archivo
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Crear registro en la base de datos
            MedicalDocument document = new MedicalDocument();
            document.setFileName(originalFileName);
            document.setFilePath(fileName);
            document.setFileType(fileExtension.toLowerCase());
            document.setFileSize(file.getSize());
            document.setMedicalHistory(medicalHistory);

            MedicalDocument savedDocument = documentRepository.save(document);
            return convertToDto(savedDocument);

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    @Transactional(readOnly = true)
    public Resource downloadFile(Long documentId) {
        MedicalDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));

        try {
            Path filePath = this.fileStorageLocation.resolve(document.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found " + document.getFileName());
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File not found " + document.getFileName(), ex);
        }
    }

    @Transactional(readOnly = true)
    public MedicalDocumentDto getDocumentById(Long documentId) {
        MedicalDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));
        return convertToDto(document);
    }

    @Transactional(readOnly = true)
    public List<MedicalDocumentDto> getDocumentsByMedicalHistoryId(Long medicalHistoryId) {
        List<MedicalDocument> documents = documentRepository.findByMedicalHistoryIdOrderByUploadedAtDesc(medicalHistoryId);
        return documents.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void deleteDocument(Long documentId) {
        MedicalDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));

        try {
            // Eliminar archivo físico
            Path filePath = this.fileStorageLocation.resolve(document.getFilePath()).normalize();
            Files.deleteIfExists(filePath);

            // Eliminar registro de la base de datos
            documentRepository.delete(document);

        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file " + document.getFileName(), ex);
        }
    }

    public void deleteFilesByMedicalHistoryId(Long medicalHistoryId) {
        List<MedicalDocument> documents = documentRepository.findByMedicalHistoryIdOrderByUploadedAtDesc(medicalHistoryId);
        for (MedicalDocument document : documents) {
            deleteDocument(document.getId());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileStorageException("Failed to store empty file.");
        }

        if (file.getSize() > maxFileSize) {
            throw new FileStorageException("File size exceeds maximum allowed size of 10MB.");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.contains("..")) {
            throw new FileStorageException("Invalid file name: " + fileName);
        }

        String fileExtension = FilenameUtils.getExtension(fileName).toLowerCase();
        if (!allowedFileTypes.contains(fileExtension)) {
            throw new FileStorageException("File type not allowed. Allowed types: " + String.join(", ", allowedFileTypes));
        }
    }

    private MedicalDocumentDto convertToDto(MedicalDocument document) {
        MedicalDocumentDto dto = new MedicalDocumentDto();
        dto.setId(document.getId());
        dto.setFileName(document.getFileName());
        dto.setFileType(document.getFileType());
        dto.setFileSize(document.getFileSize());
        dto.setDownloadUrl("/api/medical-history/documents/" + document.getId() + "/download");
        dto.setUploadedAt(document.getUploadedAt());
        return dto;
    }
}