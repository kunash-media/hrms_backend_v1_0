# Stage 1 — Build
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn clean package -DskipTests -q

# Stage 2 — Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Install wait-for-it script and dig for DNS debugging
RUN apk add --no-cache netcat-openbsd bind-tools

COPY --from=builder /app/target/*.jar app.jar

# Use wait-for-it with timeout and better error handling
ENTRYPOINT ["sh", "-c", "echo 'Waiting for MySQL...' && while ! nc -z ${DB_HOST:-mysql} 3306; do echo 'MySQL not ready, waiting...'; sleep 2; done; echo 'MySQL started!'; java -jar app.jar"]