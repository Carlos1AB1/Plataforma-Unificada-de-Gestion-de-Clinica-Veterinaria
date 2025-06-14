package com.veterinary.appointmentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "patient-service", path = "/patients")
public interface PatientServiceClient {

    @GetMapping("/{id}")
    ResponseEntity<Map<String, Object>> getPatientById(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String authorization
    );

    @GetMapping("/health")
    ResponseEntity<String> getPatientServiceHealth();
}