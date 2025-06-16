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
COPY appointment-service/pom.xml appointment-service/

# Descargar dependencias de Maven
RUN ./mvnw dependency:go-offline -B -f appointment-service/pom.xml

# Copiar el código fuente del servicio
COPY appointment-service/src appointment-service/src

# Compilar la aplicación
RUN ./mvnw clean package -Dmaven.test.skip=true -f appointment-service/pom.xml

# Exponer puerto
EXPOSE 8085

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "appointment-service/target/appointment-service-1.0.0.jar"] 