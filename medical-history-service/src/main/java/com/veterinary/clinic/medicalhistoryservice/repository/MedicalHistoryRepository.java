package com.veterinary.clinic.medicalhistoryservice.repository;

import com.veterinary.clinic.medicalhistoryservice.entity.MedicalHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {

    // Buscar historial médico por ID de paciente
    List<MedicalHistory> findByPatientIdOrderByConsultationDateDesc(Long patientId);

    // Buscar historial médico por ID de paciente con paginación
    Page<MedicalHistory> findByPatientIdOrderByConsultationDateDesc(Long patientId, Pageable pageable);

    // Buscar historial médico por veterinario
    List<MedicalHistory> findByVeterinarianIdOrderByConsultationDateDesc(Long veterinarianId);

    // Buscar historial médico por veterinario con paginación
    Page<MedicalHistory> findByVeterinarianIdOrderByConsultationDateDesc(Long veterinarianId, Pageable pageable);

    // Buscar historial médico por rango de fechas
    @Query("SELECT mh FROM MedicalHistory mh WHERE mh.consultationDate BETWEEN :startDate AND :endDate ORDER BY mh.consultationDate DESC")
    List<MedicalHistory> findByConsultationDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Buscar historial médico por paciente y rango de fechas
    @Query("SELECT mh FROM MedicalHistory mh WHERE mh.patientId = :patientId AND mh.consultationDate BETWEEN :startDate AND :endDate ORDER BY mh.consultationDate DESC")
    List<MedicalHistory> findByPatientIdAndConsultationDateBetween(@Param("patientId") Long patientId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Buscar por diagnóstico (búsqueda parcial)
    @Query("SELECT mh FROM MedicalHistory mh WHERE LOWER(mh.diagnosis) LIKE LOWER(CONCAT('%', :diagnosis, '%')) ORDER BY mh.consultationDate DESC")
    List<MedicalHistory> findByDiagnosisContainingIgnoreCase(@Param("diagnosis") String diagnosis);

    // Contar historiales por paciente
    long countByPatientId(Long patientId);

    // Obtener el último historial de un paciente
    MedicalHistory findFirstByPatientIdOrderByConsultationDateDesc(Long patientId);
}