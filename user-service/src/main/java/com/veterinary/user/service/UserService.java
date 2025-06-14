package com.veterinary.user.service;

import com.veterinary.user.client.AuthServiceClient;
import com.veterinary.user.dto.*;
import com.veterinary.user.entity.UserProfile;
import com.veterinary.user.exception.UserException;
import com.veterinary.user.repository.UserProfileRepository;
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
public class UserService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AuthServiceClient authServiceClient;

    public UserResponse createUser(CreateUserRequest request) {
        // Validar que el username no existe
        if (userProfileRepository.existsByUsername(request.getUsername())) {
            throw new UserException("Username is already taken!");
        }

        // Validar que el email no existe
        if (userProfileRepository.existsByEmail(request.getEmail())) {
            throw new UserException("Email is already in use!");
        }

        try {
            // Crear usuario en auth service
            Map<String, Object> authRequest = new HashMap<>();
            authRequest.put("username", request.getUsername());
            authRequest.put("email", request.getEmail());
            authRequest.put("password", request.getPassword());
            authRequest.put("firstName", request.getFirstName());
            authRequest.put("lastName", request.getLastName());
            authRequest.put("role", request.getRole().name());

            var authResponse = authServiceClient.registerUser(authRequest);
            Map<String, Object> authData = authResponse.getBody();

            if (authData == null || authData.get("id") == null) {
                throw new UserException("Failed to create user in auth service");
            }

            Long authUserId = ((Number) authData.get("id")).longValue();

            // Crear perfil de usuario en user service
            UserProfile userProfile = new UserProfile();
            userProfile.setAuthUserId(authUserId);
            userProfile.setUsername(request.getUsername());
            userProfile.setEmail(request.getEmail());
            userProfile.setFirstName(request.getFirstName());
            userProfile.setLastName(request.getLastName());
            userProfile.setRole(request.getRole());
            userProfile.setPhoneNumber(request.getPhoneNumber());
            userProfile.setAddress(request.getAddress());
            userProfile.setIsActive(true);

            UserProfile savedProfile = userProfileRepository.save(userProfile);
            UserDTO userDTO = new UserDTO(savedProfile);

            return new UserResponse("User created successfully", userDTO);

        } catch (FeignException e) {
            throw new UserException("Failed to create user in auth service: " + e.getMessage());
        }
    }

    public UserResponse getUserById(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with id: " + id));

        UserDTO userDTO = new UserDTO(userProfile);
        return new UserResponse("User found", userDTO);
    }

    public UserResponse getUserByUsername(String username) {
        UserProfile userProfile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new UserException("User not found with username: " + username));

        UserDTO userDTO = new UserDTO(userProfile);
        return new UserResponse("User found", userDTO);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with id: " + id));

        // Validar username único si se está cambiando
        if (request.getUsername() != null && !request.getUsername().equals(userProfile.getUsername())) {
            if (userProfileRepository.existsByUsername(request.getUsername())) {
                throw new UserException("Username is already taken!");
            }
            userProfile.setUsername(request.getUsername());
        }

        // Validar email único si se está cambiando
        if (request.getEmail() != null && !request.getEmail().equals(userProfile.getEmail())) {
            if (userProfileRepository.existsByEmail(request.getEmail())) {
                throw new UserException("Email is already in use!");
            }
            userProfile.setEmail(request.getEmail());
        }

        // Actualizar campos
        if (request.getFirstName() != null) {
            userProfile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            userProfile.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            userProfile.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            userProfile.setAddress(request.getAddress());
        }

        UserProfile updatedProfile = userProfileRepository.save(userProfile);
        UserDTO userDTO = new UserDTO(updatedProfile);

        return new UserResponse("User updated successfully", userDTO);
    }

    public UserResponse updateUserRole(Long id, RoleUpdateRequest request) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with id: " + id));

        userProfile.setRole(request.getRole());
        UserProfile updatedProfile = userProfileRepository.save(userProfile);
        UserDTO userDTO = new UserDTO(updatedProfile);

        return new UserResponse("User role updated successfully", userDTO);
    }

    public UserResponse activateUser(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with id: " + id));

        userProfile.setIsActive(true);
        userProfileRepository.save(userProfile);

        return new UserResponse("User activated successfully");
    }

    public UserResponse deactivateUser(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with id: " + id));

        userProfile.setIsActive(false);
        userProfileRepository.save(userProfile);

        return new UserResponse("User deactivated successfully");
    }

    public UserResponse getAllUsers(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserProfile> userPage = userProfileRepository.findAll(pageable);

        List<UserDTO> users = userPage.getContent().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("totalElements", userPage.getTotalElements());
        response.put("totalPages", userPage.getTotalPages());
        response.put("currentPage", page);
        response.put("pageSize", size);

        return new UserResponse("Users retrieved successfully", response);
    }

    public UserResponse getUsersByRole(UserProfile.Role role) {
        List<UserProfile> users = userProfileRepository.findByRoleAndIsActive(role, true);

        List<UserDTO> userDTOs = users.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());

        return new UserResponse("Users retrieved successfully", userDTOs);
    }

    public UserResponse searchUsersByName(String name) {
        List<UserProfile> users = userProfileRepository.findByFirstNameContainingOrLastNameContaining(name);

        List<UserDTO> userDTOs = users.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());

        return new UserResponse("Users found", userDTOs);
    }

    public UserResponse getUserStats() {
        Long totalAdmins = userProfileRepository.countActiveUsersByRole(UserProfile.Role.ADMIN);
        Long totalVeterinarios = userProfileRepository.countActiveUsersByRole(UserProfile.Role.VETERINARIO);
        Long totalRecepcionistas = userProfileRepository.countActiveUsersByRole(UserProfile.Role.RECEPCIONISTA);
        Long totalUsers = userProfileRepository.count();
        Long activeUsers = (long) userProfileRepository.findByIsActive(true).size();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("activeUsers", activeUsers);
        stats.put("totalAdmins", totalAdmins);
        stats.put("totalVeterinarios", totalVeterinarios);
        stats.put("totalRecepcionistas", totalRecepcionistas);

        return new UserResponse("User statistics retrieved", stats);
    }

    public void updateLastLogin(String username) {
        userProfileRepository.findByUsername(username).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userProfileRepository.save(user);
        });
    }

    public UserResponse deleteUser(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with id: " + id));

        // En lugar de eliminar físicamente, desactivamos el usuario
        userProfile.setIsActive(false);
        userProfileRepository.save(userProfile);

        return new UserResponse("User deleted successfully");
    }
}