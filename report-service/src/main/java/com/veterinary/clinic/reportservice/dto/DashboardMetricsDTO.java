package com.veterinary.clinic.reportservice.dto;

import java.time.LocalDate;
import java.util.Map;

public class DashboardMetricsDTO {

    private Long totalPatients;
    private Long totalAppointments;
    private Long totalPrescriptions;
    private Long totalUsers;

    private Long todayAppointments;
    private Long thisWeekAppointments;
    private Long thisMonthAppointments;

    private Long activePatients;
    private Long newPatientsThisMonth;

    private Map<String, Long> appointmentsByStatus;
    private Map<String, Long> patientsBySpecies;
    private Map<LocalDate, Long> appointmentsTrend;
    private Map<String, Long> prescriptionsByVeterinarian;

    // Constructors
    public DashboardMetricsDTO() {}

    // Getters and Setters
    public Long getTotalPatients() { return totalPatients; }
    public void setTotalPatients(Long totalPatients) { this.totalPatients = totalPatients; }

    public Long getTotalAppointments() { return totalAppointments; }
    public void setTotalAppointments(Long totalAppointments) { this.totalAppointments = totalAppointments; }

    public Long getTotalPrescriptions() { return totalPrescriptions; }
    public void setTotalPrescriptions(Long totalPrescriptions) { this.totalPrescriptions = totalPrescriptions; }

    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }

    public Long getTodayAppointments() { return todayAppointments; }
    public void setTodayAppointments(Long todayAppointments) { this.todayAppointments = todayAppointments; }

    public Long getThisWeekAppointments() { return thisWeekAppointments; }
    public void setThisWeekAppointments(Long thisWeekAppointments) { this.thisWeekAppointments = thisWeekAppointments; }

    public Long getThisMonthAppointments() { return thisMonthAppointments; }
    public void setThisMonthAppointments(Long thisMonthAppointments) { this.thisMonthAppointments = thisMonthAppointments; }

    public Long getActivePatients() { return activePatients; }
    public void setActivePatients(Long activePatients) { this.activePatients = activePatients; }

    public Long getNewPatientsThisMonth() { return newPatientsThisMonth; }
    public void setNewPatientsThisMonth(Long newPatientsThisMonth) { this.newPatientsThisMonth = newPatientsThisMonth; }

    public Map<String, Long> getAppointmentsByStatus() { return appointmentsByStatus; }
    public void setAppointmentsByStatus(Map<String, Long> appointmentsByStatus) { this.appointmentsByStatus = appointmentsByStatus; }

    public Map<String, Long> getPatientsBySpecies() { return patientsBySpecies; }
    public void setPatientsBySpecies(Map<String, Long> patientsBySpecies) { this.patientsBySpecies = patientsBySpecies; }

    public Map<LocalDate, Long> getAppointmentsTrend() { return appointmentsTrend; }
    public void setAppointmentsTrend(Map<LocalDate, Long> appointmentsTrend) { this.appointmentsTrend = appointmentsTrend; }

    public Map<String, Long> getPrescriptionsByVeterinarian() { return prescriptionsByVeterinarian; }
    public void setPrescriptionsByVeterinarian(Map<String, Long> prescriptionsByVeterinarian) { this.prescriptionsByVeterinarian = prescriptionsByVeterinarian; }
}