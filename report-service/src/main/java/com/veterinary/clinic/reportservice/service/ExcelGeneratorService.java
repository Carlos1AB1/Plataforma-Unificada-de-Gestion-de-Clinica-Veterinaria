package com.veterinary.clinic.reportservice.service;

import com.veterinary.clinic.reportservice.dto.AppointmentReportDTO;
import com.veterinary.clinic.reportservice.dto.PatientReportDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelGeneratorService {

    public byte[] generateAppointmentReportExcel(List<AppointmentReportDTO> appointments) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte de Citas");

        // Crear estilo para headers
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Crear headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "ID Cita", "Paciente", "Especie", "Propietario", "Veterinario",
                "Fecha", "Estado", "Tipo", "Observaciones"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Llenar datos
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        int rowNum = 1;

        for (AppointmentReportDTO appointment : appointments) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(appointment.getAppointmentId());
            row.createCell(1).setCellValue(appointment.getPatientName());
            row.createCell(2).setCellValue(appointment.getPatientSpecies());
            row.createCell(3).setCellValue(appointment.getOwnerName());
            row.createCell(4).setCellValue(appointment.getVeterinarianName());
            row.createCell(5).setCellValue(appointment.getAppointmentDate().format(formatter));
            row.createCell(6).setCellValue(appointment.getStatus());
            row.createCell(7).setCellValue(appointment.getAppointmentType());
            row.createCell(8).setCellValue(appointment.getObservations());
        }

        // Auto-ajustar columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    public byte[] generatePatientReportExcel(List<PatientReportDTO> patients) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte de Pacientes");

        // Crear estilo para headers
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Crear headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "ID", "Paciente", "Especie", "Raza", "Edad", "Peso",
                "Propietario", "Teléfono", "Email", "Registro", "Total Citas", "Última Visita"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Llenar datos
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int rowNum = 1;

        for (PatientReportDTO patient : patients) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(patient.getPatientId());
            row.createCell(1).setCellValue(patient.getPatientName());
            row.createCell(2).setCellValue(patient.getSpecies());
            row.createCell(3).setCellValue(patient.getBreed());
            row.createCell(4).setCellValue(patient.getAge() != null ? patient.getAge() : 0);
            row.createCell(5).setCellValue(patient.getWeight() != null ? patient.getWeight() : 0.0);
            row.createCell(6).setCellValue(patient.getOwnerName());
            row.createCell(7).setCellValue(patient.getOwnerPhone());
            row.createCell(8).setCellValue(patient.getOwnerEmail());
            row.createCell(9).setCellValue(patient.getRegistrationDate() != null ?
                    patient.getRegistrationDate().format(formatter) : "");
            row.createCell(10).setCellValue(patient.getTotalAppointments() != null ?
                    patient.getTotalAppointments() : 0);
            row.createCell(11).setCellValue(patient.getLastVisit() != null ?
                    patient.getLastVisit().format(formatter) : "");
        }

        // Auto-ajustar columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}