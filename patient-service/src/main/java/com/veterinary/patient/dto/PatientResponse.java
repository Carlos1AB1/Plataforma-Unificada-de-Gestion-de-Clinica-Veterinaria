package com.veterinary.patient.dto;

public class PatientResponse {

    private String message;
    private PatientDTO patient;
    private Object data;

    public PatientResponse() {}

    public PatientResponse(String message) {
        this.message = message;
    }

    public PatientResponse(String message, PatientDTO patient) {
        this.message = message;
        this.patient = patient;
    }

    public PatientResponse(String message, Object data) {
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

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}