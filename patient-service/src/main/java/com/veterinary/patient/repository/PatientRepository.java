package com.veterinary.patient.repository;

import com.veterinary.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByClientId(Long clientId);

    List<Patient> findByClientIdAndIsActive(Long clientId, Boolean isActive);

    List<Patient> findBySpecies(String species);

    List<Patient> findBySpeciesAndIsActive(String species, Boolean isActive);

    List<Patient> findByIsActive(Boolean isActive);

    Optional<Patient> findByMicrochipNumber(String microchipNumber);

    Boolean existsByMicrochipNumber(String microchipNumber);

    @Query("SELECT p FROM Patient p WHERE p.name LIKE %:name% AND p.isActive = true")
    List<Patient> findByNameContaining(@Param("name") String name);

    @Query("SELECT p FROM Patient p WHERE " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.species) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.breed) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "p.microchipNumber LIKE CONCAT('%', :query, '%')) " +
            "AND p.isActive = true")
    List<Patient> searchPatients(@Param("query") String query);

    @Query("SELECT DISTINCT p.species FROM Patient p WHERE p.isActive = true ORDER BY p.species")
    List<String> findDistinctSpecies();

    @Query("SELECT DISTINCT p.breed FROM Patient p WHERE p.breed IS NOT NULL AND p.isActive = true ORDER BY p.breed")
    List<String> findDistinctBreeds();

    @Query("SELECT DISTINCT p.breed FROM Patient p WHERE p.species = :species AND p.breed IS NOT NULL AND p.isActive = true ORDER BY p.breed")
    List<String> findDistinctBreedsBySpecies(@Param("species") String species);

    @Query("SELECT COUNT(p) FROM Patient p WHERE p.isActive = true")
    Long countActivePatients();

    @Query("SELECT COUNT(p) FROM Patient p WHERE p.species = :species AND p.isActive = true")
    Long countPatientsBySpecies(@Param("species") String species);

    @Query("SELECT COUNT(p) FROM Patient p WHERE p.clientId = :clientId AND p.isActive = true")
    Long countPatientsByClient(@Param("clientId") Long clientId);

    @Query("SELECT COUNT(p) FROM Patient p WHERE YEAR(p.registrationDate) = :year")
    Long countPatientsByRegistrationYear(@Param("year") int year);

    @Query("SELECT p FROM Patient p WHERE p.isSterilized = :sterilized AND p.isActive = true")
    List<Patient> findBySterilizedStatus(@Param("sterilized") Boolean sterilized);

    @Query("SELECT p FROM Patient p WHERE p.isVaccinated = :vaccinated AND p.isActive = true")
    List<Patient> findByVaccinationStatus(@Param("vaccinated") Boolean vaccinated);

    @Query("SELECT p FROM Patient p WHERE p.allergies IS NOT NULL AND p.allergies != '' AND p.isActive = true")
    List<Patient> findPatientsWithAllergies();

    @Query("SELECT p FROM Patient p WHERE p.medicalConditions IS NOT NULL AND p.medicalConditions != '' AND p.isActive = true")
    List<Patient> findPatientsWithMedicalConditions();
}