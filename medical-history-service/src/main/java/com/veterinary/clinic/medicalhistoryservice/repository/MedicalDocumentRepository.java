package com.veterinary.clinic.medicalhistoryservice.repository;

import com.veterinary.clinic.medicalhistoryservice.entity.MedicalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalDocumentRepository extends JpaRepository<MedicalDocument, Long> {

    // Buscar documentos por historial médico
    List<MedicalDocument> findByMedicalHistoryIdOrderByUploadedAtDesc(Long medicalHistoryId);

    // Buscar documentos por tipo de archivo
    List<MedicalDocument> findByFileTypeIgnoreCaseOrderByUploadedAtDesc(String fileType);

    // Contar documentos por historial médico
    long countByMedicalHistoryId(Long medicalHistoryId);

    // Buscar por nombre de archivo
    List<MedicalDocument> findByFileNameContainingIgnoreCaseOrderByUploadedAtDesc(String fileName);
}