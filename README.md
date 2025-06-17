# 🏥 Sistema de Gestión para Clínica Veterinaria

Un sistema completo de gestión para clínicas veterinarias desarrollado con arquitectura de microservicios usando **Spring Boot** y **Spring Cloud**, containerizado con **Docker** para facilitar su despliegue y escalabilidad.

## 📋 Tabla de Contenidos

- [Características Principales](#-características-principales)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Arquitectura del Sistema](#-arquitectura-del-sistema)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Uso del Sistema](#-uso-del-sistema)
- [Servicios y APIs](#-servicios-y-apis)
- [Base de Datos](#-base-de-datos)
- [Desarrollo](#-desarrollo)
- [Documentación Adicional](#-documentación-adicional)
- [Solución de Problemas](#-solución-de-problemas)
- [Contribución](#-contribución)
- [Licencia](#-licencia)

## 🚀 Características Principales

### Gestión Integral de Clínica Veterinaria

- **👤 Gestión de Usuarios**: Sistema completo de autenticación y autorización con JWT
- **🧑‍💼 Gestión de Clientes**: Registro y administración de propietarios de mascotas
- **🐕 Gestión de Pacientes**: Registro completo de mascotas con historial detallado
- **📅 Sistema de Citas**: Programación inteligente de consultas veterinarias
- **📋 Historial Médico**: Registro completo de consultas, diagnósticos y tratamientos
- **💊 Prescripciones**: Gestión de medicamentos, recetas y tratamientos
- **📊 Reportes**: Generación de reportes financieros, estadísticos y operacionales
- **🔐 Seguridad**: Autenticación JWT y control de acceso basado en roles

### Características Técnicas

- **🏗️ Arquitectura de Microservicios**: Escalabilidad y mantenibilidad
- **🔄 Service Discovery**: Registro automático de servicios con Eureka
- **🌐 API Gateway**: Punto de entrada único para todas las APIs
- **📦 Containerización**: Deployment simplificado con Docker
- **🗄️ Base de Datos Distribuida**: MySQL con bases de datos separadas por dominio
- **📈 Monitoreo**: Health checks y métricas con Spring Boot Actuator
- **🔧 Configuración Centralizada**: Variables de entorno y perfiles de Spring

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17** - Lenguaje de programación principal
- **Spring Boot 3.1.5** - Framework principal para microservicios
- **Spring Cloud 2022.0.4** - Herramientas para microservicios distribuidos
- **Spring Security** - Seguridad y autenticación
- **Spring Data JPA** - Persistencia de datos
- **Spring Cloud Gateway** - API Gateway
- **Netflix Eureka** - Service Discovery

### Base de Datos
- **MySQL 8.0** - Base de datos relacional principal
- **Flyway/Liquibase** - Migraciones de base de datos

### DevOps y Deployment
- **Docker** - Containerización
- **Docker Compose** - Orquestación de contenedores
- **Maven** - Gestión de dependencias y build

### Herramientas de Desarrollo
- **phpMyAdmin** - Administración de base de datos
- **Spring Boot Actuator** - Monitoreo y métricas
- **SLF4J + Logback** - Sistema de logging

## 🏗️ Arquitectura del Sistema

### Diagrama de Arquitectura

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Frontend      │────│   API Gateway    │────│  Eureka Server  │
│   (Cliente)     │    │   (Puerto 8080)  │    │  (Puerto 8761)  │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │
                ┌───────────────┼───────────────┐
                │               │               │
        ┌───────▼─────┐ ┌───────▼─────┐ ┌───────▼─────┐
        │Auth Service │ │User Service │ │Client Service│
        │(Puerto 8081)│ │(Puerto 8082)│ │(Puerto 8083)│
        └─────────────┘ └─────────────┘ └─────────────┘
                │               │               │
        ┌───────▼─────┐ ┌───────▼─────┐ ┌───────▼─────┐
        │Patient Serv │ │Appointment  │ │Medical Hist │
        │(Puerto 8084)│ │(Puerto 8085)│ │(Puerto 8108)│
        └─────────────┘ └─────────────┘ └─────────────┘
                │               │               │
        ┌───────▼─────┐ ┌───────▼─────┐
        │Prescription │ │Report Serv  │
        │(Puerto 8089)│ │(Puerto 8090)│
        └─────────────┘ └─────────────┘
                │               │
        ┌───────▼─────────────────▼─────┐
        │        MySQL Cluster          │
        │   (8 Bases de Datos)          │
        └───────────────────────────────┘
```

### Microservicios

| Servicio | Puerto | Responsabilidad | Base de Datos |
|----------|--------|-----------------|---------------|
| **Eureka Server** | 8761 | Registro y descubrimiento de servicios | - |
| **API Gateway** | 8080 | Enrutamiento y balanceo de carga | - |
| **Auth Service** | 8081 | Autenticación y autorización JWT | `veterinary_auth_db` |
| **User Service** | 8082 | Gestión de usuarios del sistema | `veterinary_user_db` |
| **Client Service** | 8083 | Gestión de clientes/propietarios | `veterinary_client_db` |
| **Patient Service** | 8084 | Gestión de pacientes/mascotas | `veterinary_patient_db` |
| **Appointment Service** | 8085 | Gestión de citas médicas | `veterinary_appointment_db` |
| **Medical History Service** | 8108 | Historial médico completo | `medical_history_db` |
| **Prescription Service** | 8089 | Gestión de recetas y medicamentos | `prescription_db` |
| **Report Service** | 8090 | Reportes y estadísticas | `report_db` |

## 📋 Requisitos Previos

### Software Requerido

- **Docker** 20.10 o superior
- **Docker Compose** 2.0 o superior
- **Java 17** (para desarrollo local)
- **Maven 3.8+** (para compilación local)

### Recursos del Sistema

- **RAM**: Mínimo 8GB (recomendado 16GB)
- **CPU**: Mínimo 4 cores
- **Disco**: Mínimo 10GB libres
- **Puertos disponibles**: 8080-8090, 8108, 8761, 8888, 3307-3314

### Verificación de Requisitos

```bash
# Verificar Docker
docker --version
docker-compose --version

# Verificar Java (para desarrollo)
java --version
mvn --version

# Verificar puertos disponibles (Linux/Mac)
netstat -tuln | grep -E ':(8080|8081|8082|8083|8084|8085|8089|8090|8108|8761|8888)'
```

## 🛠️ Instalación y Configuración

### Opción 1: Configuración Automática (Recomendada)

```bash
# 1. Clonar el repositorio
git clone <repository-url>
cd veterinary-clinic-microservices

# 2. Dar permisos de ejecución al script
chmod +x docker-scripts.sh

# 3. Ejecutar configuración automática
./docker-scripts.sh setup
```

### Opción 2: Configuración Manual

```bash
# 1. Configurar variables de entorno
cp environment-variables.example .env
# Editar .env según tus necesidades

# 2. Construir todas las imágenes
docker-compose build --no-cache

# 3. Iniciar todos los servicios
docker-compose up -d

# 4. Verificar que todos los servicios estén funcionando
docker-compose ps
./docker-scripts.sh health
```

### Opción 3: Desarrollo Local (Sin Docker)

```bash
# 1. Compilar todos los servicios
mvn clean install

# 2. Configurar bases de datos locales
# (Ejecutar scripts SQL en database/init-scripts/)

# 3. Iniciar Eureka Server
cd eureka-server && mvn spring-boot:run

# 4. Iniciar cada microservicio
cd auth-service && mvn spring-boot:run
cd user-service && mvn spring-boot:run
# ... repetir para cada servicio
```

## 🚀 Uso del Sistema

### Scripts de Gestión Incluidos

El proyecto incluye un script de utilidades para facilitar la gestión:

```bash
# Ver ayuda completa
./docker-scripts.sh help

# Gestión básica
./docker-scripts.sh start      # Iniciar el sistema
./docker-scripts.sh stop       # Detener el sistema
./docker-scripts.sh restart    # Reiniciar el sistema
./docker-scripts.sh status     # Ver estado de servicios

# Monitoreo
./docker-scripts.sh logs [servicio]  # Ver logs
./docker-scripts.sh health          # Verificar salud de servicios

# Mantenimiento
./docker-scripts.sh clean           # Limpiar contenedores
./docker-scripts.sh clean-all       # Limpieza completa
./docker-scripts.sh db-backup       # Backup de bases de datos
```

### Comandos Docker Compose

```bash
# Iniciar servicios en background
docker-compose up -d

# Ver logs en tiempo real
docker-compose logs -f [nombre-servicio]

# Reiniciar un servicio específico
docker-compose restart [nombre-servicio]

# Escalar un servicio
docker-compose up -d --scale user-service=2

# Detener todos los servicios
docker-compose down
```

## 🌐 Servicios y APIs

### URLs de Acceso

| Servicio | URL Local | Descripción |
|----------|-----------|-------------|
| **API Gateway** | http://localhost:8080 | Punto de entrada principal |
| **Eureka Dashboard** | http://localhost:8761 | Monitor de servicios |
| **phpMyAdmin** | http://localhost:8888 | Administración de BD |

### Endpoints Principales (vía API Gateway)

```bash
# Autenticación
POST /auth/login              # Iniciar sesión
POST /auth/register           # Registrar usuario
GET  /auth/validate           # Validar token

# Usuarios
GET    /users                 # Listar usuarios
POST   /users                 # Crear usuario
GET    /users/{id}           # Obtener usuario
PUT    /users/{id}           # Actualizar usuario
DELETE /users/{id}           # Eliminar usuario

# Clientes
GET    /clients              # Listar clientes
POST   /clients              # Crear cliente
GET    /clients/{id}         # Obtener cliente
PUT    /clients/{id}         # Actualizar cliente

# Pacientes (Mascotas)
GET    /patients             # Listar pacientes
POST   /patients             # Crear paciente
GET    /patients/{id}        # Obtener paciente
GET    /patients/client/{clientId}  # Pacientes por cliente

# Citas
GET    /appointments         # Listar citas
POST   /appointments         # Crear cita
GET    /appointments/{id}    # Obtener cita
PUT    /appointments/{id}    # Actualizar cita
DELETE /appointments/{id}    # Cancelar cita

# Historial Médico
GET    /medical-history/patient/{patientId}  # Historial por paciente
POST   /medical-history                      # Crear registro médico
POST   /medical-history/upload              # Subir documentos

# Prescripciones
GET    /prescriptions/patient/{patientId}   # Prescripciones por paciente
POST   /prescriptions                       # Crear prescripción
GET    /prescriptions/{id}                  # Obtener prescripción

# Reportes
GET    /reports/financial    # Reportes financieros
GET    /reports/appointments # Reportes de citas
GET    /reports/patients     # Reportes de pacientes
```

### Documentación de API

Una vez el sistema esté ejecutándose, la documentación Swagger estará disponible en:
- http://localhost:8080/swagger-ui.html (vía Gateway)
- http://localhost:8081/swagger-ui.html (Auth Service directo)
- http://localhost:8082/swagger-ui.html (User Service directo)
- ... (cada servicio tiene su propia documentación)

## 🗄️ Base de Datos

### Configuración de MySQL

El sistema utiliza **8 bases de datos MySQL separadas**, una para cada dominio:

| Base de Datos | Puerto | Propósito |
|---------------|--------|-----------|
| `veterinary_auth_db` | 3307 | Autenticación y tokens |
| `veterinary_user_db` | 3308 | Usuarios del sistema |
| `veterinary_client_db` | 3309 | Información de clientes |
| `veterinary_patient_db` | 3310 | Información de mascotas |
| `veterinary_appointment_db` | 3311 | Citas médicas |
| `medical_history_db` | 3312 | Historiales médicos |
| `prescription_db` | 3313 | Recetas y medicamentos |
| `report_db` | 3314 | Datos para reportes |

### Acceso a las Bases de Datos

#### vía phpMyAdmin (Recomendado)
1. Abrir: http://localhost:8888
2. Seleccionar servidor de la lista
3. Usuario: `root` / Contraseña: `root`

#### Conexión Directa
```bash
# Ejemplo para base de datos de autenticación
mysql -h localhost -P 3307 -u root -proot veterinary_auth_db

# Acceso desde contenedor
docker exec -it mysql-auth-db mysql -u root -proot veterinary_auth_db
```

### Scripts de Inicialización

Los scripts SQL de inicialización se encuentran en:
- `database/init-scripts/auth-db.sql`
- `database/init-scripts/user-db.sql`
- `database/init-scripts/client-db.sql`
- ... (uno para cada servicio)

### Backup y Restauración

```bash
# Backup automático de todas las BD
./docker-scripts.sh db-backup

# Backup manual de una BD específica
docker exec mysql-auth-db mysqldump -u root -proot veterinary_auth_db > backup_auth.sql

# Restaurar una BD
docker exec -i mysql-auth-db mysql -u root -proot veterinary_auth_db < backup_auth.sql
```

## 👨‍💻 Desarrollo

### Estructura del Proyecto

```
veterinary-clinic-microservices/
├── pom.xml                    # POM principal (parent)
├── docker-compose.yml         # Configuración de contenedores
├── docker-scripts.sh          # Scripts de utilidad
├── environment-variables.example  # Variables de entorno
├── database/                  # Scripts de BD
│   └── init-scripts/         
├── eureka-server/            # Servicio de registro
├── gateway-service/          # API Gateway
├── auth-service/            # Autenticación
├── user-service/            # Gestión de usuarios
├── client-service/          # Gestión de clientes
├── patient-service/         # Gestión de pacientes
├── appointment-service/     # Gestión de citas
├── medical-history-service/ # Historial médico
├── prescription-service/    # Prescripciones
└── report-service/         # Reportes y estadísticas
```

### Configuración de Desarrollo

#### Variables de Entorno

```bash
# Copiar archivo de ejemplo
cp environment-variables.example .env

# Editar variables según necesidad
nano .env
```

#### Perfiles de Spring

- **default**: Desarrollo local
- **docker**: Contenedores Docker
- **production**: Producción

#### Desarrollo Local

```bash
# Compilar proyecto completo
mvn clean install

# Ejecutar un servicio específico
cd auth-service
mvn spring-boot:run -Dspring-boot.run.profiles=default

# Ejecutar con debug
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

#### Desarrollo con Docker

```bash
# Reconstruir un servicio específico
docker-compose build --no-cache auth-service
docker-compose up -d auth-service

# Desarrollo con hot reload
docker-compose -f docker-compose.dev.yml up
```

### Testing

```bash
# Ejecutar tests unitarios
mvn test

# Ejecutar tests de integración
mvn verify

# Tests de un servicio específico
cd auth-service
mvn test
```

### Logging y Debugging

```bash
# Ver logs de un servicio
docker-compose logs -f auth-service

# Acceder al contenedor para debugging
docker exec -it auth-service bash

# Ver variables de entorno
docker exec auth-service env

# Configurar nivel de log
docker-compose exec auth-service java -Dlogging.level.com.veterinary=DEBUG -jar target/auth-service-1.0.0.jar
```

## 📚 Documentación Adicional

- [README-DOCKER.md](README-DOCKER.md) - Documentación detallada de Docker
- [DOCKER-SUCCESS-REPORT.md](DOCKER-SUCCESS-REPORT.md) - Reporte de implementación Docker
- `docker-scripts.sh help` - Ayuda completa de scripts

### APIs y Documentación

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Eureka Dashboard**: http://localhost:8761
- **Actuator Endpoints**: http://localhost:8080/actuator

## 🔧 Solución de Problemas

### Problemas Comunes

#### 1. Servicios no inician
```bash
# Verificar estado
./docker-scripts.sh status

# Ver logs específicos
./docker-scripts.sh logs [servicio]

# Reiniciar servicio
docker-compose restart [servicio]
```

#### 2. Error de conectividad entre servicios
```bash
# Verificar red de Docker
docker network inspect veterinary-network

# Probar conectividad
docker exec auth-service ping eureka-server
```

#### 3. Base de datos no conecta
```bash
# Ver logs de BD
docker-compose logs mysql-auth

# Verificar health check
docker-compose ps | grep mysql

# Probar conexión manual
docker exec -it mysql-auth-db mysql -u root -proot -e "SHOW DATABASES;"
```

#### 4. Puertos ocupados
```bash
# Ver qué proceso usa el puerto
netstat -tuln | grep 8080
lsof -ti:8080

# Liberar puerto (Linux/Mac)
lsof -ti:8080 | xargs kill -9
```

### Limpieza y Mantenimiento

```bash
# Reinicio completo del sistema
./docker-scripts.sh clean-all
./docker-scripts.sh setup

# Liberar espacio en disco
docker system prune -af --volumes

# Limpiar imágenes no utilizadas
docker image prune -af
```

### Monitoreo del Sistema

```bash
# Ver uso de recursos
docker stats

# Health check de servicios
./docker-scripts.sh health

# Verificar logs del sistema
./docker-scripts.sh logs
```

## 🤝 Contribución

### Proceso de Contribución

1. **Fork** el repositorio
2. Crear una **rama de feature** (`git checkout -b feature/AmazingFeature`)
3. **Commit** tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. **Push** a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un **Pull Request**

### Estándares de Código

- Seguir convenciones de **Java** y **Spring Boot**
- Documentar métodos públicos con **Javadoc**
- Incluir **tests unitarios** para nuevas funcionalidades
- Mantener **cobertura de tests** > 80%

### Estructura de Commits

```
tipo(alcance): descripción breve

descripción más detallada si es necesaria

Fixes #123
```

Tipos: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

## 🔐 Credenciales por Defecto

### Usuario Administrador del Sistema
- **Usuario**: admin
- **Email**: admin@veterinary.com
- **Contraseña**: admin123

### Base de Datos MySQL
- **Usuario**: root
- **Contraseña**: root
- **Usuario aplicación**: vet_user
- **Contraseña aplicación**: vet_password

### phpMyAdmin
- **URL**: http://localhost:8888
- **Usuario**: root
- **Contraseña**: root

> ⚠️ **IMPORTANTE**: Cambiar todas las credenciales por defecto en entorno de producción

## 📄 Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 📞 Soporte y Contacto

Si encuentras problemas o tienes preguntas:

1. **Revisar documentación**: Consultar este README y [README-DOCKER.md](README-DOCKER.md)
2. **Verificar logs**: `./docker-scripts.sh logs`
3. **Comprobar estado**: `./docker-scripts.sh health`
4. **Crear un Issue**: Reportar problemas en GitHub Issues
5. **Documentación API**: Consultar Swagger UI en http://localhost:8080/swagger-ui.html

---

## 🎉 Estado del Proyecto

✅ **Sistema Completamente Funcional**

- ✅ Arquitectura de microservicios implementada
- ✅ Todos los servicios containerizados y funcionando
- ✅ Base de datos configurada y poblada
- ✅ API Gateway configurado y funcionando
- ✅ Autenticación JWT implementada
- ✅ Documentación completa
- ✅ Scripts de automatización incluidos
- ✅ Health checks y monitoreo configurado

**¡El sistema está listo para usar en desarrollo y producción!** 🚀

---

*Desarrollado con ❤️ para la gestión eficiente de clínicas veterinarias* 