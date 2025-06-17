-- Script para insertar datos de prueba en las bases de datos

-- Insertar en la base de datos de usuarios (veterinarios)
USE veterinary_user_db;

INSERT IGNORE INTO users (id, username, password, email, first_name, last_name, role, created_at, updated_at) VALUES 
(1, 'vet001', '$2a$10$N9qo8uLOickgx2ZMRZoMye7xDz68mKTyHQ9YpSM4OAHi6qMmHuHvG', 'vet1@clinica.com', 'Dr. Juan', 'Rodriguez', 'VET', NOW(), NOW()),
(2, 'vet002', '$2a$10$N9qo8uLOickgx2ZMRZoMye7xDz68mKTyHQ9YpSM4OAHi6qMmHuHvG', 'vet2@clinica.com', 'Dra. Maria', 'Lopez', 'VET', NOW(), NOW());

-- Insertar en la base de datos de clientes
USE veterinary_client_db;

INSERT IGNORE INTO clients (id, first_name, last_name, email, phone, address, created_at, updated_at) VALUES 
(1, 'Carlos', 'Garcia', 'carlos@email.com', '555-1234', 'Calle Principal 123', NOW(), NOW()),
(2, 'Ana', 'Martinez', 'ana@email.com', '555-5678', 'Avenida Central 456', NOW(), NOW());

-- Insertar en la base de datos de pacientes (mascotas)
USE veterinary_patient_db;

INSERT IGNORE INTO patients (id, name, species, breed, age, weight, color, client_id, medical_history, allergies, created_at, updated_at) VALUES 
(1, 'Max', 'DOG', 'Labrador', 3, 25.5, 'Golden', 1, 'Vacunas al día', 'Ninguna', NOW(), NOW()),
(2, 'Luna', 'CAT', 'Persa', 2, 4.2, 'Blanco', 2, 'Esterilizada', 'Ninguna', NOW(), NOW()),
(3, 'Rocky', 'DOG', 'Pastor Alemán', 5, 35.0, 'Negro y marrón', 1, 'Tratamiento para displasia', 'Ninguna', NOW(), NOW()); 