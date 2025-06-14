package com.veterinary.user.dto;

import com.veterinary.user.entity.UserProfile;
import jakarta.validation.constraints.NotNull;

public class RoleUpdateRequest {

    @NotNull(message = "Role is required")
    private UserProfile.Role role;

    public RoleUpdateRequest() {}

    public RoleUpdateRequest(UserProfile.Role role) {
        this.role = role;
    }

    // Getters and Setters
    public UserProfile.Role getRole() {
        return role;
    }

    public void setRole(UserProfile.Role role) {
        this.role = role;
    }
}