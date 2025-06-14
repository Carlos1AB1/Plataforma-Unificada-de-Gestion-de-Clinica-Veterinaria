package com.veterinary.patient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "client-service", path = "/clients")
public interface ClientServiceClient {

    @GetMapping("/{id}")
    ResponseEntity<Map<String, Object>> getClientById(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String authorization
    );

    @GetMapping("/health")
    ResponseEntity<String> getClientServiceHealth();
}