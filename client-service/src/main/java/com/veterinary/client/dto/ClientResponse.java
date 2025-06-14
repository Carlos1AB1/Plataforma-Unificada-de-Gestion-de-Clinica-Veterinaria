package com.veterinary.client.dto;

public class ClientResponse {

    private String message;
    private ClientDTO client;
    private Object data;

    public ClientResponse() {}

    public ClientResponse(String message) {
        this.message = message;
    }

    public ClientResponse(String message, ClientDTO client) {
        this.message = message;
        this.client = client;
    }

    public ClientResponse(String message, Object data) {
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

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}