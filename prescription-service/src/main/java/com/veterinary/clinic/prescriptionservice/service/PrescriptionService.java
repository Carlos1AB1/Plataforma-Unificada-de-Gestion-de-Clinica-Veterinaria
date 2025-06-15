package com.veterinary.clinic.prescriptionservice.service;

import com.veterinary.clinic.prescriptionservice.dto.*;
import com.veterinary.clinic.prescriptionservice.entity.Medication;
import com.veterinary.clinic.prescriptionservice.entity.Prescription;
import com.veterinary.clinic.prescriptionservice.repository.PrescriptionRepository;
import com.veterinary.clinic.prescriptionservice.client.PatientClient;
import com.veterinary.clinic.prescriptionservice.client.UserClient;
import com.veterinary.clinic.prescriptionservice.client.MedicalHistoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PatientClient patientClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private MedicalHistoryClient medicalHistoryClient;

    public PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO requestDTO) {
        // Validar que la historia médica existe
        try {
            medicalHistoryClient.getMedicalHistoryById(requestDTO.getMedicalHistoryId());
        } catch (Exception e) {
            throw new RuntimeException("Medical history not found with ID: " + requestDTO.getMedicalHistoryId());
        }

        // Validar que el paciente existe
        PatientClient.PatientResponseDTO patient;
        try {
            patient = patientClient.getPatientById(requestDTO.getPatientId());
        } catch (Exception e) {
            throw new RuntimeException("Patient not found with ID: " + requestDTO.getPatientId());
        }

        // Validar que el veterinario existe
        UserClient.UserResponseDTO veterinarian;
        try {
            veterinarian = userClient.getUserById(requestDTO.getVeterinarianId());
            if (!"VETERINARIO".equals(veterinarian.getRole())) {
                throw new RuntimeException("User is not a veterinarian");
            }
        } catch (Exception e) {
            throw new RuntimeException("Veterinarian not found with ID: " + requestDTO.getVeterinarianId());
        }

        // Crear la prescripción
        Prescription prescription = new Prescription();
        prescription.setMedicalHistoryId(requestDTO.getMedicalHistoryId());
        prescription.setPatientId(requestDTO.getPatientId());
        prescription.setVeterinarianId(requestDTO.getVeterinarianId());
        prescription.setPrescriptionDate(requestDTO.getPrescriptionDate());
        prescription.setObservations(requestDTO.getObservations());

        // Crear los medicamentos
        List<Medication> medications = requestDTO.getMedications().stream()
                .map(medicationDTO -> new Medication(
                        prescription,
                        medicationDTO.getMedicationName(),
                        medicationDTO.getDosage(),
                        medicationDTO.getFrequency(),
                        medicationDTO.getDuration(),
                        medicationDTO.getInstructions()
                ))
                .collect(Collectors.toList());

        prescription.setMedications(medications);

        Prescription savedPrescription = prescriptionRepository.save(prescription);

        return convertToResponseDTO(savedPrescription, patient, veterinarian);
    }

    @Transactional(readOnly = true)
    public PrescriptionResponseDTO getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with ID: " + id));

        PatientClient.PatientResponseDTO patient = patientClient.getPatientById(prescription.getPatientId());
        UserClient.UserResponseDTO veterinarian = userClient.getUserById(prescription.getVeterinarianId());

        return convertToResponseDTO(prescription, patient, veterinarian);
    }

    @Transactional(readOnly = true)
    public List<PrescriptionResponseDTO> getPrescriptionsByPatient(Long patientId) {
        List<Prescription> prescriptions = prescriptionRepository.findByPatientIdOrderByPrescriptionDateDesc(patientId);

        return prescriptions.stream()
                .map(this::convertToResponseDTOSimple)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PrescriptionResponseDTO> getPrescriptionsByVeterinarian(Long veterinarianId) {
        List<Prescription> prescriptions = prescriptionRepository.findByVeterinarianIdOrderByPrescriptionDateDesc(veterinarianId);

        return prescriptions.stream()
                .map(this::convertToResponseDTOSimple)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PrescriptionResponseDTO> getPrescriptionsByMedicalHistory(Long medicalHistoryId) {
        List<Prescription> prescriptions = prescriptionRepository.findByMedicalHistoryId(medicalHistoryId);

        return prescriptions.stream()
                .map(this::convertToResponseDTOSimple)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<PrescriptionResponseDTO> getAllPrescriptions(Pageable pageable) {
        Page<Prescription> prescriptions = prescriptionRepository.findAll(pageable);

        return prescriptions.map(this::convertToResponseDTOSimple);
    }

    @Transactional(readOnly = true)
    public List<PrescriptionResponseDTO> getPrescriptionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Prescription> prescriptions = prescriptionRepository.findByDateRange(startDate, endDate);

        return prescriptions.stream()
                .map(this::convertToResponseDTOSimple)
                .collect(Collectors.toList());
    }

    public PrescriptionResponseDTO updatePrescriptionStatus(Long id, Prescription.PrescriptionStatus status) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with ID: " + id));

        prescription.setStatus(status);
        Prescription updatedPrescription = prescriptionRepository.save(prescription);

        return convertToResponseDTOSimple(updatedPrescription);
    }

    public void deletePrescription(Long id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new RuntimeException("Prescription not found with ID: " + id);
        }
        prescriptionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Long getTodayPrescriptionsCountByVeterinarian(Long veterinarianId) {
        return prescriptionRepository.countTodayPrescriptionsByVeterinarian(veterinarianId);
    }

    private PrescriptionResponseDTO convertToResponseDTO(Prescription prescription,
                                                         PatientClient.PatientResponseDTO patient,
                                                         UserClient.UserResponseDTO veterinarian) {
        List<MedicationDTO> medicationDTOs = prescription.getMedications().stream()
                .map(medication -> new MedicationDTO(
                        medication.getId(),
                        medication.getMedicationName(),
                        medication.getDosage(),
                        medication.getFrequency(),
                        medication.getDuration(),
                        medication.getInstructions()
                ))
                .collect(Collectors.toList());

        return new PrescriptionResponseDTO(
                prescription.getId(),
                prescription.getMedicalHistoryId(),
                prescription.getPatientId(),
                prescription.getVeterinarianId(),
                patient.getName(),
                veterinarian.getFullName(),
                prescription.getPrescriptionDate(),
                prescription.getObservations(),
                prescription.getStatus().name(),
                medicationDTOs,
                prescription.getCreatedAt(),
                prescription.getUpdatedAt()
        );
    }

    private PrescriptionResponseDTO convertToResponseDTOSimple(Prescription prescription) {
        try {
            PatientClient.PatientResponseDTO patient = patientClient.getPatientById(prescription.getPatientId());
            UserClient.UserResponseDTO veterinarian = userClient.getUserById(prescription.getVeterinarianId());
            return convertToResponseDTO(prescription, patient, veterinarian);
        } catch (Exception e) {
            // En caso de error en la comunicación con otros servicios, devolver datos básicos
            List<MedicationDTO> medicationDTOs = prescription.getMedications().stream()
                    .map(medication -> new MedicationDTO(
                            medication.getId(),
                            medication.getMedicationName(),
                            medication.getDosage(),
                            medication.getFrequency(),
                            medication.getDuration(),
                            medication.getInstructions()
                    ))
                    .collect(Collectors.toList());

            return new PrescriptionResponseDTO(
                    prescription.getId(),
                    prescription.getMedicalHistoryId(),
                    prescription.getPatientId(),
                    prescription.getVeterinarianId(),
                    "Unknown Patient",
                    "Unknown Veterinarian",
                    prescription.getPrescriptionDate(),
                    prescription.getObservations(),
                    prescription.getStatus().name(),
                    medicationDTOs,
                    prescription.getCreatedAt(),
                    prescription.getUpdatedAt()
            );
        }
    }
}