package com.veterinary.clinic.reportservice.repository;

import com.veterinary.clinic.reportservice.entity.ReportHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportHistoryRepository extends JpaRepository<ReportHistory, Long> {

    List<ReportHistory> findByGeneratedByUserIdOrderByGeneratedAtDesc(Long userId);

    List<ReportHistory> findByReportTypeOrderByGeneratedAtDesc(String reportType);

    Page<ReportHistory> findByGeneratedByUserId(Long userId, Pageable pageable);

    @Query("SELECT r FROM ReportHistory r WHERE r.generatedAt BETWEEN :startDate AND :endDate ORDER BY r.generatedAt DESC")
    List<ReportHistory> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(r) FROM ReportHistory r WHERE r.generatedByUserId = :userId AND DATE(r.generatedAt) = CURRENT_DATE")
    Long countTodayReportsByUser(@Param("userId") Long userId);

    @Query("SELECT r.reportType, COUNT(r) FROM ReportHistory r GROUP BY r.reportType")
    List<Object[]> getReportStatistics();
}