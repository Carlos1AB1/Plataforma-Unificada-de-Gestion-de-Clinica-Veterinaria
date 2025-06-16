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
COPY user-service/pom.xml user-service/

# Descargar dependencias de Maven
RUN ./mvnw dependency:go-offline -B -f user-service/pom.xml

# Copiar el código fuente del servicio
COPY user-service/src user-service/src

# Compilar la aplicación
RUN ./mvnw clean package -Dmaven.test.skip=true -f user-service/pom.xml

# Exponer puerto
EXPOSE 8082

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "user-service/target/user-service-1.0.0.jar"] 