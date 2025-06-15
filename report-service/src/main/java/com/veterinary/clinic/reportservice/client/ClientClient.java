package com.veterinary.clinic.reportservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "client-service")
public interface ClientClient {

    @GetMapping("/api/clients/{id}")
    ClientResponseDTO getClientById(@PathVariable("id") Long id);

    @GetMapping("/api/clients")
    List<ClientResponseDTO> getAllClients();

    class ClientResponseDTO {
        private Long id;
        private String firstName;
        private String lastName;
        private String document;
        private String phone;
        private String email;
        private String address;

        // Constructors
        public ClientResponseDTO() {}

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getDocument() { return document; }
        public void setDocument(String document) { this.document = document; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getFullName() {
            return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
        }
    }
}
