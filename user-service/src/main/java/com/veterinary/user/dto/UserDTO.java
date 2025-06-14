package com.veterinary.user.dto;

import com.veterinary.user.entity.UserProfile;
import java.time.LocalDateTime;

public class UserDTO {

    private Long id;
    private Long authUserId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private UserProfile.Role role;
    private String phoneNumber;
    private String address;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
    private String fullName;

    public UserDTO() {}

    public UserDTO(UserProfile userProfile) {
        this.id = userProfile.getId();
        this.authUserId = userProfile.getAuthUserId();
        this.username = userProfile.getUsername();
        this.email = userProfile.getEmail();
        this.firstName = userProfile.getFirstName();
        this.lastName = userProfile.getLastName();
        this.role = userProfile.getRole();
        this.phoneNumber = userProfile.getPhoneNumber();
        this.address = userProfile.getAddress();
        this.isActive = userProfile.getIsActive();
        this.createdAt = userProfile.getCreatedAt();
        this.updatedAt = userProfile.getUpdatedAt();
        this.lastLogin = userProfile.getLastLogin();
        this.fullName = userProfile.getFullName();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(Long authUserId) {
        this.authUserId = authUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserProfile.Role getRole() {
        return role;
    }

    public void setRole(UserProfile.Role role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}