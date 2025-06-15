package com.veterinary.clinic.reportservice.service;

import com.veterinary.clinic.reportservice.dto.AppointmentReportDTO;
import com.veterinary.clinic.reportservice.dto.PatientReportDTO;
import com.veterinary.clinic.reportservice.dto.FinancialReportDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class PdfReportService {

    public byte[] generateAppointmentReportPdf(List<AppointmentReportDTO> appointments,
                                               String startDate, String endDate) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4.rotate()); // Horizontal para más columnas
            PdfWriter.getInstance(document, baos);
            document.open();

            // Título
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("REPORTE DE CITAS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Período
            if (startDate != null && endDate != null) {
                Paragraph period = new Paragraph("Período: " + startDate + " - " + endDate);
                period.setAlignment(Element.ALIGN_CENTER);
                document.add(period);
            }

            document.add(new Paragraph("\n"));

            // Información general
            document.add(new Paragraph("Total de citas: " + appointments.size()));
            document.add(new Paragraph("Fecha de generación: " +
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(java.time.LocalDateTime.now())));

            document.add(new Paragraph("\n"));

            // Tabla de citas
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 2, 1.5f, 2, 2, 2, 1.5f, 2});

            // Headers
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            addHeaderCell(table, "ID", headerFont);
            addHeaderCell(table, "Paciente", headerFont);
            addHeaderCell(table, "Especie", headerFont);
            addHeaderCell(table, "Propietario", headerFont);
            addHeaderCell(table, "Veterinario", headerFont);
            addHeaderCell(table, "Fecha", headerFont);
            addHeaderCell(table, "Estado", headerFont);
            addHeaderCell(table, "Tipo", headerFont);

            // Datos
            Font dataFont = new Font(Font.FontFamily.HELVETICA, 8);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (AppointmentReportDTO appointment : appointments) {
                table.addCell(new Phrase(appointment.getAppointmentId().toString(), dataFont));
                table.addCell(new Phrase(appointment.getPatientName(), dataFont));
                table.addCell(new Phrase(appointment.getPatientSpecies(), dataFont));
                table.addCell(new Phrase(appointment.getOwnerName(), dataFont));
                table.addCell(new Phrase(appointment.getVeterinarianName(), dataFont));
                table.addCell(new Phrase(appointment.getAppointmentDate().format(formatter), dataFont));
                table.addCell(new Phrase(appointment.getStatus(), dataFont));
                table.addCell(new Phrase(appointment.getAppointmentType(), dataFont));
            }

            document.add(table);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating appointment PDF report: " + e.getMessage(), e);
        }
    }

    public byte[] generatePatientReportPdf(List<PatientReportDTO> patients) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, baos);
            document.open();

            // Título
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("REPORTE DE PACIENTES", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            // Información general
            document.add(new Paragraph("Total de pacientes: " + patients.size()));
            document.add(new Paragraph("Fecha de generación: " +
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(java.time.LocalDateTime.now())));

            document.add(new Paragraph("\n"));

            // Tabla de pacientes
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 2, 1.5f, 1.5f, 1, 2, 1.5f});

            // Headers
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            addHeaderCell(table, "ID", headerFont);
            addHeaderCell(table, "Paciente", headerFont);
            addHeaderCell(table, "Especie", headerFont);
            addHeaderCell(table, "Raza", headerFont);
            addHeaderCell(table, "Edad", headerFont);
            addHeaderCell(table, "Propietario", headerFont);
            addHeaderCell(table, "Total Citas", headerFont);

            // Datos
            Font dataFont = new Font(Font.FontFamily.HELVETICA, 8);

            for (PatientReportDTO patient : patients) {
                table.addCell(new Phrase(patient.getPatientId().toString(), dataFont));
                table.addCell(new Phrase(patient.getPatientName(), dataFont));
                table.addCell(new Phrase(patient.getSpecies(), dataFont));
                table.addCell(new Phrase(patient.getBreed(), dataFont));
                table.addCell(new Phrase(patient.getAge() != null ? patient.getAge().toString() : "N/A", dataFont));
                table.addCell(new Phrase(patient.getOwnerName(), dataFont));
                table.addCell(new Phrase(patient.getTotalAppointments() != null ? patient.getTotalAppointments().toString() : "0", dataFont));
            }

            document.add(table);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating patient PDF report: " + e.getMessage(), e);
        }
    }

    public byte[] generateFinancialReportPdf(FinancialReportDTO report) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Título
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("REPORTE FINANCIERO", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            // Período
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            document.add(new Paragraph("Período: " + report.getStartDate().format(formatter) +
                    " - " + report.getEndDate().format(formatter)));
            document.add(new Paragraph("Fecha de generación: " +
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(java.time.LocalDateTime.now())));

            document.add(new Paragraph("\n"));

            // Resumen financiero
            Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            document.add(new Paragraph("RESUMEN FINANCIERO", sectionFont));

            document.add(new Paragraph("Total de citas: " + report.getTotalAppointments()));
            document.add(new Paragraph("Ingresos totales: $" + report.getTotalRevenue()));
            document.add(new Paragraph("Valor promedio por cita: $" + report.getAverageAppointmentValue()));

            document.add(new Paragraph("\n"));

            // Citas por tipo
            if (report.getAppointmentsByType() != null && !report.getAppointmentsByType().isEmpty()) {
                document.add(new Paragraph("CITAS POR TIPO", sectionFont));

                PdfPTable typeTable = new PdfPTable(2);
                typeTable.setWidthPercentage(70);
                typeTable.setWidths(new float[]{2, 1});

                Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                addHeaderCell(typeTable, "Tipo de Cita", headerFont);
                addHeaderCell(typeTable, "Cantidad", headerFont);

                for (Map.Entry<String, Long> entry : report.getAppointmentsByType().entrySet()) {
                    typeTable.addCell(new Phrase(entry.getKey()));
                    typeTable.addCell(new Phrase(entry.getValue().toString()));
                }

                document.add(typeTable);
                document.add(new Paragraph("\n"));
            }

            // Ingresos por veterinario
            if (report.getRevenueByVeterinarian() != null && !report.getRevenueByVeterinarian().isEmpty()) {
                document.add(new Paragraph("INGRESOS POR VETERINARIO", sectionFont));

                PdfPTable vetTable = new PdfPTable(2);
                vetTable.setWidthPercentage(70);
                vetTable.setWidths(new float[]{2, 1});

                Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                addHeaderCell(vetTable, "Veterinario", headerFont);
                addHeaderCell(vetTable, "Ingresos", headerFont);

                for (Map.Entry<String, BigDecimal> entry : report.getRevenueByVeterinarian().entrySet()) {
                    vetTable.addCell(new Phrase(entry.getKey()));
                    vetTable.addCell(new Phrase("$" + entry.getValue()));
                }

                document.add(vetTable);
            }

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating financial PDF report: " + e.getMessage(), e);
        }
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell headerCell = new PdfPCell(new Phrase(text, font));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        headerCell.setPadding(5);
        table.addCell(headerCell);
    }
}
