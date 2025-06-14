package com.veterinary.clinic.medicalhistoryservice.service;

import com.veterinary.clinic.medicalhistoryservice.client.PatientClient;
import com.veterinary.clinic.medicalhistoryservice.client.PatientDto;
import com.veterinary.clinic.medicalhistoryservice.client.UserClient;
import com.veterinary.clinic.medicalhistoryservice.client.UserDto;
import com.veterinary.clinic.medicalhistoryservice.dto.*;
import com.veterinary.clinic.medicalhistoryservice.entity.MedicalHistory;
import com.veterinary.clinic.medicalhistoryservice.exception.ResourceNotFoundException;
import com.veterinary.clinic.medicalhistoryservice.repository.MedicalHistoryRepository;
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
public class MedicalHistoryService {

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Autowired
    private PatientClient patientClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private FileService fileService;

    public MedicalHistoryResponseDto createMedicalHistory(MedicalHistoryCreateDto createDto) {
        // Validar que el paciente existe
        if (!patientClient.existsById(createDto.getPatientId())) {
            throw new ResourceNotFoundException("Patient not found with id: " + createDto.getPatientId());
        }

        // Validar que el veterinario existe
        if (!userClient.existsById(createDto.getVeterinarianId())) {
            throw new ResourceNotFoundException("Veterinarian not found with id: " + createDto.getVeterinarianId());
        }

        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setPatientId(createDto.getPatientId());
        medicalHistory.setVeterinarianId(createDto.getVeterinarianId());
        medicalHistory.setDiagnosis(createDto.getDiagnosis());
        medicalHistory.setTreatment(createDto.getTreatment());
        medicalHistory.setNotes(createDto.getNotes());
        medicalHistory.setConsultationDate(createDto.getConsultationDate());

        MedicalHistory savedHistory = medicalHistoryRepository.save(medicalHistory);
        return convertToResponseDto(savedHistory);
    }

    @Transactional(readOnly = true)
    public MedicalHistoryResponseDto getMedicalHistoryById(Long id) {
        MedicalHistory medicalHistory = medicalHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical history not found with id: " + id));
        return convertToResponseDto(medicalHistory);
    }

    @Transactional(readOnly = true)
    public List<MedicalHistoryResponseDto> getMedicalHistoryByPatientId(Long patientId) {
        List<MedicalHistory> histories = medicalHistoryRepository.findByPatientIdOrderByConsultationDateDesc(patientId);
        return histories.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<MedicalHistoryResponseDto> getMedicalHistoryByPatientId(Long patientId, Pageable pageable) {
        Page<MedicalHistory> histories = medicalHistoryRepository.findByPatientIdOrderByConsultationDateDesc(patientId, pageable);
        return histories.map(this::convertToResponseDto);
    }

    @Transactional(readOnly = true)
    public List<MedicalHistoryResponseDto> getMedicalHistoryByVeterinarianId(Long veterinarianId) {
        List<MedicalHistory> histories = medicalHistoryRepository.findByVeterinarianIdOrderByConsultationDateDesc(veterinarianId);
        return histories.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicalHistoryResponseDto> getMedicalHistoryByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<MedicalHistory> histories = medicalHistoryRepository.findByConsultationDateBetween(startDate, endDate);
        return histories.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicalHistoryResponseDto> getMedicalHistoryByPatientAndDateRange(Long patientId, LocalDateTime startDate, LocalDateTime endDate) {
        List<MedicalHistory> histories = medicalHistoryRepository.findByPatientIdAndConsultationDateBetween(patientId, startDate, endDate);
        return histories.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicalHistoryResponseDto> searchByDiagnosis(String diagnosis) {
        List<MedicalHistory> histories = medicalHistoryRepository.findByDiagnosisContainingIgnoreCase(diagnosis);
        return histories.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public MedicalHistoryResponseDto updateMedicalHistory(Long id, MedicalHistoryUpdateDto updateDto) {
        MedicalHistory medicalHistory = medicalHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical history not found with id: " + id));

        if (updateDto.getDiagnosis() != null) {
            medicalHistory.setDiagnosis(updateDto.getDiagnosis());
        }
        if (updateDto.getTreatment() != null) {
            medicalHistory.setTreatment(updateDto.getTreatment());
        }
        if (updateDto.getNotes() != null) {
            medicalHistory.setNotes(updateDto.getNotes());
        }
        if (updateDto.getConsultationDate() != null) {
            medicalHistory.setConsultationDate(updateDto.getConsultationDate());
        }

        MedicalHistory updatedHistory = medicalHistoryRepository.save(medicalHistory);
        return convertToResponseDto(updatedHistory);
    }

    public void deleteMedicalHistory(Long id) {
        MedicalHistory medicalHistory = medicalHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical history not found with id: " + id));

        // Eliminar archivos asociados
        fileService.deleteFilesByMedicalHistoryId(id);

        medicalHistoryRepository.delete(medicalHistory);
    }

    @Transactional(readOnly = true)
    public long countByPatientId(Long patientId) {
        return medicalHistoryRepository.countByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public MedicalHistoryResponseDto getLastMedicalHistoryByPatientId(Long patientId) {
        MedicalHistory medicalHistory = medicalHistoryRepository.findFirstByPatientIdOrderByConsultationDateDesc(patientId);
        if (medicalHistory == null) {
            throw new ResourceNotFoundException("No medical history found for patient with id: " + patientId);
        }
        return convertToResponseDto(medicalHistory);
    }

    private MedicalHistoryResponseDto convertToResponseDto(MedicalHistory medicalHistory) {
        MedicalHistoryResponseDto dto = new MedicalHistoryResponseDto();
        dto.setId(medicalHistory.getId());
        dto.setPatientId(medicalHistory.getPatientId());
        dto.setVeterinarianId(medicalHistory.getVeterinarianId());
        dto.setDiagnosis(medicalHistory.getDiagnosis());
        dto.setTreatment(medicalHistory.getTreatment());
        dto.setNotes(medicalHistory.getNotes());
        dto.setConsultationDate(medicalHistory.getConsultationDate());
        dto.setCreatedAt(medicalHistory.getCreatedAt());
        dto.setUpdatedAt(medicalHistory.getUpdatedAt());

        // Obtener información del paciente
        try {
            PatientDto patient = patientClient.getPatientById(medicalHistory.getPatientId());
            dto.setPatientName(patient.getName());
        } catch (Exception e) {
            dto.setPatientName("Unknown Patient");
        }

        // Obtener información del veterinario
        try {
            UserDto veterinarian = userClient.getUserById(medicalHistory.getVeterinarianId());
            dto.setVeterinarianName(veterinarian.getFullName());
        } catch (Exception e) {
            dto.setVeterinarianName("Unknown Veterinarian");
        }

        // Obtener documentos asociados
        List<MedicalDocumentDto> documents = fileService.getDocumentsByMedicalHistoryId(medicalHistory.getId());
        dto.setDocuments(documents);

        return dto;
    }
}