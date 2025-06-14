package com.veterinary.appointmentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "user-service", path = "/users")
public interface UserServiceClient {

    @GetMapping("/{id}")
    ResponseEntity<Map<String, Object>> getUserById(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String authorization
    );

    @GetMapping("/role/VETERINARIO")
    ResponseEntity<Map<String, Object>> getVeterinarians(
            @RequestHeader("Authorization") String authorization
    );

    @GetMapping("/health")
    ResponseEntity<String> getUserServiceHealth();
}