package com.veterinary.clinic.prescriptionservice.repository;

import com.veterinary.clinic.prescriptionservice.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByPatientIdOrderByPrescriptionDateDesc(Long patientId);

    List<Prescription> findByVeterinarianIdOrderByPrescriptionDateDesc(Long veterinarianId);

    List<Prescription> findByMedicalHistoryId(Long medicalHistoryId);

    Page<Prescription> findByPatientId(Long patientId, Pageable pageable);

    Page<Prescription> findByVeterinarianId(Long veterinarianId, Pageable pageable);

    @Query("SELECT p FROM Prescription p WHERE p.prescriptionDate BETWEEN :startDate AND :endDate ORDER BY p.prescriptionDate DESC")
    List<Prescription> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM Prescription p WHERE p.patientId = :patientId AND p.prescriptionDate BETWEEN :startDate AND :endDate ORDER BY p.prescriptionDate DESC")
    List<Prescription> findByPatientIdAndDateRange(@Param("patientId") Long patientId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(p) FROM Prescription p WHERE p.veterinarianId = :veterinarianId AND DATE(p.prescriptionDate) = CURRENT_DATE")
    Long countTodayPrescriptionsByVeterinarian(@Param("veterinarianId") Long veterinarianId);

    boolean existsByMedicalHistoryId(Long medicalHistoryId);
}
