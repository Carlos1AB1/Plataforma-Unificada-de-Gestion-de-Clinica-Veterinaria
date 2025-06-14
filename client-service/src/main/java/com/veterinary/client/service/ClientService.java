package com.veterinary.client.service;

import com.veterinary.client.dto.*;
import com.veterinary.client.entity.Client;
import com.veterinary.client.exception.ClientException;
import com.veterinary.client.repository.ClientRepository;
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
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public ClientResponse createClient(CreateClientRequest request, String currentUser) {
        // Validar que el documento no existe
        if (clientRepository.existsByDocumentNumber(request.getDocumentNumber())) {
            throw new ClientException("Document number already exists!");
        }

        // Validar que el email no existe (si se proporciona)
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            if (clientRepository.existsByEmail(request.getEmail())) {
                throw new ClientException("Email already exists!");
            }
        }

        // Crear nuevo cliente
        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setDocumentNumber(request.getDocumentNumber());
        client.setDocumentType(request.getDocumentType());
        client.setEmail(request.getEmail());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setAlternativePhone(request.getAlternativePhone());
        client.setAddress(request.getAddress());
        client.setCity(request.getCity());
        client.setPostalCode(request.getPostalCode());
        client.setBirthDate(request.getBirthDate());
        client.setGender(request.getGender());
        client.setOccupation(request.getOccupation());
        client.setEmergencyContactName(request.getEmergencyContactName());
        client.setEmergencyContactPhone(request.getEmergencyContactPhone());
        client.setNotes(request.getNotes());
        client.setIsActive(true);
        client.setCreatedBy(currentUser);
        client.setUpdatedBy(currentUser);

        Client savedClient = clientRepository.save(client);
        ClientDTO clientDTO = new ClientDTO(savedClient);

        return new ClientResponse("Client created successfully", clientDTO);
    }

    public ClientResponse getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientException("Client not found with id: " + id));

        ClientDTO clientDTO = new ClientDTO(client);
        return new ClientResponse("Client found", clientDTO);
    }

    public ClientResponse getClientByDocumentNumber(String documentNumber) {
        Client client = clientRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new ClientException("Client not found with document number: " + documentNumber));

        ClientDTO clientDTO = new ClientDTO(client);
        return new ClientResponse("Client found", clientDTO);
    }

    public ClientResponse updateClient(Long id, UpdateClientRequest request, String currentUser) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientException("Client not found with id: " + id));

        // Validar email único si se está cambiando
        if (request.getEmail() != null && !request.getEmail().equals(client.getEmail())) {
            if (clientRepository.existsByEmail(request.getEmail())) {
                throw new ClientException("Email already exists!");
            }
        }

        // Actualizar campos
        if (request.getFirstName() != null) {
            client.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            client.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            client.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            client.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAlternativePhone() != null) {
            client.setAlternativePhone(request.getAlternativePhone());
        }
        if (request.getAddress() != null) {
            client.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            client.setCity(request.getCity());
        }
        if (request.getPostalCode() != null) {
            client.setPostalCode(request.getPostalCode());
        }
        if (request.getBirthDate() != null) {
            client.setBirthDate(request.getBirthDate());
        }
        if (request.getGender() != null) {
            client.setGender(request.getGender());
        }
        if (request.getOccupation() != null) {
            client.setOccupation(request.getOccupation());
        }
        if (request.getEmergencyContactName() != null) {
            client.setEmergencyContactName(request.getEmergencyContactName());
        }
        if (request.getEmergencyContactPhone() != null) {
            client.setEmergencyContactPhone(request.getEmergencyContactPhone());
        }
        if (request.getNotes() != null) {
            client.setNotes(request.getNotes());
        }

        client.setUpdatedBy(currentUser);
        Client updatedClient = clientRepository.save(client);
        ClientDTO clientDTO = new ClientDTO(updatedClient);

        return new ClientResponse("Client updated successfully", clientDTO);
    }

    public ClientResponse activateClient(Long id, String currentUser) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientException("Client not found with id: " + id));

        client.setIsActive(true);
        client.setUpdatedBy(currentUser);
        clientRepository.save(client);

        return new ClientResponse("Client activated successfully");
    }

    public ClientResponse deactivateClient(Long id, String currentUser) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientException("Client not found with id: " + id));

        client.setIsActive(false);
        client.setUpdatedBy(currentUser);
        clientRepository.save(client);

        return new ClientResponse("Client deactivated successfully");
    }

    public ClientResponse getAllClients(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Client> clientPage = clientRepository.findAll(pageable);

        List<ClientDTO> clients = clientPage.getContent().stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("clients", clients);
        response.put("totalElements", clientPage.getTotalElements());
        response.put("totalPages", clientPage.getTotalPages());
        response.put("currentPage", page);
        response.put("pageSize", size);

        return new ClientResponse("Clients retrieved successfully", response);
    }

    public ClientResponse getActiveClients() {
        List<Client> clients = clientRepository.findAllActiveClients();

        List<ClientDTO> clientDTOs = clients.stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());

        return new ClientResponse("Active clients retrieved successfully", clientDTOs);
    }

    public ClientResponse searchClients(String query) {
        List<Client> clients = clientRepository.searchClients(query);

        List<ClientDTO> clientDTOs = clients.stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());

        return new ClientResponse("Search completed", clientDTOs);
    }

    public ClientResponse searchClientsByName(String name) {
        List<Client> clients = clientRepository.findByFirstNameContainingOrLastNameContaining(name);

        List<ClientDTO> clientDTOs = clients.stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());

        return new ClientResponse("Clients found by name", clientDTOs);
    }

    public ClientResponse getClientsByCity(String city) {
        List<Client> clients = clientRepository.findByCity(city);

        List<ClientDTO> clientDTOs = clients.stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());

        return new ClientResponse("Clients found by city", clientDTOs);
    }

    public ClientResponse getClientsByDocumentType(Client.DocumentType documentType) {
        List<Client> clients = clientRepository.findByDocumentType(documentType);

        List<ClientDTO> clientDTOs = clients.stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());

        return new ClientResponse("Clients found by document type", clientDTOs);
    }

    public ClientResponse getClientStats() {
        Long totalClients = clientRepository.count();
        Long activeClients = clientRepository.countActiveClients();
        Long inactiveClients = totalClients - activeClients;

        int currentYear = LocalDateTime.now().getYear();
        Long newClientsThisYear = clientRepository.countClientsByYear(currentYear);

        List<String> cities = clientRepository.findDistinctCities();
        Long clientsWithEmergencyContact = (long) clientRepository.findClientsWithEmergencyContact().size();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalClients", totalClients);
        stats.put("activeClients", activeClients);
        stats.put("inactiveClients", inactiveClients);
        stats.put("newClientsThisYear", newClientsThisYear);
        stats.put("totalCities", cities.size());
        stats.put("citiesList", cities);
        stats.put("clientsWithEmergencyContact", clientsWithEmergencyContact);

        return new ClientResponse("Client statistics retrieved", stats);
    }

    public ClientResponse deleteClient(Long id, String currentUser) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientException("Client not found with id: " + id));

        // Eliminación lógica (desactivar)
        client.setIsActive(false);
        client.setUpdatedBy(currentUser);
        clientRepository.save(client);

        return new ClientResponse("Client deleted successfully");
    }

    public List<String> getAllCities() {
        return clientRepository.findDistinctCities();
    }
}