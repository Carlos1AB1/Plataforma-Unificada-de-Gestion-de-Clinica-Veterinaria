-- =====================================================
-- VETERINARY CLINIC - CLIENT SERVICE DATABASE
-- =====================================================

USE veterinary_client_db;

-- Crear usuario adicional
CREATE USER IF NOT EXISTS 'vet_user'@'%' IDENTIFIED BY 'vet_password';
GRANT ALL PRIVILEGES ON veterinary_client_db.* TO 'vet_user'@'%';
FLUSH PRIVILEGES;

-- Configuraciones de base de datos
SET FOREIGN_KEY_CHECKS = 0;

-- Tabla de clientes
CREATE TABLE IF NOT EXISTS clients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_number VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    secondary_phone VARCHAR(20),
    address TEXT,
    city VARCHAR(50),
    state VARCHAR(50),
    postal_code VARCHAR(10),
    country VARCHAR(50) DEFAULT 'España',
    date_of_birth DATE,
    identity_document VARCHAR(20),
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    registration_date DATE DEFAULT (CURRENT_DATE),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de métodos de pago preferidos del cliente
CREATE TABLE IF NOT EXISTS client_payment_methods (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT NOT NULL,
    payment_type ENUM('CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'BANK_TRANSFER', 'INSURANCE') NOT NULL,
    is_preferred BOOLEAN DEFAULT FALSE,
    card_last_digits VARCHAR(4),
    insurance_provider VARCHAR(100),
    insurance_policy_number VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);

-- Tabla de comunicaciones con clientes
CREATE TABLE IF NOT EXISTS client_communications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT NOT NULL,
    communication_type ENUM('EMAIL', 'SMS', 'PHONE_CALL', 'LETTER', 'IN_PERSON') NOT NULL,
    subject VARCHAR(255),
    message TEXT,
    sent_by BIGINT,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('SENT', 'DELIVERED', 'READ', 'FAILED') DEFAULT 'SENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);

-- Tabla de preferencias de comunicación del cliente
CREATE TABLE IF NOT EXISTS client_communication_preferences (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT UNIQUE NOT NULL,
    email_notifications BOOLEAN DEFAULT TRUE,
    sms_notifications BOOLEAN DEFAULT TRUE,
    phone_notifications BOOLEAN DEFAULT TRUE,
    marketing_emails BOOLEAN DEFAULT FALSE,
    appointment_reminders BOOLEAN DEFAULT TRUE,
    treatment_updates BOOLEAN DEFAULT TRUE,
    promotional_offers BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);

-- Insertar datos de ejemplo
INSERT IGNORE INTO clients (client_number, first_name, last_name, email, phone, address, city, state, postal_code, identity_document) VALUES 
('CLI001', 'Juan', 'Pérez', 'juan.perez@email.com', '+34612345678', 'Calle Mayor 123', 'Madrid', 'Madrid', '28001', '12345678A'),
('CLI002', 'María', 'García', 'maria.garcia@email.com', '+34623456789', 'Avenida Libertad 456', 'Barcelona', 'Barcelona', '08001', '87654321B'),
('CLI003', 'Pedro', 'López', 'pedro.lopez@email.com', '+34634567890', 'Plaza España 789', 'Valencia', 'Valencia', '46001', '11223344C'),
('CLI004', 'Laura', 'Sánchez', 'laura.sanchez@email.com', '+34645678901', 'Calle Sol 321', 'Sevilla', 'Andalucía', '41001', '55667788D'),
('CLI005', 'Miguel', 'Fernández', 'miguel.fernandez@email.com', '+34656789012', 'Avenida Luna 654', 'Bilbao', 'País Vasco', '48001', '99887766E');

-- Insertar métodos de pago preferidos
INSERT IGNORE INTO client_payment_methods (client_id, payment_type, is_preferred) VALUES 
(1, 'CREDIT_CARD', TRUE),
(1, 'CASH', FALSE),
(2, 'DEBIT_CARD', TRUE),
(3, 'CASH', TRUE),
(4, 'CREDIT_CARD', TRUE),
(5, 'BANK_TRANSFER', TRUE);

-- Insertar preferencias de comunicación
INSERT IGNORE INTO client_communication_preferences (client_id, email_notifications, sms_notifications, appointment_reminders) VALUES 
(1, TRUE, TRUE, TRUE),
(2, TRUE, FALSE, TRUE),
(3, FALSE, TRUE, TRUE),
(4, TRUE, TRUE, TRUE),
(5, TRUE, TRUE, FALSE);

SET FOREIGN_KEY_CHECKS = 1; 