package com.veterinary.clinic.reportservice.controller;


import com.veterinary.clinic.reportservice.dto.*;
import com.veterinary.clinic.reportservice.entity.ReportHistory;
import com.veterinary.clinic.reportservice.service.ReportService;
import com.veterinary.clinic.reportservice.service.ExcelGeneratorService;
import com.veterinary.clinic.reportservice.service.PdfReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Report Management", description = "Operations related to report generation and statistics")
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ExcelGeneratorService excelGeneratorService;

    @Autowired
    private PdfReportService pdfReportService;

    @GetMapping("/dashboard")
    @Operation(summary = "Get dashboard metrics", description = "Retrieve dashboard metrics and statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<DashboardMetricsDTO> getDashboardMetrics() {
        DashboardMetricsDTO metrics = reportService.getDashboardMetrics();
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/appointments")
    @Operation(summary = "Get appointment report", description = "Generate appointment report for specified date range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<AppointmentReportDTO>> getAppointmentReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {

        Long userId = getUserIdFromAuthentication(authentication);
        List<AppointmentReportDTO> report = reportService.getAppointmentReport(startDate, endDate, userId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/appointments/excel")
    @Operation(summary = "Export appointment report to Excel", description = "Generate and download appointment report in Excel format")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<byte[]> exportAppointmentReportExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) throws IOException {

        Long userId = getUserIdFromAuthentication(authentication);
        List<AppointmentReportDTO> report = reportService.getAppointmentReport(startDate, endDate, userId);
        byte[] excelBytes = excelGeneratorService.generateAppointmentReportExcel(report);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "reporte_citas_" +
                startDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "_" +
                endDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".xlsx");
        headers.setContentLength(excelBytes.length);

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/appointments/pdf")
    @Operation(summary = "Export appointment report to PDF", description = "Generate and download appointment report in PDF format")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<byte[]> exportAppointmentReportPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {

        Long userId = getUserIdFromAuthentication(authentication);
        List<AppointmentReportDTO> report = reportService.getAppointmentReport(startDate, endDate, userId);

        String startDateStr = startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String endDateStr = endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        byte[] pdfBytes = pdfReportService.generateAppointmentReportPdf(report, startDateStr, endDateStr);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_citas_" +
                startDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "_" +
                endDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".pdf");
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/patients")
    @Operation(summary = "Get patient report", description = "Generate comprehensive patient report")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<PatientReportDTO>> getPatientReport(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<PatientReportDTO> report = reportService.getPatientReport(userId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/patients/excel")
    @Operation(summary = "Export patient report to Excel", description = "Generate and download patient report in Excel format")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<byte[]> exportPatientReportExcel(Authentication authentication) throws IOException {
        Long userId = getUserIdFromAuthentication(authentication);
        List<PatientReportDTO> report = reportService.getPatientReport(userId);
        byte[] excelBytes = excelGeneratorService.generatePatientReportExcel(report);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "reporte_pacientes_" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".xlsx");
        headers.setContentLength(excelBytes.length);

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/patients/pdf")
    @Operation(summary = "Export patient report to PDF", description = "Generate and download patient report in PDF format")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<byte[]> exportPatientReportPdf(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<PatientReportDTO> report = reportService.getPatientReport(userId);
        byte[] pdfBytes = pdfReportService.generatePatientReportPdf(report);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_pacientes_" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".pdf");
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/financial")
    @Operation(summary = "Get financial report", description = "Generate financial report for specified date range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FinancialReportDTO> getFinancialReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {

        Long userId = getUserIdFromAuthentication(authentication);
        FinancialReportDTO report = reportService.getFinancialReport(startDate, endDate, userId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/financial/pdf")
    @Operation(summary = "Export financial report to PDF", description = "Generate and download financial report in PDF format")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportFinancialReportPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {

        Long userId = getUserIdFromAuthentication(authentication);
        FinancialReportDTO report = reportService.getFinancialReport(startDate, endDate, userId);
        byte[] pdfBytes = pdfReportService.generateFinancialReportPdf(report);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_financiero_" +
                startDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "_" +
                endDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".pdf");
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/history")
    @Operation(summary = "Get report history", description = "Get history of generated reports for current user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<ReportHistory>> getReportHistory(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<ReportHistory> history = reportService.getReportHistory(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get report statistics", description = "Get statistics about report generation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getReportStatistics() {
        Map<String, Long> statistics = reportService.getReportStatistics();
        return ResponseEntity.ok(statistics);
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        // En un escenario real, obtendrías el ID del usuario del JWT o de la base de datos
        // Por simplicidad, retornamos 1L (asumiendo que es un admin)
        return 1L;
    }
}
