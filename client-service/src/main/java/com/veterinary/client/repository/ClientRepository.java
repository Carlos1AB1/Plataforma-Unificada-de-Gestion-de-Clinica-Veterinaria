package com.veterinary.client.repository;

import com.veterinary.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByDocumentNumber(String documentNumber);

    Optional<Client> findByEmail(String email);

    List<Client> findByIsActive(Boolean isActive);

    Boolean existsByDocumentNumber(String documentNumber);

    Boolean existsByEmail(String email);

    @Query("SELECT c FROM Client c WHERE c.firstName LIKE %:name% OR c.lastName LIKE %:name%")
    List<Client> findByFirstNameContainingOrLastNameContaining(@Param("name") String name);

    @Query("SELECT c FROM Client c WHERE " +
            "(LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "c.documentNumber LIKE CONCAT('%', :query, '%') OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "c.phoneNumber LIKE CONCAT('%', :query, '%')) " +
            "AND c.isActive = true")
    List<Client> searchClients(@Param("query") String query);

    @Query("SELECT c FROM Client c WHERE c.city = :city AND c.isActive = true")
    List<Client> findByCity(@Param("city") String city);

    @Query("SELECT DISTINCT c.city FROM Client c WHERE c.city IS NOT NULL AND c.isActive = true ORDER BY c.city")
    List<String> findDistinctCities();

    @Query("SELECT c FROM Client c WHERE c.documentType = :documentType AND c.isActive = true")
    List<Client> findByDocumentType(@Param("documentType") Client.DocumentType documentType);

    @Query("SELECT c FROM Client c WHERE c.isActive = true ORDER BY c.firstName, c.lastName")
    List<Client> findAllActiveClients();

    @Query("SELECT COUNT(c) FROM Client c WHERE c.isActive = true")
    Long countActiveClients();

    @Query("SELECT COUNT(c) FROM Client c WHERE YEAR(c.createdAt) = :year")
    Long countClientsByYear(@Param("year") int year);

    @Query("SELECT c FROM Client c WHERE c.emergencyContactPhone IS NOT NULL AND c.isActive = true")
    List<Client> findClientsWithEmergencyContact();
}