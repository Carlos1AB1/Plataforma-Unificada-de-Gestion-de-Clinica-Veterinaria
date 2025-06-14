package com.veterinary.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendPasswordResetEmail(String email, String resetToken) {
        // Simulación de envío de email
        String resetLink = "http://localhost:8080/auth/reset-password?token=" + resetToken;

        String emailContent = String.format(
                """
                ======= VETERINARY CLINIC - PASSWORD RESET =======
                
                Hello,
                
                You requested a password reset for your account.
                Click the link below to reset your password:
                
                %s
                
                This link will expire in 1 hour.
                
                If you didn't request this, please ignore this email.
                
                Best regards,
                Veterinary Clinic Team
                ================================================
                """, resetLink
        );

        logger.info("=== EMAIL SENT SIMULATION ===");
        logger.info("To: {}", email);
        logger.info("Subject: Password Reset Request");
        logger.info("Content:\n{}", emailContent);
        logger.info("=== END EMAIL SIMULATION ===");
    }

    public void sendWelcomeEmail(String email, String firstName, String username) {
        // Simulación de envío de email de bienvenida
        String emailContent = String.format(
                """
                ======= WELCOME TO VETERINARY CLINIC =======
                
                Hello %s,
                
                Welcome to our Veterinary Clinic system!
                
                Your account details:
                - Username: %s
                - Email: %s
                
                You can now login and start using the system.
                
                Best regards,
                Veterinary Clinic Team
                ==========================================
                """, firstName, username, email
        );

        logger.info("=== WELCOME EMAIL SENT SIMULATION ===");
        logger.info("To: {}", email);
        logger.info("Subject: Welcome to Veterinary Clinic");
        logger.info("Content:\n{}", emailContent);
        logger.info("=== END EMAIL SIMULATION ===");
    }
}