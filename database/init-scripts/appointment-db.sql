-- =====================================================
-- VETERINARY CLINIC - APPOINTMENT SERVICE DATABASE
-- =====================================================

USE veterinary_appointment_db;

-- Crear usuario adicional
CREATE USER IF NOT EXISTS 'vet_user'@'%' IDENTIFIED BY 'vet_password';
GRANT ALL PRIVILEGES ON veterinary_appointment_db.* TO 'vet_user'@'%';
FLUSH PRIVILEGES;

-- Configuraciones de base de datos
SET FOREIGN_KEY_CHECKS = 0;

-- Tabla de tipos de citas
CREATE TABLE IF NOT EXISTS appointment_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    duration_minutes INT NOT NULL DEFAULT 30,
    base_price DECIMAL(8,2),
    color_code VARCHAR(7),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de citas
CREATE TABLE IF NOT EXISTS appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_number VARCHAR(20) UNIQUE NOT NULL,
    patient_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    veterinarian_id BIGINT NOT NULL,
    appointment_type_id BIGINT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    duration_minutes INT NOT NULL DEFAULT 30,
    status ENUM('SCHEDULED', 'CONFIRMED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED', 'NO_SHOW') DEFAULT 'SCHEDULED',
    priority ENUM('LOW', 'NORMAL', 'HIGH', 'URGENT') DEFAULT 'NORMAL',
    reason TEXT,
    symptoms TEXT,
    notes TEXT,
    estimated_cost DECIMAL(8,2),
    actual_cost DECIMAL(8,2),
    reminder_sent BOOLEAN DEFAULT FALSE,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    cancelled_at TIMESTAMP NULL,
    cancelled_by BIGINT,
    cancellation_reason TEXT,
    FOREIGN KEY (appointment_type_id) REFERENCES appointment_types(id)
);

-- Tabla de recordatorios de citas
CREATE TABLE IF NOT EXISTS appointment_reminders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    reminder_type ENUM('EMAIL', 'SMS', 'PHONE_CALL') NOT NULL,
    reminder_time TIMESTAMP NOT NULL,
    status ENUM('PENDING', 'SENT', 'DELIVERED', 'FAILED') DEFAULT 'PENDING',
    message TEXT,
    sent_at TIMESTAMP NULL,
    delivered_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE
);

-- Tabla de seguimiento de citas
CREATE TABLE IF NOT EXISTS appointment_followups (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    followup_date DATE NOT NULL,
    followup_type ENUM('PHONE_CALL', 'EMAIL', 'VISIT', 'SMS') NOT NULL,
    status ENUM('PENDING', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    notes TEXT,
    performed_by BIGINT,
    completed_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE
);

-- Tabla de disponibilidad de veterinarios
CREATE TABLE IF NOT EXISTS veterinarian_availability (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    veterinarian_id BIGINT NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    break_start_time TIME,
    break_end_time TIME,
    max_appointments INT DEFAULT 16,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_vet_date (veterinarian_id, date)
);

-- Tabla de bloqueos de horarios
CREATE TABLE IF NOT EXISTS schedule_blocks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    veterinarian_id BIGINT,
    start_datetime TIMESTAMP NOT NULL,
    end_datetime TIMESTAMP NOT NULL,
    block_type ENUM('VACATION', 'SICK_LEAVE', 'TRAINING', 'MEETING', 'PERSONAL', 'EMERGENCY') NOT NULL,
    title VARCHAR(255),
    description TEXT,
    is_recurring BOOLEAN DEFAULT FALSE,
    recurrence_pattern VARCHAR(100),
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar tipos de citas por defecto
INSERT IGNORE INTO appointment_types (name, description, duration_minutes, base_price, color_code) VALUES 
('Consulta General', 'Consulta veterinaria general', 30, 45.00, '#4CAF50'),
('Vacunación', 'Aplicación de vacunas', 15, 25.00, '#2196F3'),
('Cirugía Menor', 'Procedimientos quirúrgicos menores', 60, 150.00, '#FF9800'),
('Cirugía Mayor', 'Procedimientos quirúrgicos mayores', 120, 350.00, '#F44336'),
('Emergencia', 'Consulta de emergencia', 30, 80.00, '#E91E63'),
('Control Post-operatorio', 'Control después de cirugía', 20, 30.00, '#9C27B0'),
('Limpieza Dental', 'Limpieza y cuidado dental', 45, 75.00, '#00BCD4'),
('Radiografía', 'Estudios radiológicos', 30, 60.00, '#795548'),
('Análisis de Laboratorio', 'Toma de muestras para análisis', 15, 35.00, '#607D8B'),
('Desparasitación', 'Tratamiento antiparasitario', 15, 20.00, '#8BC34A');

-- Insertar citas de ejemplo
INSERT IGNORE INTO appointments (appointment_number, patient_id, client_id, veterinarian_id, appointment_type_id, appointment_date, appointment_time, reason, status) VALUES 
('APP001', 1, 1, 1, 1, '2024-02-15', '09:00:00', 'Revisión anual', 'SCHEDULED'),
('APP002', 2, 1, 2, 2, '2024-02-15', '10:30:00', 'Vacuna anual', 'SCHEDULED'),
('APP003', 3, 2, 1, 1, '2024-02-16', '14:00:00', 'Revisión general', 'CONFIRMED'),
('APP004', 4, 3, 2, 7, '2024-02-17', '11:00:00', 'Limpieza dental', 'SCHEDULED'),
('APP005', 5, 4, 1, 9, '2024-02-18', '16:00:00', 'Análisis de sangre', 'SCHEDULED');

-- Insertar recordatorios programados
INSERT IGNORE INTO appointment_reminders (appointment_id, reminder_type, reminder_time, message) VALUES 
(1, 'EMAIL', '2024-02-14 18:00:00', 'Recordatorio: Cita veterinaria mañana a las 09:00'),
(1, 'SMS', '2024-02-15 08:00:00', 'Cita hoy 09:00 - Clínica Veterinaria'),
(2, 'EMAIL', '2024-02-14 18:00:00', 'Recordatorio: Vacunación programada mañana'),
(3, 'SMS', '2024-02-15 19:00:00', 'Cita confirmada mañana 14:00'),
(4, 'EMAIL', '2024-02-16 18:00:00', 'Recordatorio: Limpieza dental programada');

-- Insertar disponibilidad de veterinarios para la próxima semana
INSERT IGNORE INTO veterinarian_availability (veterinarian_id, date, start_time, end_time, break_start_time, break_end_time, max_appointments) VALUES 
(1, '2024-02-15', '08:00:00', '17:00:00', '13:00:00', '14:00:00', 16),
(1, '2024-02-16', '08:00:00', '17:00:00', '13:00:00', '14:00:00', 16),
(1, '2024-02-17', '08:00:00', '15:00:00', '12:00:00', '13:00:00', 12),
(2, '2024-02-15', '09:00:00', '18:00:00', '13:30:00', '14:30:00', 16),
(2, '2024-02-16', '09:00:00', '18:00:00', '13:30:00', '14:30:00', 16),
(2, '2024-02-17', '09:00:00', '16:00:00', '13:00:00', '14:00:00', 12);

SET FOREIGN_KEY_CHECKS = 1; 