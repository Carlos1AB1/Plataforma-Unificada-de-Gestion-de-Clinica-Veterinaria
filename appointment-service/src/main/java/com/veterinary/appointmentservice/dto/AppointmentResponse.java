package com.veterinary.appointmentservice.dto;

public class AppointmentResponse {

    private String message;
    private AppointmentDTO appointment;
    private Object data;

    public AppointmentResponse() {}

    public AppointmentResponse(String message) {
        this.message = message;
    }

    public AppointmentResponse(String message, AppointmentDTO appointment) {
        this.message = message;
        this.appointment = appointment;
    }

    public AppointmentResponse(String message, Object data) {
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

    public AppointmentDTO getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentDTO appointment) {
        this.appointment = appointment;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}