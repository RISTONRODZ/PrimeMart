# --- Stage 1: Build ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first to leverage Docker layer caching for dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# --- Stage 2: Runtime ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Render injects PORT at runtime; Spring Boot reads it via server.port env var
ENV SERVER_PORT=8080
EXPOSE 8080

# Non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]