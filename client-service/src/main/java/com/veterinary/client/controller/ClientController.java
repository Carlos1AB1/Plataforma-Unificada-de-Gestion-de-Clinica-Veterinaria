package com.veterinary.client.controller;

import com.veterinary.client.dto.*;
import com.veterinary.client.entity.Client;
import com.veterinary.client.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@Tag(name = "Client Management", description = "Client (pet owners) management operations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    @Operation(summary = "Create a new client")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<ClientResponse> createClient(
            @Valid @RequestBody CreateClientRequest request,
            Authentication authentication) {
        ClientResponse response = clientService.createClient(request, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
        ClientResponse response = clientService.getClientById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/document/{documentNumber}")
    @Operation(summary = "Get client by document number")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<ClientResponse> getClientByDocumentNumber(@PathVariable String documentNumber) {
        ClientResponse response = clientService.getClientByDocumentNumber(documentNumber);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update client information")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<ClientResponse> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody UpdateClientRequest request,
            Authentication authentication) {
        ClientResponse response = clientService.updateClient(id, request, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate client")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<ClientResponse> activateClient(
            @PathVariable Long id,
            Authentication authentication) {
        ClientResponse response = clientService.activateClient(id, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate client")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<ClientResponse> deactivateClient(
            @PathVariable Long id,
            Authentication authentication) {
        ClientResponse response = clientService.deactivateClient(id, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all clients with pagination")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<ClientResponse> getAllClients(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "firstName") String sortBy,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "asc") String sortDir) {
        ClientResponse response = clientService.getAllClients(page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active clients")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<ClientResponse> getActiveClients() {
        ClientResponse response = clientService.getActiveClients();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search clients by multiple criteria")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<ClientResponse> searchClients(
            @Parameter(description = "Search query (name, document, email, phone)")
            @RequestParam String query) {
        ClientResponse response = clientService.searchClients(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/name")
    @Operation(summary = "Search clients by name")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<ClientResponse> searchClientsByName(
            @Parameter(description = "Client name to search")
            @RequestParam String name) {
        ClientResponse response = clientService.searchClientsByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/city/{city}")
    @Operation(summary = "Get clients by city")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<ClientResponse> getClientsByCity(@PathVariable String city) {
        ClientResponse response = clientService.getClientsByCity(city);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/document-type/{documentType}")
    @Operation(summary = "Get clients by document type")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<ClientResponse> getClientsByDocumentType(@PathVariable Client.DocumentType documentType) {
        ClientResponse response = clientService.getClientsByDocumentType(documentType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get client statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<ClientResponse> getClientStats() {
        ClientResponse response = clientService.getClientStats();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cities")
    @Operation(summary = "Get all cities list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<String>> getAllCities() {
        List<String> cities = clientService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete client (deactivate)")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<ClientResponse> deleteClient(
            @PathVariable Long id,
            Authentication authentication) {
        ClientResponse response = clientService.deleteClient(id, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Client Service is running!");
    }
}