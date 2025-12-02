# ==============================
# Etapa 1: Build (Maven + Java 17)
# ==============================
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar archivos de configuración Maven
COPY pom.xml .

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar aplicación
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

# Exponer puerto (Railway asigna dinámicamente)
EXPOSE 8080

# Configuración JVM optimizada para contenedores
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport"

# Ejecutar con perfil de producción
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=prod -jar app.jar"]