package com.veterinary.patient.service;

import com.veterinary.patient.client.ClientServiceClient;
import com.veterinary.patient.dto.*;
import com.veterinary.patient.entity.Patient;
import com.veterinary.patient.exception.PatientException;
import com.veterinary.patient.repository.PatientRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ClientServiceClient clientServiceClient;

    public PatientResponse createPatient(CreatePatientRequest request, String currentUser, String authHeader) {
        // Validar que el cliente existe
        try {
            var clientResponse = clientServiceClient.getClientById(request.getClientId(), authHeader);
            if (clientResponse.getBody() == null) {
                throw new PatientException("Client not found with id: " + request.getClientId());
            }
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new PatientException("Client not found with id: " + request.getClientId());
            }
            throw new PatientException("Error validating client: " + e.getMessage());
        }

        // Validar microchip único si se proporciona
        if (request.getMicrochipNumber() != null && !request.getMicrochipNumber().trim().isEmpty()) {
            if (patientRepository.existsByMicrochipNumber(request.getMicrochipNumber())) {
                throw new PatientException("Microchip number already exists!");
            }
        }

        // Crear nuevo paciente
        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setClientId(request.getClientId());
        patient.setSpecies(request.getSpecies());
        patient.setBreed(request.getBreed());
        patient.setColor(request.getColor());
        patient.setGender(request.getGender());
        patient.setBirthDate(request.getBirthDate());
        patient.setWeight(request.getWeight());
        patient.setMicrochipNumber(request.getMicrochipNumber());
        patient.setIsSterilized(request.getIsSterilized());
        patient.setIsVaccinated(request.getIsVaccinated());
        patient.setAllergies(request.getAllergies());
        patient.setMedicalConditions(request.getMedicalConditions());
        patient.setNotes(request.getNotes());
        patient.setIsActive(true);
        patient.setCreatedBy(currentUser);
        patient.setUpdatedBy(currentUser);

        Patient savedPatient = patientRepository.save(patient);
        PatientDTO patientDTO = new PatientDTO(savedPatient);

        // Intentar obtener nombre del cliente
        enrichWithClientInfo(patientDTO, authHeader);

        return new PatientResponse("Patient created successfully", patientDTO);
    }

    public PatientResponse getPatientById(Long id, String authHeader) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientException("Patient not found with id: " + id));

        PatientDTO patientDTO = new PatientDTO(patient);
        enrichWithClientInfo(patientDTO, authHeader);

        return new PatientResponse("Patient found", patientDTO);
    }

    public PatientResponse getPatientsByClientId(Long clientId, String authHeader) {
        // Verificar que el cliente existe
        try {
            clientServiceClient.getClientById(clientId, authHeader);
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new PatientException("Client not found with id: " + clientId);
            }
        }

        List<Patient> patients = patientRepository.findByClientIdAndIsActive(clientId, true);
        List<PatientDTO> patientDTOs = patients.stream()
                .map(PatientDTO::new)
                .collect(Collectors.toList());

        // Enriquecer con información del cliente
        patientDTOs.forEach(dto -> enrichWithClientInfo(dto, authHeader));

        return new PatientResponse("Patients found for client", patientDTOs);
    }

    public PatientResponse updatePatient(Long id, UpdatePatientRequest request, String currentUser, String authHeader) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientException("Patient not found with id: " + id));

        // Validar microchip único si se está cambiando
        if (request.getMicrochipNumber() != null &&
                !request.getMicrochipNumber().equals(patient.getMicrochipNumber())) {
            if (patientRepository.existsByMicrochipNumber(request.getMicrochipNumber())) {
                throw new PatientException("Microchip number already exists!");
            }
        }

        // Actualizar campos
        if (request.getName() != null) {
            patient.setName(request.getName());
        }
        if (request.getSpecies() != null) {
            patient.setSpecies(request.getSpecies());
        }
        if (request.getBreed() != null) {
            patient.setBreed(request.getBreed());
        }
        if (request.getColor() != null) {
            patient.setColor(request.getColor());
        }
        if (request.getGender() != null) {
            patient.setGender(request.getGender());
        }
        if (request.getBirthDate() != null) {
            patient.setBirthDate(request.getBirthDate());
        }
        if (request.getWeight() != null) {
            patient.setWeight(request.getWeight());
        }
        if (request.getMicrochipNumber() != null) {
            patient.setMicrochipNumber(request.getMicrochipNumber());
        }
        if (request.getIsSterilized() != null) {
            patient.setIsSterilized(request.getIsSterilized());
        }
        if (request.getIsVaccinated() != null) {
            patient.setIsVaccinated(request.getIsVaccinated());
        }
        if (request.getAllergies() != null) {
            patient.setAllergies(request.getAllergies());
        }
        if (request.getMedicalConditions() != null) {
            patient.setMedicalConditions(request.getMedicalConditions());
        }
        if (request.getNotes() != null) {
            patient.setNotes(request.getNotes());
        }

        patient.setUpdatedBy(currentUser);
        Patient updatedPatient = patientRepository.save(patient);
        PatientDTO patientDTO = new PatientDTO(updatedPatient);

        enrichWithClientInfo(patientDTO, authHeader);

        return new PatientResponse("Patient updated successfully", patientDTO);
    }

    public PatientResponse getAllPatients(int page, int size, String sortBy, String sortDir, String authHeader) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Patient> patientPage = patientRepository.findAll(pageable);

        List<PatientDTO> patients = patientPage.getContent().stream()
                .map(PatientDTO::new)
                .collect(Collectors.toList());

        // Enriquecer con información del cliente
        patients.forEach(dto -> enrichWithClientInfo(dto, authHeader));

        Map<String, Object> response = new HashMap<>();
        response.put("patients", patients);
        response.put("totalElements", patientPage.getTotalElements());
        response.put("totalPages", patientPage.getTotalPages());
        response.put("currentPage", page);
        response.put("pageSize", size);

        return new PatientResponse("Patients retrieved successfully", response);
    }

    public PatientResponse getActivePatients(String authHeader) {
        List<Patient> patients = patientRepository.findByIsActive(true);

        List<PatientDTO> patientDTOs = patients.stream()
                .map(PatientDTO::new)
                .collect(Collectors.toList());

        // Enriquecer con información del cliente
        patientDTOs.forEach(dto -> enrichWithClientInfo(dto, authHeader));

        return new PatientResponse("Active patients retrieved successfully", patientDTOs);
    }

    public PatientResponse searchPatients(String query, String authHeader) {
        List<Patient> patients = patientRepository.searchPatients(query);

        List<PatientDTO> patientDTOs = patients.stream()
                .map(PatientDTO::new)
                .collect(Collectors.toList());

        // Enriquecer con información del cliente
        patientDTOs.forEach(dto -> enrichWithClientInfo(dto, authHeader));

        return new PatientResponse("Search completed", patientDTOs);
    }

    public PatientResponse getPatientsBySpecies(String species, String authHeader) {
        List<Patient> patients = patientRepository.findBySpeciesAndIsActive(species, true);

        List<PatientDTO> patientDTOs = patients.stream()
                .map(PatientDTO::new)
                .collect(Collectors.toList());

        // Enriquecer con información del cliente
        patientDTOs.forEach(dto -> enrichWithClientInfo(dto, authHeader));

        return new PatientResponse("Patients found by species", patientDTOs);
    }

    public PatientResponse getPatientByMicrochip(String microchipNumber, String authHeader) {
        Patient patient = patientRepository.findByMicrochipNumber(microchipNumber)
                .orElseThrow(() -> new PatientException("Patient not found with microchip: " + microchipNumber));

        PatientDTO patientDTO = new PatientDTO(patient);
        enrichWithClientInfo(patientDTO, authHeader);

        return new PatientResponse("Patient found by microchip", patientDTO);
    }

    public PatientResponse getPatientStats(String authHeader) {
        Long totalPatients = patientRepository.count();
        Long activePatients = patientRepository.countActivePatients();
        Long inactivePatients = totalPatients - activePatients;

        int currentYear = LocalDateTime.now().getYear();
        Long newPatientsThisYear = patientRepository.countPatientsByRegistrationYear(currentYear);

        List<String> species = patientRepository.findDistinctSpecies();
        List<String> breeds = patientRepository.findDistinctBreeds();

        Map<String, Long> speciesCount = new HashMap<>();
        for (String s : species) {
            speciesCount.put(s, patientRepository.countPatientsBySpecies(s));
        }

        Long sterilizedCount = (long) patientRepository.findBySterilizedStatus(true).size();
        Long vaccinatedCount = (long) patientRepository.findByVaccinationStatus(true).size();
        Long patientsWithAllergies = (long) patientRepository.findPatientsWithAllergies().size();
        Long patientsWithMedicalConditions = (long) patientRepository.findPatientsWithMedicalConditions().size();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPatients", totalPatients);
        stats.put("activePatients", activePatients);
        stats.put("inactivePatients", inactivePatients);
        stats.put("newPatientsThisYear", newPatientsThisYear);
        stats.put("totalSpecies", species.size());
        stats.put("totalBreeds", breeds.size());
        stats.put("speciesList", species);
        stats.put("breedsList", breeds);
        stats.put("speciesCount", speciesCount);
        stats.put("sterilizedCount", sterilizedCount);
        stats.put("vaccinatedCount", vaccinatedCount);
        stats.put("patientsWithAllergies", patientsWithAllergies);
        stats.put("patientsWithMedicalConditions", patientsWithMedicalConditions);

        return new PatientResponse("Patient statistics retrieved", stats);
    }

    public PatientResponse activatePatient(Long id, String currentUser) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientException("Patient not found with id: " + id));

        patient.setIsActive(true);
        patient.setUpdatedBy(currentUser);
        patientRepository.save(patient);

        return new PatientResponse("Patient activated successfully");
    }

    public PatientResponse deactivatePatient(Long id, String currentUser) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientException("Patient not found with id: " + id));

        patient.setIsActive(false);
        patient.setUpdatedBy(currentUser);
        patientRepository.save(patient);

        return new PatientResponse("Patient deactivated successfully");
    }

    public PatientResponse deletePatient(Long id, String currentUser) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientException("Patient not found with id: " + id));

        // Eliminación lógica (desactivar)
        patient.setIsActive(false);
        patient.setUpdatedBy(currentUser);
        patientRepository.save(patient);

        return new PatientResponse("Patient deleted successfully");
    }

    public List<String> getAllSpecies() {
        return patientRepository.findDistinctSpecies();
    }

    public List<String> getAllBreeds() {
        return patientRepository.findDistinctBreeds();
    }

    public List<String> getBreedsBySpecies(String species) {
        return patientRepository.findDistinctBreedsBySpecies(species);
    }

    // Helper method to enrich PatientDTO with client information
    private void enrichWithClientInfo(PatientDTO patientDTO, String authHeader) {
        try {
            if (authHeader != null && patientDTO.getClientId() != null) {
                var clientResponse = clientServiceClient.getClientById(patientDTO.getClientId(), authHeader);
                if (clientResponse.getBody() != null) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> clientData = (Map<String, Object>) clientResponse.getBody().get("client");
                    if (clientData != null) {
                        String firstName = (String) clientData.get("firstName");
                        String lastName = (String) clientData.get("lastName");
                        if (firstName != null && lastName != null) {
                            patientDTO.setClientName(firstName + " " + lastName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Si falla, simplemente no se enriquece la información del cliente
            // Esto no debe interrumpir el flujo principal
        }
    }
}