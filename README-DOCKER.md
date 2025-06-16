# 🐕 Sistema de Clínica Veterinaria - Dockerizado

Este proyecto es un sistema completo de gestión para clínicas veterinarias desarrollado con arquitectura de microservicios usando Spring Boot y dockerizado para facilitar su despliegue.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Arquitectura](#arquitectura)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Configuración](#instalación-y-configuración)
- [Uso del Sistema](#uso-del-sistema)
- [Servicios y Puertos](#servicios-y-puertos)
- [Base de Datos](#base-de-datos)
- [Scripts de Utilidad](#scripts-de-utilidad)
- [Monitoreo y Logs](#monitoreo-y-logs)
- [Desarrollo](#desarrollo)
- [Solución de Problemas](#solución-de-problemas)

## 🚀 Características

- **Gestión de Usuarios**: Sistema de autenticación y autorización con JWT
- **Gestión de Clientes**: Registro y administración de dueños de mascotas
- **Gestión de Pacientes**: Registro y seguimiento de mascotas
- **Sistema de Citas**: Programación y gestión de consultas veterinarias
- **Historial Médico**: Registro completo de consultas y tratamientos
- **Prescripciones**: Gestión de medicamentos y recetas
- **Reportes**: Generación de reportes financieros y estadísticos
- **Arquitectura de Microservicios**: Escalabilidad y mantenibilidad

## 🏗️ Arquitectura

### Microservicios

1. **Eureka Server** (Puerto 8761) - Descubrimiento de servicios
2. **API Gateway** (Puerto 8080) - Puerta de entrada única
3. **Auth Service** (Puerto 8081) - Autenticación y autorización
4. **User Service** (Puerto 8082) - Gestión de usuarios del sistema
5. **Client Service** (Puerto 8083) - Gestión de clientes
6. **Patient Service** (Puerto 8084) - Gestión de pacientes (mascotas)
7. **Appointment Service** (Puerto 8085) - Gestión de citas
8. **Medical History Service** (Puerto 8108) - Historial médico
9. **Prescription Service** (Puerto 8089) - Prescripciones
10. **Report Service** (Puerto 8090) - Reportes y estadísticas

### Bases de Datos

Cada servicio tiene su propia base de datos MySQL:
- `veterinary_auth_db` - Datos de autenticación
- `veterinary_user_db` - Datos de usuarios
- `veterinary_client_db` - Datos de clientes
- `veterinary_patient_db` - Datos de pacientes
- `veterinary_appointment_db` - Datos de citas
- `medical_history_db` - Historiales médicos
- `prescription_db` - Prescripciones
- `report_db` - Reportes y estadísticas

## 📋 Requisitos Previos

- **Docker** 20.10 o superior
- **Docker Compose** 2.0 o superior
- **8GB RAM** mínimo recomendado
- **Puertos disponibles**: 8080-8090, 8108, 8761, 8888, 3307-3314

### Verificar Requisitos

```bash
# Verificar Docker
docker --version

# Verificar Docker Compose
docker-compose --version

# Verificar puertos disponibles (Linux/Mac)
netstat -tuln | grep -E ':(8080|8081|8082|8083|8084|8085|8089|8090|8108|8761|8888)'
```

## 🛠️ Instalación y Configuración

### Opción 1: Configuración Automática (Recomendada)

```bash
# 1. Dar permisos de ejecución al script
chmod +x docker-scripts.sh

# 2. Ejecutar configuración automática
./docker-scripts.sh setup
```

### Opción 2: Configuración Manual

```bash
# 1. Construir todas las imágenes
docker-compose build --no-cache

# 2. Iniciar todos los servicios
docker-compose up -d

# 3. Verificar que todos los servicios estén corriendo
docker-compose ps
```

## 🚀 Uso del Sistema

### Scripts de Gestión

El proyecto incluye un script de utilidades para facilitar la gestión:

```bash
# Ver ayuda
./docker-scripts.sh help

# Iniciar el sistema
./docker-scripts.sh start

# Ver estado de los servicios
./docker-scripts.sh status

# Ver logs de todos los servicios
./docker-scripts.sh logs

# Ver logs de un servicio específico
./docker-scripts.sh logs auth-service

# Verificar salud de los servicios
./docker-scripts.sh health

# Detener el sistema
./docker-scripts.sh stop
```

### Comandos Docker Compose Directos

```bash
# Iniciar servicios
docker-compose up -d

# Detener servicios
docker-compose down

# Ver logs
docker-compose logs -f [nombre-servicio]

# Reiniciar un servicio específico
docker-compose restart [nombre-servicio]

# Escalar un servicio
docker-compose up -d --scale user-service=2
```

## 🌐 Servicios y Puertos

| Servicio | Puerto | URL | Descripción |
|----------|--------|-----|-------------|
| API Gateway | 8080 | http://localhost:8080 | Punto de entrada principal |
| Eureka Server | 8761 | http://localhost:8761 | Registro de servicios |
| Auth Service | 8081 | http://localhost:8081 | Autenticación |
| User Service | 8082 | http://localhost:8082 | Gestión de usuarios |
| Client Service | 8083 | http://localhost:8083 | Gestión de clientes |
| Patient Service | 8084 | http://localhost:8084 | Gestión de pacientes |
| Appointment Service | 8085 | http://localhost:8085 | Gestión de citas |
| Medical History Service | 8108 | http://localhost:8108 | Historial médico |
| Prescription Service | 8089 | http://localhost:8089 | Prescripciones |
| Report Service | 8090 | http://localhost:8090 | Reportes |
| phpMyAdmin | 8888 | http://localhost:8888 | Administración BD |

### Endpoints Principales

#### A través del API Gateway (Puerto 8080):
- `GET /auth/**` → Auth Service
- `GET /users/**` → User Service
- `GET /clients/**` → Client Service
- `GET /patients/**` → Patient Service
- `GET /appointments/**` → Appointment Service
- `GET /medical-history/**` → Medical History Service
- `GET /prescriptions/**` → Prescription Service
- `GET /reports/**` → Report Service

## 🗄️ Base de Datos

### Acceso a phpMyAdmin

1. Abrir: http://localhost:8888
2. Servidor: Seleccionar cualquier base de datos del dropdown
3. Usuario: `root`
4. Contraseña: `root`

### Conexión Directa a MySQL

```bash
# Conectar a base de datos específica
docker exec -it mysql-auth-db mysql -u root -proot veterinary_auth_db

# Ejemplo de conexión externa
mysql -h localhost -P 3307 -u root -proot veterinary_auth_db
```

### Puertos de Base de Datos

| Base de Datos | Puerto | Contenedor |
|---------------|--------|------------|
| Auth DB | 3307 | mysql-auth-db |
| User DB | 3308 | mysql-user-db |
| Client DB | 3309 | mysql-client-db |
| Patient DB | 3310 | mysql-patient-db |
| Appointment DB | 3311 | mysql-appointment-db |
| Medical History DB | 3312 | mysql-medical-history-db |
| Prescription DB | 3313 | mysql-prescription-db |
| Report DB | 3314 | mysql-report-db |

### Backup y Restauración

```bash
# Realizar backup de todas las bases de datos
./docker-scripts.sh db-backup

# Backup manual de una base de datos específica
docker exec mysql-auth-db mysqldump -u root -proot veterinary_auth_db > backup_auth.sql

# Restaurar una base de datos
docker exec -i mysql-auth-db mysql -u root -proot veterinary_auth_db < backup_auth.sql
```

## 🛠️ Scripts de Utilidad

### docker-scripts.sh

Script principal para gestión del proyecto:

```bash
./docker-scripts.sh [comando]
```

**Comandos disponibles:**

- `setup` - Configuración inicial completa
- `build` - Construir todas las imágenes
- `start` - Iniciar todos los servicios
- `stop` - Detener todos los servicios
- `restart` - Reiniciar todos los servicios
- `status` - Ver estado de contenedores
- `logs [servicio]` - Ver logs
- `health` - Verificar salud de servicios
- `clean` - Limpiar contenedores e imágenes
- `clean-all` - Limpieza completa (incluye volúmenes)
- `db-backup` - Backup de bases de datos

## 📊 Monitoreo y Logs

### Ver Logs en Tiempo Real

```bash
# Todos los servicios
docker-compose logs -f

# Servicio específico
docker-compose logs -f auth-service

# Últimas 100 líneas
docker-compose logs --tail=100 auth-service
```

### Monitoreo de Recursos

```bash
# Ver uso de recursos de contenedores
docker stats

# Ver información detallada de un contenedor
docker inspect [container-name]
```

### Health Checks

Los servicios incluyen health checks automáticos:

```bash
# Verificar salud usando el script
./docker-scripts.sh health

# Verificar manualmente
curl http://localhost:8081/actuator/health
```

## 👨‍💻 Desarrollo

### Variables de Entorno

Las configuraciones se manejan a través de variables de entorno en el `docker-compose.yml`:

- `SPRING_PROFILES_ACTIVE=docker`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE`

### Desarrollo Local

```bash
# Modo desarrollo con reconstrucción automática
docker-compose up --build

# Reconstruir un servicio específico
docker-compose build --no-cache auth-service
docker-compose up -d auth-service
```

### Debugging

```bash
# Acceder al contenedor
docker exec -it auth-service bash

# Ver variables de entorno
docker exec auth-service env

# Ver archivos de configuración
docker exec auth-service cat /app/src/main/resources/application-docker.yml
```

## 🔧 Solución de Problemas

### Problemas Comunes

#### 1. Error de Puertos Ocupados

```bash
# Verificar qué está usando el puerto
netstat -tuln | grep 8080

# Matar proceso que usa el puerto (Linux/Mac)
lsof -ti:8080 | xargs kill -9
```

#### 2. Servicios que no Inician

```bash
# Ver logs del servicio
docker-compose logs auth-service

# Reiniciar servicio específico
docker-compose restart auth-service

# Verificar recursos del sistema
docker system df
```

#### 3. Problemas de Conectividad entre Servicios

```bash
# Verificar red de Docker
docker network ls
docker network inspect veterinary-network

# Probar conectividad entre contenedores
docker exec auth-service ping eureka-server
```

#### 4. Base de Datos no se Conecta

```bash
# Verificar estado de contenedores MySQL
docker-compose ps | grep mysql

# Ver logs de base de datos
docker-compose logs mysql-auth

# Probar conexión manual
docker exec -it mysql-auth-db mysql -u root -proot -e "SHOW DATABASES;"
```

### Limpieza y Reinicio

```bash
# Reinicio completo del sistema
./docker-scripts.sh clean-all
./docker-scripts.sh setup

# Liberar espacio en disco
docker system prune -af --volumes
```

### Logs de Depuración

```bash
# Habilitar logs de debug en un servicio
docker-compose exec auth-service java -Dlogging.level.root=DEBUG -jar target/auth-service-1.0.0.jar
```

## 📧 Soporte

Si encuentras problemas:

1. Verifica los logs: `./docker-scripts.sh logs`
2. Comprueba el estado: `./docker-scripts.sh status`
3. Verifica la salud: `./docker-scripts.sh health`
4. Reinicia el sistema: `./docker-scripts.sh restart`

## 🔐 Credenciales por Defecto

### Usuario Administrador
- **Usuario**: admin
- **Email**: admin@veterinary.com
- **Contraseña**: admin123

### Base de Datos
- **Usuario**: root
- **Contraseña**: root

### phpMyAdmin
- **Usuario**: root
- **Contraseña**: root

---

## 📝 Notas Importantes

- El primer inicio puede tomar varios minutos mientras se descargan las imágenes y se inicializan las bases de datos
- Asegúrate de tener suficiente espacio en disco (mínimo 5GB libres)
- Los datos se persisten en volúmenes de Docker, no se pierden al reiniciar
- Para un entorno de producción, cambia todas las contraseñas por defecto

¡El sistema está listo para usarse! 🎉 