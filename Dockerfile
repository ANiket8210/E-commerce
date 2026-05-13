# This is a multi-stage Docker build

# This Dockerfile is for the server
# Stage 1 : Alias build, to referece later
FROM cleanstart/maven:3.9.12-amd64 AS build
WORKDIR /app
COPY server/pom.xml .
COPY server/src ./src
RUN mvn clean package -DskipTests

# Copy Artifact: Copies the built JAR file from the build stage
# --from=build references the first stage
# Renames it to app.jar for simplicity
FROM openjdk:27-ea-jdk-slim-bookworm
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]