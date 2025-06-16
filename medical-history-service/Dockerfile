# Usar OpenJDK 17 como imagen base
FROM openjdk:17-jdk-alpine

# Instalar curl para health checks
RUN apk add --no-cache curl

# Crear directorio de la aplicación
WORKDIR /app

# Crear directorio para uploads de archivos
RUN mkdir -p /app/uploads
RUN chmod 755 /app/uploads

# Copiar el archivo wrapper de Maven y dar permisos (desde raíz)
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

# Copiar pom.xml padre y del servicio
COPY pom.xml .
COPY medical-history-service/pom.xml medical-history-service/

# Descargar dependencias de Maven
RUN ./mvnw dependency:go-offline -B -f medical-history-service/pom.xml

# Copiar el código fuente del servicio
COPY medical-history-service/src medical-history-service/src

# Compilar la aplicación
RUN ./mvnw clean package -Dmaven.test.skip=true -f medical-history-service/pom.xml

# Exponer puerto
EXPOSE 8108

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "medical-history-service/target/medical-history-service-1.0.0.jar"] 