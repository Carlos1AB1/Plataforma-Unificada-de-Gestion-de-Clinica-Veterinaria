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
COPY gateway-service/pom.xml gateway-service/

# Descargar dependencias de Maven
RUN ./mvnw dependency:go-offline -B -f gateway-service/pom.xml

# Copiar el código fuente del servicio
COPY gateway-service/src gateway-service/src

# Compilar la aplicación
RUN ./mvnw clean package -Dmaven.test.skip=true -f gateway-service/pom.xml

# Exponer puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "gateway-service/target/gateway-service-1.0.0.jar"] 