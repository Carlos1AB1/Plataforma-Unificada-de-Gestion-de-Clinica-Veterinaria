package com.veterinary.patient.exception;

public class PatientException extends RuntimeException {

    public PatientException(String message) {
        super(message);
    }

    public PatientException(String message, Throwable cause) {
        super(message, cause);
    }
}