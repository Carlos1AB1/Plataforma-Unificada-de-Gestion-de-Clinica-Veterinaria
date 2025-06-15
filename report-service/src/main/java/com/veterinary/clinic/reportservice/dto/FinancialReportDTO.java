package com.veterinary.clinic.reportservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class FinancialReportDTO {

    private LocalDate reportDate;
    private LocalDate startDate;
    private LocalDate endDate;

    private Long totalAppointments;
    private BigDecimal totalRevenue;
    private BigDecimal averageAppointmentValue;

    private Map<String, Long> appointmentsByType;
    private Map<String, BigDecimal> revenueByVeterinarian;
    private Map<LocalDate, BigDecimal> dailyRevenue;
    private Map<String, Long> serviceUtilization;

    // Constructors
    public FinancialReportDTO() {}

    public FinancialReportDTO(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.reportDate = LocalDate.now();
    }

    // Getters and Setters
    public LocalDate getReportDate() { return reportDate; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Long getTotalAppointments() { return totalAppointments; }
    public void setTotalAppointments(Long totalAppointments) { this.totalAppointments = totalAppointments; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public BigDecimal getAverageAppointmentValue() { return averageAppointmentValue; }
    public void setAverageAppointmentValue(BigDecimal averageAppointmentValue) { this.averageAppointmentValue = averageAppointmentValue; }

    public Map<String, Long> getAppointmentsByType() { return appointmentsByType; }
    public void setAppointmentsByType(Map<String, Long> appointmentsByType) { this.appointmentsByType = appointmentsByType; }

    public Map<String, BigDecimal> getRevenueByVeterinarian() { return revenueByVeterinarian; }
    public void setRevenueByVeterinarian(Map<String, BigDecimal> revenueByVeterinarian) { this.revenueByVeterinarian = revenueByVeterinarian; }

    public Map<LocalDate, BigDecimal> getDailyRevenue() { return dailyRevenue; }
    public void setDailyRevenue(Map<LocalDate, BigDecimal> dailyRevenue) { this.dailyRevenue = dailyRevenue; }

    public Map<String, Long> getServiceUtilization() { return serviceUtilization; }
    public void setServiceUtilization(Map<String, Long> serviceUtilization) { this.serviceUtilization = serviceUtilization; }
}