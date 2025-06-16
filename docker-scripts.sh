#!/bin/bash

# =====================================================
# VETERINARY CLINIC - DOCKER MANAGEMENT SCRIPT
# =====================================================

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para mostrar el header
show_header() {
    echo -e "${BLUE}"
    echo "======================================================"
    echo "  CLÍNICA VETERINARIA - GESTIÓN DOCKER"
    echo "======================================================"
    echo -e "${NC}"
}

# Función para mostrar ayuda
show_help() {
    echo -e "${YELLOW}Uso: $0 [COMANDO]${NC}"
    echo ""
    echo "Comandos disponibles:"
    echo -e "  ${GREEN}build${NC}        - Construir todas las imágenes"
    echo -e "  ${GREEN}start${NC}        - Iniciar todos los servicios"
    echo -e "  ${GREEN}stop${NC}         - Detener todos los servicios"
    echo -e "  ${GREEN}restart${NC}      - Reiniciar todos los servicios"
    echo -e "  ${GREEN}logs${NC}         - Ver logs de todos los servicios"
    echo -e "  ${GREEN}logs [servicio]${NC} - Ver logs de un servicio específico"
    echo -e "  ${GREEN}status${NC}       - Ver estado de los contenedores"
    echo -e "  ${GREEN}clean${NC}        - Limpiar contenedores e imágenes"
    echo -e "  ${GREEN}clean-all${NC}    - Limpieza completa (incluye volúmenes)"
    echo -e "  ${GREEN}setup${NC}        - Configuración inicial completa"
    echo -e "  ${GREEN}health${NC}       - Verificar estado de salud de servicios"
    echo -e "  ${GREEN}db-backup${NC}    - Realizar backup de todas las bases de datos"
    echo -e "  ${GREEN}db-restore${NC}   - Restaurar backup de bases de datos"
    echo ""
    echo "Servicios disponibles:"
    echo "  eureka-server, gateway-service, auth-service, user-service,"
    echo "  client-service, patient-service, appointment-service,"
    echo "  medical-history-service, prescription-service, report-service"
}

# Función para construir imágenes
build_images() {
    echo -e "${YELLOW}Construyendo todas las imágenes...${NC}"
    docker-compose build --no-cache
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Imágenes construidas exitosamente${NC}"
    else
        echo -e "${RED}✗ Error al construir las imágenes${NC}"
        exit 1
    fi
}

# Función para iniciar servicios
start_services() {
    echo -e "${YELLOW}Iniciando todos los servicios...${NC}"
    docker-compose up -d
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Servicios iniciados exitosamente${NC}"
        echo -e "${BLUE}Accesos disponibles:${NC}"
        echo "  - Eureka Server: http://localhost:8761"
        echo "  - API Gateway: http://localhost:8080"
        echo "  - Auth Service: http://localhost:8081"
        echo "  - User Service: http://localhost:8082"
        echo "  - Client Service: http://localhost:8083"
        echo "  - Patient Service: http://localhost:8084"
        echo "  - Appointment Service: http://localhost:8085"
        echo "  - Medical History Service: http://localhost:8108"
        echo "  - Prescription Service: http://localhost:8089"
        echo "  - Report Service: http://localhost:8090"
        echo "  - phpMyAdmin: http://localhost:8888"
    else
        echo -e "${RED}✗ Error al iniciar los servicios${NC}"
        exit 1
    fi
}

# Función para detener servicios
stop_services() {
    echo -e "${YELLOW}Deteniendo todos los servicios...${NC}"
    docker-compose down
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Servicios detenidos exitosamente${NC}"
    else
        echo -e "${RED}✗ Error al detener los servicios${NC}"
        exit 1
    fi
}

# Función para reiniciar servicios
restart_services() {
    echo -e "${YELLOW}Reiniciando todos los servicios...${NC}"
    docker-compose down
    docker-compose up -d
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Servicios reiniciados exitosamente${NC}"
    else
        echo -e "${RED}✗ Error al reiniciar los servicios${NC}"
        exit 1
    fi
}

# Función para ver logs
show_logs() {
    if [ -z "$1" ]; then
        echo -e "${YELLOW}Mostrando logs de todos los servicios...${NC}"
        docker-compose logs -f
    else
        echo -e "${YELLOW}Mostrando logs de $1...${NC}"
        docker-compose logs -f "$1"
    fi
}

# Función para ver estado
show_status() {
    echo -e "${YELLOW}Estado de los contenedores:${NC}"
    docker-compose ps
}

# Función de limpieza
clean_docker() {
    echo -e "${YELLOW}Limpiando contenedores e imágenes...${NC}"
    docker-compose down
    docker system prune -f
    echo -e "${GREEN}✓ Limpieza completada${NC}"
}

# Función de limpieza completa
clean_all() {
    echo -e "${RED}⚠️  ADVERTENCIA: Esto eliminará TODOS los datos incluyendo las bases de datos${NC}"
    read -p "¿Estás seguro? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo -e "${YELLOW}Realizando limpieza completa...${NC}"
        docker-compose down -v
        docker system prune -af --volumes
        echo -e "${GREEN}✓ Limpieza completa realizada${NC}"
    else
        echo -e "${BLUE}Operación cancelada${NC}"
    fi
}

# Función de configuración inicial
setup_project() {
    echo -e "${YELLOW}Configurando proyecto inicial...${NC}"
    
    # Verificar si Docker está instalado
    if ! command -v docker &> /dev/null; then
        echo -e "${RED}✗ Docker no está instalado${NC}"
        exit 1
    fi
    
    # Verificar si Docker Compose está instalado
    if ! command -v docker-compose &> /dev/null; then
        echo -e "${RED}✗ Docker Compose no está instalado${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}✓ Docker y Docker Compose están instalados${NC}"
    
    # Construir imágenes
    build_images
    
    # Iniciar servicios
    start_services
    
    echo -e "${GREEN}✓ Configuración inicial completada${NC}"
    echo -e "${BLUE}El sistema está listo para usar${NC}"
}

# Función para verificar salud de servicios
check_health() {
    echo -e "${YELLOW}Verificando estado de salud de los servicios...${NC}"
    
    services=("eureka-server:8761" "gateway-service:8080" "auth-service:8081" 
              "user-service:8082" "client-service:8083" "patient-service:8084"
              "appointment-service:8085" "medical-history-service:8108" 
              "prescription-service:8089" "report-service:8090")
    
    for service in "${services[@]}"; do
        IFS=':' read -r name port <<< "$service"
        if curl -s "http://localhost:$port/actuator/health" > /dev/null 2>&1; then
            echo -e "${GREEN}✓ $name está saludable${NC}"
        else
            echo -e "${RED}✗ $name no responde${NC}"
        fi
    done
}

# Función para backup de bases de datos
backup_databases() {
    echo -e "${YELLOW}Realizando backup de bases de datos...${NC}"
    
    mkdir -p backups
    timestamp=$(date +%Y%m%d_%H%M%S)
    
    databases=("veterinary_auth_db" "veterinary_user_db" "veterinary_client_db" 
               "veterinary_patient_db" "veterinary_appointment_db" "medical_history_db"
               "prescription_db" "report_db")
    
    for db in "${databases[@]}"; do
        echo "Backing up $db..."
        docker exec mysql-${db%-*}-db mysqldump -u root -proot $db > "backups/${db}_${timestamp}.sql"
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✓ Backup de $db completado${NC}"
        else
            echo -e "${RED}✗ Error en backup de $db${NC}"
        fi
    done
    
    echo -e "${GREEN}✓ Backup completado en directorio 'backups'${NC}"
}

# Main script logic
show_header

case "$1" in
    "build")
        build_images
        ;;
    "start")
        start_services
        ;;
    "stop")
        stop_services
        ;;
    "restart")
        restart_services
        ;;
    "logs")
        show_logs "$2"
        ;;
    "status")
        show_status
        ;;
    "clean")
        clean_docker
        ;;
    "clean-all")
        clean_all
        ;;
    "setup")
        setup_project
        ;;
    "health")
        check_health
        ;;
    "db-backup")
        backup_databases
        ;;
    "help"|"--help"|"-h")
        show_help
        ;;
    "")
        show_help
        ;;
    *)
        echo -e "${RED}Comando no reconocido: $1${NC}"
        show_help
        exit 1
        ;;
esac 