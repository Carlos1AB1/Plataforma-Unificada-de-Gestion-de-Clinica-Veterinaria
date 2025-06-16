-- =====================================================
-- VETERINARY CLINIC - MEDICAL HISTORY SERVICE DATABASE
-- =====================================================

USE medical_history_db;

-- Crear usuario adicional
CREATE USER IF NOT EXISTS 'vet_user'@'%' IDENTIFIED BY 'vet_password';
GRANT ALL PRIVILEGES ON medical_history_db.* TO 'vet_user'@'%';
FLUSH PRIVILEGES;

-- Configuraciones de base de datos
SET FOREIGN_KEY_CHECKS = 0;

-- Tabla de diagnósticos
CREATE TABLE IF NOT EXISTS diagnoses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    severity ENUM('MILD', 'MODERATE', 'SEVERE', 'CRITICAL') DEFAULT 'MODERATE',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de historiales médicos
CREATE TABLE IF NOT EXISTS medical_histories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    appointment_id BIGINT,
    visit_date DATE NOT NULL,
    veterinarian_id BIGINT NOT NULL,
    chief_complaint TEXT,
    history_of_present_illness TEXT,
    physical_examination TEXT,
    vital_signs JSON,
    weight DECIMAL(5,2),
    temperature DECIMAL(4,1),
    heart_rate INT,
    respiratory_rate INT,
    blood_pressure VARCHAR(20),
    assessment TEXT,
    plan TEXT,
    follow_up_instructions TEXT,
    next_visit_date DATE,
    status ENUM('DRAFT', 'COMPLETED', 'REVIEWED') DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de diagnósticos por visita
CREATE TABLE IF NOT EXISTS medical_history_diagnoses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medical_history_id BIGINT NOT NULL,
    diagnosis_id BIGINT NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (medical_history_id) REFERENCES medical_histories(id) ON DELETE CASCADE,
    FOREIGN KEY (diagnosis_id) REFERENCES diagnoses(id)
);

-- Tabla de tratamientos aplicados
CREATE TABLE IF NOT EXISTS treatments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medical_history_id BIGINT NOT NULL,
    treatment_name VARCHAR(255) NOT NULL,
    treatment_type ENUM('MEDICATION', 'PROCEDURE', 'SURGERY', 'THERAPY', 'DIETARY', 'OTHER') NOT NULL,
    description TEXT,
    dosage VARCHAR(100),
    frequency VARCHAR(100),
    duration VARCHAR(100),
    start_date DATE,
    end_date DATE,
    instructions TEXT,
    cost DECIMAL(8,2),
    status ENUM('PRESCRIBED', 'IN_PROGRESS', 'COMPLETED', 'DISCONTINUED') DEFAULT 'PRESCRIBED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (medical_history_id) REFERENCES medical_histories(id) ON DELETE CASCADE
);

-- Tabla de archivos médicos (radiografías, análisis, etc.)
CREATE TABLE IF NOT EXISTS medical_files (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medical_history_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_type ENUM('IMAGE', 'PDF', 'LAB_RESULT', 'XRAY', 'ULTRASOUND', 'VIDEO', 'OTHER') NOT NULL,
    file_size BIGINT,
    mime_type VARCHAR(100),
    description TEXT,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    uploaded_by BIGINT,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (medical_history_id) REFERENCES medical_histories(id) ON DELETE CASCADE
);

-- Tabla de alergias del paciente
CREATE TABLE IF NOT EXISTS patient_allergies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    allergen VARCHAR(255) NOT NULL,
    allergy_type ENUM('FOOD', 'MEDICATION', 'ENVIRONMENTAL', 'CONTACT', 'OTHER') NOT NULL,
    severity ENUM('MILD', 'MODERATE', 'SEVERE', 'ANAPHYLACTIC') DEFAULT 'MODERATE',
    symptoms TEXT,
    onset_date DATE,
    notes TEXT,
    discovered_by BIGINT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de medicamentos actuales del paciente
CREATE TABLE IF NOT EXISTS patient_current_medications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    medication_name VARCHAR(255) NOT NULL,
    dosage VARCHAR(100),
    frequency VARCHAR(100),
    start_date DATE NOT NULL,
    end_date DATE,
    prescribing_veterinarian BIGINT,
    reason TEXT,
    instructions TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insertar diagnósticos comunes
INSERT IGNORE INTO diagnoses (code, name, description, category, severity) VALUES 
('D001', 'Otitis Externa', 'Inflamación del oído externo', 'Dermatología', 'MILD'),
('D002', 'Gastroenteritis', 'Inflamación del tracto gastrointestinal', 'Gastroenterología', 'MODERATE'),
('D003', 'Fractura de Hueso', 'Rotura o fisura en el hueso', 'Traumatología', 'SEVERE'),
('D004', 'Conjuntivitis', 'Inflamación de la conjuntiva ocular', 'Oftalmología', 'MILD'),
('D005', 'Diabetes Mellitus', 'Trastorno metabólico de la glucosa', 'Endocrinología', 'MODERATE'),
('D006', 'Insuficiencia Renal', 'Deterioro de la función renal', 'Nefrología', 'SEVERE'),
('D007', 'Artritis', 'Inflamación de las articulaciones', 'Reumatología', 'MODERATE'),
('D008', 'Dermatitis Alérgica', 'Reacción alérgica en la piel', 'Dermatología', 'MILD'),
('D009', 'Cardiomiopatía', 'Enfermedad del músculo cardíaco', 'Cardiología', 'SEVERE'),
('D010', 'Parasitosis Intestinal', 'Infestación por parásitos intestinales', 'Parasitología', 'MODERATE');

-- Insertar historiales médicos de ejemplo
INSERT IGNORE INTO medical_histories (patient_id, appointment_id, visit_date, veterinarian_id, chief_complaint, physical_examination, weight, temperature, assessment, plan) VALUES 
(1, 1, '2024-01-15', 1, 'El propietario reporta que el perro se rasca mucho las orejas', 'Examen físico normal excepto por enrojecimiento en oído derecho', 28.5, 38.7, 'Otitis externa leve en oído derecho', 'Limpieza auricular y antibiótico tópico'),
(2, 2, '2024-01-20', 2, 'Vacunación anual programada', 'Examen físico completo - todo normal', 4.2, 38.5, 'Paciente sano para vacunación', 'Aplicar vacuna trivalente felina'),
(3, 3, '2024-01-25', 1, 'Cojera en pata trasera izquierda desde hace 3 días', 'Dolor y ligera inflamación en articulación de la rodilla', 32.0, 39.1, 'Posible artritis o lesión ligamentaria', 'Radiografías y tratamiento antiinflamatorio'),
(4, 4, '2024-02-01', 2, 'Mal aliento y dificultad para comer', 'Acumulación significativa de sarro dental', 3.8, 38.3, 'Enfermedad periodontal moderada', 'Limpieza dental bajo anestesia'),
(5, 5, '2024-02-05', 1, 'Revisión post-operatoria de esterilización', 'Herida quirúrgica sanando bien', 15.2, 38.4, 'Recuperación normal post-cirugía', 'Continuar cuidados en casa');

-- Insertar diagnósticos por visita
INSERT IGNORE INTO medical_history_diagnoses (medical_history_id, diagnosis_id, is_primary, notes) VALUES 
(1, 1, TRUE, 'Otitis externa leve, responde bien al tratamiento'),
(2, NULL, FALSE, 'Vacunación preventiva - no hay diagnóstico'),
(3, 7, TRUE, 'Artritis leve en articulación de la rodilla'),
(4, NULL, TRUE, 'Enfermedad periodontal moderada'),
(5, NULL, FALSE, 'Control post-operatorio normal');

-- Insertar tratamientos
INSERT IGNORE INTO treatments (medical_history_id, treatment_name, treatment_type, description, dosage, frequency, duration, start_date, instructions, cost, status) VALUES 
(1, 'Limpieza Auricular + Antibiótico', 'MEDICATION', 'Limpieza diaria del oído y aplicación de gotas antibióticas', '3 gotas', 'Cada 12 horas', '7 días', '2024-01-15', 'Limpiar oído antes de aplicar medicación', 35.50, 'COMPLETED'),
(2, 'Vacuna Trivalente Felina', 'PROCEDURE', 'Vacunación anual preventiva', '1 dosis', 'Una vez', 'N/A', '2024-01-20', 'Observar por reacciones alérgicas 24h', 25.00, 'COMPLETED'),
(3, 'Antiinflamatorio', 'MEDICATION', 'Tratamiento para reducir inflamación articular', '50mg', 'Cada 24 horas', '10 días', '2024-01-25', 'Administrar con comida', 28.00, 'IN_PROGRESS'),
(4, 'Limpieza Dental', 'PROCEDURE', 'Limpieza dental bajo anestesia general', 'N/A', 'Una vez', 'N/A', '2024-02-01', 'Ayuno 12h antes del procedimiento', 150.00, 'COMPLETED'),
(5, 'Cuidados Post-operatorios', 'THERAPY', 'Seguimiento de cicatrización', 'N/A', 'Diario', '10 días', '2024-02-05', 'Mantener herida limpia y seca', 0.00, 'IN_PROGRESS');

-- Insertar alergias de ejemplo
INSERT IGNORE INTO patient_allergies (patient_id, allergen, allergy_type, severity, symptoms, onset_date) VALUES 
(1, 'Pollo', 'FOOD', 'MODERATE', 'Picazón y enrojecimiento de la piel', '2023-06-15'),
(3, 'Penicilina', 'MEDICATION', 'SEVERE', 'Vómitos y diarrea', '2022-03-10'),
(4, 'Ácaros del polvo', 'ENVIRONMENTAL', 'MILD', 'Estornudos frecuentes', '2023-11-20');

-- Insertar medicamentos actuales
INSERT IGNORE INTO patient_current_medications (patient_id, medication_name, dosage, frequency, start_date, prescribing_veterinarian, reason, is_active) VALUES 
(3, 'Carprofeno', '50mg', 'Cada 24 horas', '2024-01-25', 1, 'Control del dolor articular', TRUE),
(5, 'Antibiótico post-quirúrgico', '250mg', 'Cada 12 horas', '2024-02-05', 1, 'Prevención de infección post-operatoria', TRUE);

SET FOREIGN_KEY_CHECKS = 1; 