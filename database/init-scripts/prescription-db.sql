-- =====================================================
-- VETERINARY CLINIC - PRESCRIPTION SERVICE DATABASE
-- =====================================================

USE prescription_db;

-- Crear usuario adicional
CREATE USER IF NOT EXISTS 'vet_user'@'%' IDENTIFIED BY 'vet_password';
GRANT ALL PRIVILEGES ON prescription_db.* TO 'vet_user'@'%';
FLUSH PRIVILEGES;

-- Configuraciones de base de datos
SET FOREIGN_KEY_CHECKS = 0;

-- Tabla de medicamentos
CREATE TABLE IF NOT EXISTS medications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    generic_name VARCHAR(255),
    brand_name VARCHAR(255),
    medication_type ENUM('ANTIBIOTIC', 'ANTI_INFLAMMATORY', 'PAIN_KILLER', 'VITAMIN', 'VACCINE', 'ANTISEPTIC', 'DEWORMER', 'HORMONE', 'OTHER') NOT NULL,
    form ENUM('TABLET', 'CAPSULE', 'LIQUID', 'INJECTION', 'TOPICAL', 'POWDER', 'DROPS', 'SPRAY') NOT NULL,
    strength VARCHAR(50),
    unit VARCHAR(20),
    description TEXT,
    contraindications TEXT,
    side_effects TEXT,
    storage_conditions TEXT,
    requires_prescription BOOLEAN DEFAULT TRUE,
    is_controlled_substance BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de prescripciones
CREATE TABLE IF NOT EXISTS prescriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    prescription_number VARCHAR(20) UNIQUE NOT NULL,
    patient_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    veterinarian_id BIGINT NOT NULL,
    appointment_id BIGINT,
    medical_history_id BIGINT,
    prescription_date DATE NOT NULL,
    status ENUM('DRAFT', 'ACTIVE', 'COMPLETED', 'CANCELLED', 'EXPIRED') DEFAULT 'DRAFT',
    total_cost DECIMAL(10,2) DEFAULT 0.00,
    notes TEXT,
    special_instructions TEXT,
    follow_up_required BOOLEAN DEFAULT FALSE,
    follow_up_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de elementos de prescripción (medicamentos prescritos)
CREATE TABLE IF NOT EXISTS prescription_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    prescription_id BIGINT NOT NULL,
    medication_id BIGINT NOT NULL,
    quantity DECIMAL(8,2) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    dosage VARCHAR(100) NOT NULL,
    frequency VARCHAR(100) NOT NULL,
    duration VARCHAR(100),
    route_of_administration ENUM('ORAL', 'TOPICAL', 'INJECTION', 'INTRAVENOUS', 'SUBCUTANEOUS', 'INTRAMUSCULAR', 'EYE_DROPS', 'EAR_DROPS', 'OTHER') NOT NULL,
    instructions TEXT,
    start_date DATE,
    end_date DATE,
    refills_allowed INT DEFAULT 0,
    refills_used INT DEFAULT 0,
    unit_price DECIMAL(8,2),
    total_price DECIMAL(10,2),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (prescription_id) REFERENCES prescriptions(id) ON DELETE CASCADE,
    FOREIGN KEY (medication_id) REFERENCES medications(id)
);

-- Tabla de dispensaciones
CREATE TABLE IF NOT EXISTS dispensations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    prescription_item_id BIGINT NOT NULL,
    dispensed_quantity DECIMAL(8,2) NOT NULL,
    dispensed_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dispensed_by BIGINT,
    batch_number VARCHAR(50),
    expiration_date DATE,
    cost DECIMAL(8,2),
    notes TEXT,
    FOREIGN KEY (prescription_item_id) REFERENCES prescription_items(id) ON DELETE CASCADE
);

-- Tabla de inventario de medicamentos
CREATE TABLE IF NOT EXISTS medication_inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medication_id BIGINT NOT NULL,
    batch_number VARCHAR(50) NOT NULL,
    quantity_in_stock DECIMAL(10,2) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    cost_per_unit DECIMAL(8,2),
    sale_price_per_unit DECIMAL(8,2),
    supplier VARCHAR(255),
    purchase_date DATE,
    expiration_date DATE,
    storage_location VARCHAR(100),
    minimum_stock_level DECIMAL(8,2) DEFAULT 10.00,
    is_expired BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (medication_id) REFERENCES medications(id),
    UNIQUE KEY unique_medication_batch (medication_id, batch_number)
);

-- Tabla de alertas de inventario
CREATE TABLE IF NOT EXISTS inventory_alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medication_id BIGINT NOT NULL,
    alert_type ENUM('LOW_STOCK', 'EXPIRED', 'NEAR_EXPIRY') NOT NULL,
    alert_message TEXT NOT NULL,
    current_stock DECIMAL(10,2),
    minimum_required DECIMAL(10,2),
    expiration_date DATE,
    is_resolved BOOLEAN DEFAULT FALSE,
    resolved_at TIMESTAMP NULL,
    resolved_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (medication_id) REFERENCES medications(id)
);

-- Insertar medicamentos comunes
INSERT IGNORE INTO medications (name, generic_name, medication_type, form, strength, unit, description, requires_prescription) VALUES 
('Amoxicilina 250mg', 'Amoxicilina', 'ANTIBIOTIC', 'TABLET', '250', 'mg', 'Antibiótico de amplio espectro para infecciones bacterianas', TRUE),
('Carprofeno 75mg', 'Carprofeno', 'ANTI_INFLAMMATORY', 'TABLET', '75', 'mg', 'Antiinflamatorio no esteroideo para control del dolor', TRUE),
('Metacam 1.5mg/ml', 'Meloxicam', 'ANTI_INFLAMMATORY', 'LIQUID', '1.5', 'mg/ml', 'Antiinflamatorio líquido para administración oral', TRUE),
('Frontline Spray', 'Fipronil', 'OTHER', 'SPRAY', '0.25', '%', 'Antiparasitario externo para pulgas y garrapatas', FALSE),
('Drontal Plus', 'Praziquantel + Pirantel', 'DEWORMER', 'TABLET', 'Variable', 'mg', 'Desparasitante interno de amplio espectro', TRUE),
('Gentamicina Gotas', 'Gentamicina', 'ANTIBIOTIC', 'DROPS', '3', 'mg/ml', 'Antibiótico en gotas para infecciones oculares y auditivas', TRUE),
('Prednisona 5mg', 'Prednisona', 'HORMONE', 'TABLET', '5', 'mg', 'Corticosteroide para procesos inflamatorios', TRUE),
('Vitamina B Complex', 'Complejo B', 'VITAMIN', 'INJECTION', '100', 'mg/ml', 'Suplemento vitamínico inyectable', FALSE),
('Betadine Solución', 'Povidona Yodada', 'ANTISEPTIC', 'LIQUID', '10', '%', 'Antiséptico para limpieza de heridas', FALSE),
('Tramadol 50mg', 'Tramadol', 'PAIN_KILLER', 'TABLET', '50', 'mg', 'Analgésico opioide para dolor moderado a severo', TRUE);

-- Insertar inventario de medicamentos
INSERT IGNORE INTO medication_inventory (medication_id, batch_number, quantity_in_stock, unit, cost_per_unit, sale_price_per_unit, supplier, purchase_date, expiration_date, minimum_stock_level) VALUES 
(1, 'AMX2024001', 100.00, 'tablets', 0.50, 1.25, 'Laboratorios Veterinarios SA', '2024-01-15', '2025-12-31', 20.00),
(2, 'CAR2024001', 50.00, 'tablets', 1.20, 2.50, 'Medicamentos Veterinarios SL', '2024-01-20', '2025-11-30', 15.00),
(3, 'MEL2024001', 20.00, 'bottles', 8.50, 18.00, 'Productos Farmacéuticos SA', '2024-02-01', '2025-10-15', 5.00),
(4, 'FRO2024001', 30.00, 'bottles', 12.00, 25.00, 'Antiparasitarios España SL', '2024-01-10', '2026-01-10', 10.00),
(5, 'DRO2024001', 80.00, 'tablets', 2.00, 4.50, 'Laboratorios Veterinarios SA', '2024-01-25', '2025-09-30', 25.00);

-- Insertar prescripciones de ejemplo
INSERT IGNORE INTO prescriptions (prescription_number, patient_id, client_id, veterinarian_id, appointment_id, prescription_date, status, notes) VALUES 
('PRE001', 1, 1, 1, 1, '2024-01-15', 'ACTIVE', 'Tratamiento para otitis externa'),
('PRE002', 3, 2, 1, 3, '2024-01-25', 'ACTIVE', 'Tratamiento antiinflamatorio para artritis'),
('PRE003', 4, 3, 2, 4, '2024-02-01', 'COMPLETED', 'Medicación post limpieza dental'),
('PRE004', 5, 4, 1, 5, '2024-02-05', 'ACTIVE', 'Antibiótico post-operatorio'),
('PRE005', 2, 1, 2, 2, '2024-01-20', 'COMPLETED', 'Desparasitación preventiva');

-- Insertar elementos de prescripción
INSERT IGNORE INTO prescription_items (prescription_id, medication_id, quantity, unit, dosage, frequency, duration, route_of_administration, instructions, start_date, end_date, unit_price, total_price) VALUES 
(1, 6, 1, 'bottle', '3 gotas', 'Cada 12 horas', '7 días', 'EAR_DROPS', 'Limpiar oído antes de aplicar', '2024-01-15', '2024-01-22', 12.00, 12.00),
(2, 2, 10, 'tablets', '75mg', 'Cada 24 horas', '10 días', 'ORAL', 'Administrar con comida', '2024-01-25', '2024-02-04', 2.50, 25.00),
(3, 1, 14, 'tablets', '250mg', 'Cada 12 horas', '7 días', 'ORAL', 'Completar todo el tratamiento', '2024-02-01', '2024-02-08', 1.25, 17.50),
(4, 1, 20, 'tablets', '250mg', 'Cada 12 horas', '10 días', 'ORAL', 'Administrar después de cirugía', '2024-02-05', '2024-02-15', 1.25, 25.00),
(5, 5, 2, 'tablets', 'Según peso', 'Una dosis', 'Único', 'ORAL', 'Repetir en 15 días', '2024-01-20', '2024-01-20', 4.50, 9.00);

-- Insertar dispensaciones
INSERT IGNORE INTO dispensations (prescription_item_id, dispensed_quantity, dispensed_date, dispensed_by, batch_number, cost) VALUES 
(1, 1, '2024-01-15 10:30:00', 3, 'GEN2024001', 12.00),
(2, 10, '2024-01-25 14:15:00', 3, 'CAR2024001', 25.00),
(3, 14, '2024-02-01 16:45:00', 3, 'AMX2024001', 17.50),
(4, 20, '2024-02-05 11:20:00', 3, 'AMX2024001', 25.00),
(5, 2, '2024-01-20 12:00:00', 3, 'DRO2024001', 9.00);

-- Insertar alertas de inventario
INSERT IGNORE INTO inventory_alerts (medication_id, alert_type, alert_message, current_stock, minimum_required) VALUES 
(3, 'LOW_STOCK', 'Stock bajo de Metacam - Solo quedan 5 unidades', 5.00, 5.00),
(2, 'LOW_STOCK', 'Stock crítico de Carprofeno - Solo quedan 15 unidades', 15.00, 15.00);

SET FOREIGN_KEY_CHECKS = 1; 