-- =====================================================
-- VETERINARY CLINIC - REPORT SERVICE DATABASE
-- =====================================================

USE report_db;

-- Crear usuario adicional
CREATE USER IF NOT EXISTS 'vet_user'@'%' IDENTIFIED BY 'vet_password';
GRANT ALL PRIVILEGES ON report_db.* TO 'vet_user'@'%';
FLUSH PRIVILEGES;

-- Configuraciones de base de datos
SET FOREIGN_KEY_CHECKS = 0;

-- Tabla de tipos de reportes
CREATE TABLE IF NOT EXISTS report_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    category ENUM('FINANCIAL', 'MEDICAL', 'ADMINISTRATIVE', 'STATISTICAL', 'OPERATIONAL') NOT NULL,
    template_path VARCHAR(500),
    parameters JSON,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de reportes generados
CREATE TABLE IF NOT EXISTS reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_number VARCHAR(20) UNIQUE NOT NULL,
    report_type_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    generated_by BIGINT NOT NULL,
    generation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    start_date DATE,
    end_date DATE,
    status ENUM('GENERATING', 'COMPLETED', 'FAILED', 'CANCELLED') DEFAULT 'GENERATING',
    file_path VARCHAR(500),
    file_name VARCHAR(255),
    file_size BIGINT,
    format ENUM('PDF', 'EXCEL', 'CSV', 'HTML') DEFAULT 'PDF',
    parameters JSON,
    execution_time_ms BIGINT,
    error_message TEXT,
    download_count INT DEFAULT 0,
    last_downloaded_at TIMESTAMP NULL,
    is_scheduled BOOLEAN DEFAULT FALSE,
    schedule_cron VARCHAR(100),
    next_execution TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (report_type_id) REFERENCES report_types(id)
);

-- Tabla de métricas del negocio
CREATE TABLE IF NOT EXISTS business_metrics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    metric_date DATE NOT NULL,
    total_appointments INT DEFAULT 0,
    completed_appointments INT DEFAULT 0,
    cancelled_appointments INT DEFAULT 0,
    new_clients INT DEFAULT 0,
    new_patients INT DEFAULT 0,
    total_revenue DECIMAL(12,2) DEFAULT 0.00,
    appointment_revenue DECIMAL(12,2) DEFAULT 0.00,
    medication_revenue DECIMAL(12,2) DEFAULT 0.00,
    procedure_revenue DECIMAL(12,2) DEFAULT 0.00,
    total_expenses DECIMAL(12,2) DEFAULT 0.00,
    staff_expenses DECIMAL(12,2) DEFAULT 0.00,
    medication_expenses DECIMAL(12,2) DEFAULT 0.00,
    equipment_expenses DECIMAL(12,2) DEFAULT 0.00,
    overhead_expenses DECIMAL(12,2) DEFAULT 0.00,
    profit_margin DECIMAL(5,2) DEFAULT 0.00,
    client_satisfaction_score DECIMAL(3,2) DEFAULT 0.00,
    average_wait_time_minutes INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_metric_date (metric_date)
);

-- Tabla de reportes financieros
CREATE TABLE IF NOT EXISTS financial_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_id BIGINT UNIQUE NOT NULL,
    report_period ENUM('DAILY', 'WEEKLY', 'MONTHLY', 'QUARTERLY', 'YEARLY') NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_income DECIMAL(12,2) DEFAULT 0.00,
    consultation_income DECIMAL(12,2) DEFAULT 0.00,
    procedure_income DECIMAL(12,2) DEFAULT 0.00,
    medication_income DECIMAL(12,2) DEFAULT 0.00,
    total_expenses DECIMAL(12,2) DEFAULT 0.00,
    staff_costs DECIMAL(12,2) DEFAULT 0.00,
    medication_costs DECIMAL(12,2) DEFAULT 0.00,
    equipment_costs DECIMAL(12,2) DEFAULT 0.00,
    utilities_costs DECIMAL(12,2) DEFAULT 0.00,
    other_expenses DECIMAL(12,2) DEFAULT 0.00,
    gross_profit DECIMAL(12,2) DEFAULT 0.00,
    net_profit DECIMAL(12,2) DEFAULT 0.00,
    profit_margin_percentage DECIMAL(5,2) DEFAULT 0.00,
    tax_amount DECIMAL(12,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (report_id) REFERENCES reports(id) ON DELETE CASCADE
);

-- Tabla de reportes médicos/estadísticos
CREATE TABLE IF NOT EXISTS medical_statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_id BIGINT UNIQUE NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    total_consultations INT DEFAULT 0,
    emergency_consultations INT DEFAULT 0,
    routine_consultations INT DEFAULT 0,
    surgery_procedures INT DEFAULT 0,
    vaccination_procedures INT DEFAULT 0,
    dental_procedures INT DEFAULT 0,
    most_common_diagnosis VARCHAR(255),
    most_common_treatment VARCHAR(255),
    average_consultation_duration_minutes INT DEFAULT 0,
    patient_recovery_rate DECIMAL(5,2) DEFAULT 0.00,
    complication_rate DECIMAL(5,2) DEFAULT 0.00,
    readmission_rate DECIMAL(5,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (report_id) REFERENCES reports(id) ON DELETE CASCADE
);

-- Tabla de reportes de inventario
CREATE TABLE IF NOT EXISTS inventory_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_id BIGINT UNIQUE NOT NULL,
    report_date DATE NOT NULL,
    total_medications INT DEFAULT 0,
    low_stock_items INT DEFAULT 0,
    expired_items INT DEFAULT 0,
    near_expiry_items INT DEFAULT 0,
    total_inventory_value DECIMAL(12,2) DEFAULT 0.00,
    monthly_consumption_value DECIMAL(12,2) DEFAULT 0.00,
    waste_value DECIMAL(12,2) DEFAULT 0.00,
    inventory_turnover_rate DECIMAL(5,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (report_id) REFERENCES reports(id) ON DELETE CASCADE
);

-- Insertar tipos de reportes predefinidos
INSERT IGNORE INTO report_types (name, description, category, parameters) VALUES 
('Reporte Financiero Mensual', 'Resumen de ingresos y gastos mensuales', 'FINANCIAL', '{"period": "monthly", "include_tax": true}'),
('Reporte de Citas Diarias', 'Listado de citas del día con estado', 'ADMINISTRATIVE', '{"include_details": true, "group_by_veterinarian": true}'),
('Estadísticas Médicas', 'Análisis de diagnósticos y tratamientos más comunes', 'MEDICAL', '{"include_success_rate": true, "group_by_diagnosis": true}'),
('Reporte de Inventario', 'Estado actual del inventario de medicamentos', 'OPERATIONAL', '{"include_expired": true, "alert_low_stock": true}'),
('Reporte de Nuevos Clientes', 'Clientes registrados en el período', 'ADMINISTRATIVE', '{"include_pets": true, "group_by_month": true}'),
('Análisis de Rentabilidad', 'Análisis de rentabilidad por servicio', 'FINANCIAL', '{"include_costs": true, "group_by_service": true}'),
('Reporte de Vacunaciones', 'Control de vacunas aplicadas y pendientes', 'MEDICAL', '{"include_pending": true, "group_by_vaccine": true}'),
('Estadísticas de Satisfacción', 'Métricas de satisfacción del cliente', 'STATISTICAL', '{"include_comments": true, "rating_breakdown": true}');

-- Insertar métricas de negocio de ejemplo (últimos 30 días)
INSERT IGNORE INTO business_metrics (metric_date, total_appointments, completed_appointments, cancelled_appointments, new_clients, new_patients, total_revenue, appointment_revenue, medication_revenue) VALUES 
('2024-01-15', 12, 10, 2, 2, 3, 450.00, 300.00, 150.00),
('2024-01-16', 15, 13, 2, 1, 1, 520.00, 380.00, 140.00),
('2024-01-17', 18, 16, 2, 3, 4, 680.00, 500.00, 180.00),
('2024-01-18', 14, 12, 2, 1, 2, 490.00, 340.00, 150.00),
('2024-01-19', 16, 14, 2, 2, 2, 575.00, 425.00, 150.00),
('2024-01-20', 10, 9, 1, 1, 1, 385.00, 285.00, 100.00),
('2024-01-21', 8, 7, 1, 0, 0, 290.00, 220.00, 70.00);

-- Insertar reportes de ejemplo
INSERT IGNORE INTO reports (report_number, report_type_id, title, generated_by, generation_date, start_date, end_date, status, format, file_name) VALUES 
('RPT001', 1, 'Reporte Financiero Enero 2024', 1, '2024-02-01 09:00:00', '2024-01-01', '2024-01-31', 'COMPLETED', 'PDF', 'reporte_financiero_enero_2024.pdf'),
('RPT002', 3, 'Estadísticas Médicas Q1 2024', 1, '2024-02-05 14:30:00', '2024-01-01', '2024-01-31', 'COMPLETED', 'EXCEL', 'estadisticas_medicas_q1_2024.xlsx'),
('RPT003', 4, 'Inventario Febrero 2024', 2, '2024-02-10 11:15:00', '2024-02-01', '2024-02-10', 'COMPLETED', 'PDF', 'inventario_febrero_2024.pdf'),
('RPT004', 2, 'Citas del día 15/02/2024', 3, '2024-02-15 08:00:00', '2024-02-15', '2024-02-15', 'COMPLETED', 'CSV', 'citas_15_febrero_2024.csv'),
('RPT005', 5, 'Nuevos Clientes Enero 2024', 1, '2024-02-01 16:45:00', '2024-01-01', '2024-01-31', 'COMPLETED', 'PDF', 'nuevos_clientes_enero_2024.pdf');

-- Insertar datos financieros de ejemplo
INSERT IGNORE INTO financial_reports (report_id, report_period, start_date, end_date, total_income, consultation_income, medication_income, total_expenses, staff_costs, gross_profit, net_profit) VALUES 
(1, 'MONTHLY', '2024-01-01', '2024-01-31', 15750.00, 11250.00, 4500.00, 8900.00, 6500.00, 6850.00, 5120.00);

-- Insertar estadísticas médicas de ejemplo
INSERT IGNORE INTO medical_statistics (report_id, period_start, period_end, total_consultations, emergency_consultations, routine_consultations, surgery_procedures, vaccination_procedures, most_common_diagnosis, average_consultation_duration_minutes) VALUES 
(2, '2024-01-01', '2024-01-31', 285, 45, 240, 12, 38, 'Otitis Externa', 35);

SET FOREIGN_KEY_CHECKS = 1; 