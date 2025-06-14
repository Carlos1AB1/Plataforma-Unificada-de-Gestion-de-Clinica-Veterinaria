package com.veterinary.user.controller;

import com.veterinary.user.dto.*;
import com.veterinary.user.entity.UserProfile;
import com.veterinary.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "User management operations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse response = userService.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user information")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleUpdateRequest request) {
        UserResponse response = userService.updateUserRole(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> activateUser(@PathVariable Long id) {
        UserResponse response = userService.activateUser(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable Long id) {
        UserResponse response = userService.deactivateUser(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all users with pagination")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO')")
    public ResponseEntity<UserResponse> getAllUsers(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "firstName") String sortBy,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "asc") String sortDir) {
        UserResponse response = userService.getAllUsers(page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "Get users by role")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<UserResponse> getUsersByRole(@PathVariable UserProfile.Role role) {
        UserResponse response = userService.getUsersByRole(role);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search users by name")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<UserResponse> searchUsersByName(@RequestParam String name) {
        UserResponse response = userService.searchUsersByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get user statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserStats() {
        UserResponse response = userService.getUserStats();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{username}/last-login")
    @Operation(summary = "Update last login time")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<Void> updateLastLogin(@PathVariable String username) {
        userService.updateLastLogin(username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user (deactivate)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {
        UserResponse response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    @Operation(summary = "Get current user profile")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VETERINARIO') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<UserResponse> getCurrentUserProfile(Authentication authentication) {
        UserResponse response = userService.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User Service is running!");
    }
}