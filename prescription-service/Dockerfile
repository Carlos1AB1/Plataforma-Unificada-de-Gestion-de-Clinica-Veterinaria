# Usar OpenJDK 17 como imagen base
FROM openjdk:17-jdk-alpine

# Instalar curl para health checks
RUN apk add --no-cache curl

# Crear directorio de la aplicación
WORKDIR /app

# Copiar el archivo wrapper de Maven y dar permisos (desde raíz)
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

# Copiar pom.xml padre y del servicio
COPY pom.xml .
COPY prescription-service/pom.xml prescription-service/

# Descargar dependencias de Maven
RUN ./mvnw dependency:go-offline -B -f prescription-service/pom.xml

# Copiar el código fuente del servicio
COPY prescription-service/src prescription-service/src

# Compilar la aplicación
RUN ./mvnw clean package -Dmaven.test.skip=true -f prescription-service/pom.xml

# Exponer puerto
EXPOSE 8089

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "prescription-service/target/prescription-service-1.0.0.jar"] 