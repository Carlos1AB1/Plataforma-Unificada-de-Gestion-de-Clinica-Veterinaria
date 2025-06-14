package com.veterinary.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "auth-service", path = "/auth")
public interface AuthServiceClient {

    @PostMapping("/register")
    ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, Object> registerRequest);

    @GetMapping("/validate")
    ResponseEntity<Boolean> validateToken(@RequestParam("token") String token);

    @PostMapping("/change-password")
    ResponseEntity<Map<String, Object>> changePassword(
            @RequestHeader("Authorization") String authorization,
            @RequestBody Map<String, Object> changePasswordRequest);
}