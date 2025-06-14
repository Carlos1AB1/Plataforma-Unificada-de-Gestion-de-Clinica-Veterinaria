package com.veterinary.clinic.medicalhistoryservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service")
public interface PatientClient {

    @GetMapping("/api/patients/{id}")
    PatientDto getPatientById(@PathVariable("id") Long id);

    @GetMapping("/api/patients/{id}/exists")
    boolean existsById(@PathVariable("id") Long id);
}