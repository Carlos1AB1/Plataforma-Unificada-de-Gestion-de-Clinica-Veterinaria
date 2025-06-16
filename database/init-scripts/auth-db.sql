-- =====================================================
-- VETERINARY CLINIC - AUTH SERVICE DATABASE
-- =====================================================

USE veterinary_auth_db;

-- Crear usuario adicional
CREATE USER IF NOT EXISTS 'vet_user'@'%' IDENTIFIED BY 'vet_password';
GRANT ALL PRIVILEGES ON veterinary_auth_db.* TO 'vet_user'@'%';
FLUSH PRIVILEGES;

-- Configuraciones de base de datos
SET FOREIGN_KEY_CHECKS = 0;

-- Tabla de usuarios del sistema de autenticación
CREATE TABLE IF NOT EXISTS auth_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    enabled BOOLEAN DEFAULT TRUE,
    account_non_expired BOOLEAN DEFAULT TRUE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de relación usuario-rol
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES auth_users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Tabla de tokens de acceso
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(500) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES auth_users(id) ON DELETE CASCADE
);

-- Tabla para reset de passwords
CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES auth_users(id) ON DELETE CASCADE
);

-- Insertar roles por defecto
INSERT IGNORE INTO roles (name, description) VALUES 
('ROLE_ADMIN', 'Administrador del sistema'),
('ROLE_VETERINARIAN', 'Veterinario'),
('ROLE_ASSISTANT', 'Asistente veterinario'),
('ROLE_CLIENT', 'Cliente');

-- Insertar usuario administrador por defecto
-- Password: admin123 (encriptado con BCrypt)
INSERT IGNORE INTO auth_users (username, email, password, first_name, last_name) VALUES 
('admin', 'admin@veterinary.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8UUW21TJJUrCi', 'Admin', 'System');

-- Asignar rol de administrador al usuario admin
INSERT IGNORE INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM auth_users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

SET FOREIGN_KEY_CHECKS = 1; 