# Use an official OpenJDK 21 runtime as a parent image
FROM eclipse-temurin:21-jre-alpine

# Create a non-root user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Set the working directory
WORKDIR /app

# Copy build files and set ownership
COPY --chown=appuser:appgroup build/libs/latest/redis-test-*.jar app.jar

# Switch to non-root user
USER appuser

# Expose the application port
EXPOSE 7070

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:7070/actuator/health || exit 1

# Run the Spring Boot application with security settings
ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-secure}", \
    "-jar", "app.jar"]
