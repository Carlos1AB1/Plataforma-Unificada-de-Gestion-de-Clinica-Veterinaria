-- =====================================================
-- VETERINARY CLINIC - PATIENT SERVICE DATABASE
-- =====================================================

USE veterinary_patient_db;

-- Crear usuario adicional
CREATE USER IF NOT EXISTS 'vet_user'@'%' IDENTIFIED BY 'vet_password';
GRANT ALL PRIVILEGES ON veterinary_patient_db.* TO 'vet_user'@'%';
FLUSH PRIVILEGES;

-- Configuraciones de base de datos
SET FOREIGN_KEY_CHECKS = 0;

-- Tabla de especies
CREATE TABLE IF NOT EXISTS species (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    scientific_name VARCHAR(100),
    common_names TEXT,
    average_lifespan INT,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de razas
CREATE TABLE IF NOT EXISTS breeds (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    species_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    average_weight_min DECIMAL(5,2),
    average_weight_max DECIMAL(5,2),
    average_height_min DECIMAL(5,2),
    average_height_max DECIMAL(5,2),
    temperament TEXT,
    special_care_notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (species_id) REFERENCES species(id),
    UNIQUE KEY unique_breed_per_species (species_id, name)
);

-- Tabla de pacientes (mascotas)
CREATE TABLE IF NOT EXISTS patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_number VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    species_id BIGINT NOT NULL,
    breed_id BIGINT,
    client_id BIGINT NOT NULL,
    date_of_birth DATE,
    gender ENUM('MALE', 'FEMALE', 'UNKNOWN') NOT NULL,
    weight DECIMAL(5,2),
    height DECIMAL(5,2),
    color VARCHAR(100),
    markings TEXT,
    microchip_number VARCHAR(50) UNIQUE,
    is_neutered BOOLEAN DEFAULT FALSE,
    neutered_date DATE,
    registration_date DATE DEFAULT (CURRENT_DATE),
    status ENUM('ACTIVE', 'INACTIVE', 'DECEASED') DEFAULT 'ACTIVE',
    death_date DATE,
    death_cause TEXT,
    allergies TEXT,
    medications TEXT,
    special_notes TEXT,
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    insurance_provider VARCHAR(100),
    insurance_policy_number VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (species_id) REFERENCES species(id),
    FOREIGN KEY (breed_id) REFERENCES breeds(id)
);

-- Tabla de vacunas aplicadas
CREATE TABLE IF NOT EXISTS patient_vaccinations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    vaccine_name VARCHAR(100) NOT NULL,
    vaccine_batch VARCHAR(50),
    vaccination_date DATE NOT NULL,
    next_due_date DATE,
    veterinarian_id BIGINT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- Tabla de pesos históricos
CREATE TABLE IF NOT EXISTS patient_weight_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    weight DECIMAL(5,2) NOT NULL,
    measurement_date DATE NOT NULL,
    measured_by BIGINT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- Insertar especies básicas
INSERT IGNORE INTO species (name, scientific_name, average_lifespan) VALUES 
('Perro', 'Canis lupus familiaris', 13),
('Gato', 'Felis catus', 15),
('Conejo', 'Oryctolagus cuniculus', 9),
('Cobaya', 'Cavia porcellus', 6),
('Hurón', 'Mustela putorius furo', 8),
('Ave', 'Aves', 10),
('Reptil', 'Reptilia', 15),
('Pez', 'Actinopterygii', 5);

-- Insertar razas comunes de perros
INSERT IGNORE INTO breeds (species_id, name, average_weight_min, average_weight_max) VALUES 
(1, 'Golden Retriever', 25.0, 35.0),
(1, 'Labrador Retriever', 25.0, 36.0),
(1, 'Pastor Alemán', 22.0, 40.0),
(1, 'Bulldog Francés', 8.0, 14.0),
(1, 'Chihuahua', 1.5, 3.0),
(1, 'Yorkshire Terrier', 2.0, 3.5),
(1, 'Mestizo', 5.0, 50.0);

-- Insertar razas comunes de gatos
INSERT IGNORE INTO breeds (species_id, name, average_weight_min, average_weight_max) VALUES 
(2, 'Persa', 3.0, 7.0),
(2, 'Siamés', 3.0, 5.0),
(2, 'Maine Coon', 4.0, 8.0),
(2, 'Británico de Pelo Corto', 4.0, 8.0),
(2, 'Mestizo', 3.0, 7.0);

-- Insertar pacientes de ejemplo
INSERT IGNORE INTO patients (patient_number, name, species_id, breed_id, client_id, date_of_birth, gender, weight, color, microchip_number) VALUES 
('PAT001', 'Max', 1, 1, 1, '2020-03-15', 'MALE', 28.5, 'Dorado', '123456789012345'),
('PAT002', 'Luna', 2, 8, 1, '2019-07-22', 'FEMALE', 4.2, 'Gris y blanco', '234567890123456'),
('PAT003', 'Rocky', 1, 3, 2, '2018-11-10', 'MALE', 32.0, 'Negro y marrón', '345678901234567'),
('PAT004', 'Mia', 2, 9, 3, '2021-01-05', 'FEMALE', 3.8, 'Atigrado', '456789012345678'),
('PAT005', 'Buddy', 1, 7, 4, '2020-06-18', 'MALE', 15.5, 'Mestizo marrón', '567890123456789'),
('PAT006', 'Whiskers', 2, 10, 5, '2019-12-03', 'MALE', 5.1, 'Blanco y negro', '678901234567890');

-- Insertar vacunaciones de ejemplo
INSERT IGNORE INTO patient_vaccinations (patient_id, vaccine_name, vaccination_date, next_due_date, veterinarian_id) VALUES 
(1, 'Polivalente Canina', '2023-01-15', '2024-01-15', 1),
(1, 'Antirrábica', '2023-01-15', '2024-01-15', 1),
(2, 'Trivalente Felina', '2023-02-10', '2024-02-10', 2),
(3, 'Polivalente Canina', '2023-03-05', '2024-03-05', 1),
(4, 'Trivalente Felina', '2023-04-12', '2024-04-12', 2),
(5, 'Polivalente Canina', '2023-05-20', '2024-05-20', 1);

-- Insertar historial de pesos
INSERT IGNORE INTO patient_weight_history (patient_id, weight, measurement_date, measured_by) VALUES 
(1, 26.0, '2023-01-15', 1),
(1, 27.5, '2023-06-15', 1),
(1, 28.5, '2023-12-15', 1),
(2, 3.8, '2023-02-10', 2),
(2, 4.0, '2023-08-10', 2),
(2, 4.2, '2023-12-10', 2);

SET FOREIGN_KEY_CHECKS = 1; 