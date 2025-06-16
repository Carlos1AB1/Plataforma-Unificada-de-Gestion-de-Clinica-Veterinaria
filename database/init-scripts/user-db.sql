-- =====================================================
-- VETERINARY CLINIC - USER SERVICE DATABASE
-- =====================================================

USE veterinary_user_db;

-- Crear usuario adicional
CREATE USER IF NOT EXISTS 'vet_user'@'%' IDENTIFIED BY 'vet_password';
GRANT ALL PRIVILEGES ON veterinary_user_db.* TO 'vet_user'@'%';
FLUSH PRIVILEGES;

-- Configuraciones de base de datos
SET FOREIGN_KEY_CHECKS = 0;

-- Tabla de usuarios del sistema
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    auth_user_id BIGINT UNIQUE NOT NULL,
    employee_id VARCHAR(20) UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    date_of_birth DATE,
    hire_date DATE,
    department VARCHAR(50),
    position VARCHAR(50),
    salary DECIMAL(10,2),
    license_number VARCHAR(50),
    specialization VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de perfiles profesionales
CREATE TABLE IF NOT EXISTS professional_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    medical_license VARCHAR(50),
    specialization VARCHAR(100),
    years_experience INT,
    education TEXT,
    certifications TEXT,
    bio TEXT,
    consultation_fee DECIMAL(8,2),
    is_available BOOLEAN DEFAULT TRUE,
    working_hours JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tabla de horarios de trabajo
CREATE TABLE IF NOT EXISTS work_schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    day_of_week ENUM('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tabla de vacaciones/ausencias
CREATE TABLE IF NOT EXISTS user_absences (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    absence_type ENUM('VACATION', 'SICK_LEAVE', 'PERSONAL', 'TRAINING', 'OTHER') NOT NULL,
    reason TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    approved_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (approved_by) REFERENCES users(id)
);

-- Insertar datos de ejemplo
INSERT IGNORE INTO users (auth_user_id, employee_id, first_name, last_name, email, phone, department, position, license_number, specialization) VALUES 
(1, 'VET001', 'Dr. María', 'González', 'maria.gonzalez@veterinary.com', '+1234567890', 'Medicina', 'Veterinario Senior', 'VET-12345', 'Medicina Interna'),
(2, 'VET002', 'Dr. Carlos', 'Rodríguez', 'carlos.rodriguez@veterinary.com', '+1234567891', 'Cirugía', 'Veterinario', 'VET-12346', 'Cirugía'),
(3, 'AST001', 'Ana', 'Martínez', 'ana.martinez@veterinary.com', '+1234567892', 'Recepción', 'Asistente', NULL, NULL);

-- Insertar perfiles profesionales de ejemplo
INSERT IGNORE INTO professional_profiles (user_id, medical_license, specialization, years_experience, consultation_fee) VALUES 
(1, 'VET-12345', 'Medicina Interna', 10, 75.00),
(2, 'VET-12346', 'Cirugía Veterinaria', 8, 90.00);

-- Insertar horarios de trabajo de ejemplo
INSERT IGNORE INTO work_schedules (user_id, day_of_week, start_time, end_time) VALUES 
(1, 'MONDAY', '08:00:00', '17:00:00'),
(1, 'TUESDAY', '08:00:00', '17:00:00'),
(1, 'WEDNESDAY', '08:00:00', '17:00:00'),
(1, 'THURSDAY', '08:00:00', '17:00:00'),
(1, 'FRIDAY', '08:00:00', '15:00:00'),
(2, 'MONDAY', '09:00:00', '18:00:00'),
(2, 'TUESDAY', '09:00:00', '18:00:00'),
(2, 'WEDNESDAY', '09:00:00', '18:00:00'),
(2, 'THURSDAY', '09:00:00', '18:00:00'),
(2, 'FRIDAY', '09:00:00', '16:00:00');

SET FOREIGN_KEY_CHECKS = 1; 