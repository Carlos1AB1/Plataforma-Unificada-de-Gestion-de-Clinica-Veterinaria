package com.veterinary.clinic.prescriptionservice.service;


import com.veterinary.clinic.prescriptionservice.dto.PrescriptionResponseDTO;
import com.veterinary.clinic.prescriptionservice.dto.MedicationDTO;
import com.veterinary.clinic.prescriptionservice.client.PatientClient;
import com.veterinary.clinic.prescriptionservice.client.UserClient;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGeneratorService {

    @Autowired
    private PatientClient patientClient;

    @Autowired
    private UserClient userClient;

    public byte[] generatePrescriptionPdf(PrescriptionResponseDTO prescription) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Obtener datos adicionales
            PatientClient.PatientResponseDTO patient = patientClient.getPatientById(prescription.getPatientId());
            UserClient.UserResponseDTO veterinarian = userClient.getUserById(prescription.getVeterinarianId());

            // Título
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("RECETA MÉDICA VETERINARIA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            // Información de la clínica
            Font clinicFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph clinicName = new Paragraph("CLÍNICA VETERINARIA", clinicFont);
            clinicName.setAlignment(Element.ALIGN_CENTER);
            document.add(clinicName);

            Paragraph address = new Paragraph("Dirección: Calle Principal 123");
            address.setAlignment(Element.ALIGN_CENTER);
            document.add(address);

            Paragraph phone = new Paragraph("Teléfono: +1 234 567 8900");
            phone.setAlignment(Element.ALIGN_CENTER);
            document.add(phone);

            document.add(new Paragraph("\n"));

            // Información del veterinario
            PdfPTable veterinarianTable = new PdfPTable(2);
            veterinarianTable.setWidthPercentage(100);
            veterinarianTable.setWidths(new float[]{1, 2});

            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            PdfPCell cell1 = new PdfPCell(new Phrase("Veterinario:", boldFont));
            cell1.setBorder(Rectangle.NO_BORDER);
            veterinarianTable.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Phrase(veterinarian.getFullName()));
            cell2.setBorder(Rectangle.NO_BORDER);
            veterinarianTable.addCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Phrase("Email:", boldFont));
            cell3.setBorder(Rectangle.NO_BORDER);
            veterinarianTable.addCell(cell3);

            PdfPCell cell4 = new PdfPCell(new Phrase(veterinarian.getEmail()));
            cell4.setBorder(Rectangle.NO_BORDER);
            veterinarianTable.addCell(cell4);

            PdfPCell cell5 = new PdfPCell(new Phrase("Fecha:", boldFont));
            cell5.setBorder(Rectangle.NO_BORDER);
            veterinarianTable.addCell(cell5);

            PdfPCell cell6 = new PdfPCell(new Phrase(
                    prescription.getPrescriptionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            cell6.setBorder(Rectangle.NO_BORDER);
            veterinarianTable.addCell(cell6);

            document.add(veterinarianTable);
            document.add(new Paragraph("\n"));

            // Información del paciente
            Paragraph patientTitle = new Paragraph("DATOS DEL PACIENTE", boldFont);
            document.add(patientTitle);

            PdfPTable patientTable = new PdfPTable(2);
            patientTable.setWidthPercentage(100);
            patientTable.setWidths(new float[]{1, 2});

            // Datos del paciente
            addTableRow(patientTable, "Nombre:", patient.getName(), boldFont);
            addTableRow(patientTable, "Especie:", patient.getSpecies(), boldFont);
            addTableRow(patientTable, "Raza:", patient.getBreed(), boldFont);
            addTableRow(patientTable, "Edad:", patient.getAge() + " años", boldFont);
            addTableRow(patientTable, "Peso:", patient.getWeight() + " kg", boldFont);

            document.add(patientTable);
            document.add(new Paragraph("\n"));

            // Prescripción
            Paragraph prescriptionTitle = new Paragraph("PRESCRIPCIÓN MÉDICA", boldFont);
            document.add(prescriptionTitle);

            if (prescription.getObservations() != null && !prescription.getObservations().isEmpty()) {
                document.add(new Paragraph("Observaciones: " + prescription.getObservations()));
                document.add(new Paragraph("\n"));
            }

            // Medicamentos
            Paragraph medicationTitle = new Paragraph("MEDICAMENTOS PRESCRITOS", boldFont);
            document.add(medicationTitle);

            PdfPTable medicationTable = new PdfPTable(5);
            medicationTable.setWidthPercentage(100);
            medicationTable.setWidths(new float[]{2, 1, 1, 1, 2});

            // Headers
            addHeaderCell(medicationTable, "Medicamento", boldFont);
            addHeaderCell(medicationTable, "Dosis", boldFont);
            addHeaderCell(medicationTable, "Frecuencia", boldFont);
            addHeaderCell(medicationTable, "Duración", boldFont);
            addHeaderCell(medicationTable, "Instrucciones", boldFont);

            // Datos de medicamentos
            for (MedicationDTO medication : prescription.getMedications()) {
                medicationTable.addCell(new Phrase(medication.getMedicationName()));
                medicationTable.addCell(new Phrase(medication.getDosage()));
                medicationTable.addCell(new Phrase(medication.getFrequency()));
                medicationTable.addCell(new Phrase(medication.getDuration()));
                medicationTable.addCell(new Phrase(
                        medication.getInstructions() != null ? medication.getInstructions() : "-"));
            }

            document.add(medicationTable);

            // Pie de página
            document.add(new Paragraph("\n\n"));
            Paragraph signature = new Paragraph("_________________________________");
            signature.setAlignment(Element.ALIGN_CENTER);
            document.add(signature);

            Paragraph signatureText = new Paragraph("Firma del Veterinario");
            signatureText.setAlignment(Element.ALIGN_CENTER);
            document.add(signatureText);

            Font smallFont = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC);
            Paragraph validity = new Paragraph("Esta receta es válida por 30 días desde la fecha de emisión.", smallFont);
            validity.setAlignment(Element.ALIGN_CENTER);
            document.add(validity);

            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
        }
    }

    private void addTableRow(PdfPTable table, String label, String value, Font boldFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, boldFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value));
        valueCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(valueCell);
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell headerCell = new PdfPCell(new Phrase(text, font));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(headerCell);
    }
}
