# ==============================
# Etapa 1: Build (Maven + Java 17)
# ==============================
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar archivos de configuración Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar aplicación (salta tests para build rápido)
RUN mvn clean package -DskipTests

# ==============================
# Etapa 2: Runtime (Java 17 JRE)
# ==============================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

# Copiar JAR desde etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Configuración JVM optimizada
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Ejecutar aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]