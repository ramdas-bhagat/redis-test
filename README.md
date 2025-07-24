# Redis Test Spring Boot Application

This project demonstrates using H2 as a database and Redis as a cache in a Spring Boot application.

## Features

- H2 in-memory database
- Redis cache integration
- JPA/Hibernate configuration
- Docker Compose setup for easy deployment
- Redis Commander for Redis monitoring
- Health checks for all services

## Prerequisites

- Java 21
- Gradle
- Docker and Docker Compose

## Setup

### Using Docker Compose (Recommended)

The easiest way to run the application is using Docker Compose:

```sh
docker-compose up -d
```

This will start:

- Redis server on port 6379
- Redis Commander UI on port 8081
- Spring Boot application on port 7070

### Manual Setup

Alternatively, you can run the application manually:

1. Start Redis:

```sh
docker run -d --name redis-server -p 6379:6379 redis:latest
```

2. Run the Spring Boot application:

```sh
./gradlew bootRun
```

## Available Endpoints

The application exposes the following endpoints:

- Welcome Message: [http://localhost:7070/api/v1/public/getWelcomeMessage](http://localhost:7070/api/v1/public/getWelcomeMessage)
- Health Check: [http://localhost:7070/actuator/health](http://localhost:7070/actuator/health)
- H2 Console: [http://localhost:7070/h2-console](http://localhost:7070/h2-console)

## Monitoring

### Redis Commander

Access Redis Commander at [http://localhost:8081](http://localhost:8081) to monitor and manage your Redis instance.

### Application Health

The application includes Spring Boot Actuator for health monitoring. You can check the application's health at the `/actuator/health` endpoint.

## License

MIT
