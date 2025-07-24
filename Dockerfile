# Use an official OpenJDK 21 runtime as a parent image
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy build files
COPY build/libs/latest/redis-test-*.jar app.jar

# Expose the application port
EXPOSE 7070

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
