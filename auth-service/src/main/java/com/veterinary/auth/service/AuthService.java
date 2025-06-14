package com.veterinary.auth.service;

import com.veterinary.auth.dto.*;
import com.veterinary.auth.entity.User;
import com.veterinary.auth.exception.AuthException;
import com.veterinary.auth.repository.UserRepository;
import com.veterinary.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public AuthResponse register(RegisterRequest request) {
        // Validar que el username no existe
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AuthException("Username is already taken!");
        }

        // Validar que el email no existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email is already in use!");
        }

        // Crear nuevo usuario
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole());
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        // Enviar email de bienvenida
        emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getFirstName(), savedUser.getUsername());

        // Generar token JWT
        String token = jwtUtil.generateToken(savedUser);

        return new AuthResponse(token, savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new AuthException("Invalid username or password"));

        if (!user.getIsActive()) {
            throw new AuthException("Account is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user);
    }

    public AuthResponse changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException("User not found"));

        // Validar contraseña actual
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AuthException("Current password is incorrect");
        }

        // Validar que las nuevas contraseñas coincidan
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AuthException("New passwords do not match");
        }

        // Actualizar contraseña
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new AuthResponse("Password changed successfully");
    }

    public AuthResponse forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Email not found"));

        // Generar token de reset
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));

        userRepository.save(user);

        // Enviar email con token
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);

        return new AuthResponse("Password reset email sent");
    }

    public AuthResponse resetPassword(String token, String newPassword) {
        User user = userRepository.findByValidResetToken(token, LocalDateTime.now())
                .orElseThrow(() -> new AuthException("Invalid or expired reset token"));

        // Actualizar contraseña y limpiar token
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);

        return new AuthResponse("Password reset successfully");
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException("User not found"));
    }

    public boolean validateToken(String token) {
        return jwtUtil.isTokenValid(token);
    }
}