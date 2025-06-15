package com.veterinary.clinic.reportservice.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserResponseDTO getUserById(@PathVariable("id") Long id);

    @GetMapping("/api/users")
    List<UserResponseDTO> getAllUsers();

    @GetMapping("/api/users/count/total")
    Long getTotalUsersCount();

    @GetMapping("/api/users/veterinarians")
    List<UserResponseDTO> getVeterinarians();

    class UserResponseDTO {
        private Long id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String role;

        // Constructors
        public UserResponseDTO() {}

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getFullName() {
            return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
        }
    }
}
