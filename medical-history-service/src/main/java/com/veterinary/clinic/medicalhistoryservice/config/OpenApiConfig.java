package com.veterinary.clinic.medicalhistoryservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Medical History Service API",
                description = "API for managing medical history records and documents in the veterinary clinic system",
                version = "1.0.0",
                contact = @Contact(
                        name = "Veterinary Clinic Support",
                        email = "support@veterinaryclinic.com",
                        url = "https://veterinaryclinic.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        description = "Local Development Server",
                        url = "http://localhost:8108"
                ),
                @Server(
                        description = "API Gateway",
                        url = "http://localhost:8080"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "JWT Authorization header using the Bearer scheme. Example: 'Bearer {token}'"
)
public class OpenApiConfig {
}