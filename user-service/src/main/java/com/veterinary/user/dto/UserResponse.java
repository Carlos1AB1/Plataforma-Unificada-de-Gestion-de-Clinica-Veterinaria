package com.veterinary.user.dto;

public class UserResponse {

    private String message;
    private UserDTO user;
    private Object data;

    public UserResponse() {}

    public UserResponse(String message) {
        this.message = message;
    }

    public UserResponse(String message, UserDTO user) {
        this.message = message;
        this.user = user;
    }

    public UserResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}