package com.veterinary.appointmentservice.repository;

import com.veterinary.appointmentservice.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Búsqueda por paciente
    List<Appointment> findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(Long patientId);

    // Búsqueda por veterinario
    List<Appointment> findByVeterinarianIdOrderByAppointmentDateDescAppointmentTimeDesc(Long veterinarianId);

    // Búsqueda por fecha
    List<Appointment> findByAppointmentDateOrderByAppointmentTime(LocalDate appointmentDate);

    // Búsqueda por estado
    List<Appointment> findByStatusOrderByAppointmentDateDescAppointmentTimeDesc(Appointment.Status status);

    // Búsqueda por veterinario y fecha
    List<Appointment> findByVeterinarianIdAndAppointmentDateOrderByAppointmentTime(Long veterinarianId, LocalDate appointmentDate);

    // Validar disponibilidad del veterinario - VERSIÓN CORREGIDA
    @Query(value = "SELECT * FROM appointments a WHERE a.veterinarian_id = :veterinarianId " +
            "AND a.appointment_date = :appointmentDate " +
            "AND a.status NOT IN ('CANCELLED', 'NO_SHOW') " +
            "AND ((a.appointment_time < :endTime AND a.appointment_time >= :startTime) " +
            "OR (a.appointment_time < :endTime AND " +
            "ADDTIME(a.appointment_time, SEC_TO_TIME(a.duration_minutes * 60)) > :startTime))",
            nativeQuery = true)
    List<Appointment> findConflictingAppointments(
            @Param("veterinarianId") Long veterinarianId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    // Citas por rango de fechas
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate " +
            "ORDER BY a.appointmentDate, a.appointmentTime")
    List<Appointment> findByAppointmentDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Citas de hoy
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate = CURRENT_DATE " +
            "ORDER BY a.appointmentTime")
    List<Appointment> findTodaysAppointments();

    // Citas por veterinario y rango de fechas
    @Query("SELECT a FROM Appointment a WHERE a.veterinarianId = :veterinarianId " +
            "AND a.appointmentDate BETWEEN :startDate AND :endDate " +
            "ORDER BY a.appointmentDate, a.appointmentTime")
    List<Appointment> findByVeterinarianIdAndDateRange(
            @Param("veterinarianId") Long veterinarianId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Citas pendientes (futuras)
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate > CURRENT_DATE " +
            "OR (a.appointmentDate = CURRENT_DATE AND a.appointmentTime > CURRENT_TIME) " +
            "ORDER BY a.appointmentDate, a.appointmentTime")
    List<Appointment> findUpcomingAppointments();

    // Citas de esta semana
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate >= :startOfWeek " +
            "AND a.appointmentDate <= :endOfWeek " +
            "ORDER BY a.appointmentDate, a.appointmentTime")
    List<Appointment> findWeeklyAppointments(
            @Param("startOfWeek") LocalDate startOfWeek,
            @Param("endOfWeek") LocalDate endOfWeek
    );

    // Citas de este mes
    @Query("SELECT a FROM Appointment a WHERE YEAR(a.appointmentDate) = :year " +
            "AND MONTH(a.appointmentDate) = :month " +
            "ORDER BY a.appointmentDate, a.appointmentTime")
    List<Appointment> findMonthlyAppointments(
            @Param("year") int year,
            @Param("month") int month
    );

    // Estadísticas
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.status = :status")
    Long countByStatus(@Param("status") Appointment.Status status);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.appointmentDate = CURRENT_DATE")
    Long countTodaysAppointments();

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.veterinarianId = :veterinarianId " +
            "AND a.appointmentDate = CURRENT_DATE")
    Long countTodaysAppointmentsByVeterinarian(@Param("veterinarianId") Long veterinarianId);

    // Búsqueda de texto
    @Query("SELECT a FROM Appointment a WHERE LOWER(a.reason) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(a.notes) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY a.appointmentDate DESC, a.appointmentTime DESC")
    List<Appointment> searchAppointments(@Param("query") String query);
}