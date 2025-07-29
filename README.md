# Redis Test Spring Boot Application

This project demonstrates a **production-ready Spring Boot application** with Redis caching, JWT authentication, and comprehensive security configurations.

## 🚀 Features

- **H2 In-Memory Database** with JPA/Hibernate
- **Redis Cache Integration** with Spring Boot
- **JWT Authentication** with secure token management
- **Docker Compose Setup** for easy deployment
- **Redis Commander** for Redis monitoring and management
- **Spring Security** with role-based access control
- **Health Checks** and monitoring with Spring Actuator
- **Production Security** configurations and best practices
- **Environment-based Configuration** for different deployment scenarios

## 📋 Prerequisites

- **Java 21** or higher
- **Gradle 8+** (wrapper included)
- **Docker & Docker Compose** for containerized deployment
- **Git** for version control

## 🔧 Quick Start

### Option 1: Standard Setup (Development)

```bash
# Clone the repository
git clone https://github.com/ramdas-bhagat/redis-test.git
cd redis-test

# Start Redis (required for caching)
docker run -d --name redis-server -p 6379:6379 redis:latest

# Build and run the application
./gradlew bootRun
```

### Option 2: Docker Compose (Recommended)

```bash
# Start all services (Redis + Redis Commander + Application)
docker-compose up -d

# View logs
docker-compose logs -f app
```

### Option 3: Secure Production Setup

```bash
# Copy environment template
cp .env.template .env

# Edit .env file with your secure values
# vim .env  # or your preferred editor

# Start with production security settings
docker-compose -f docker-compose-secure.yml up -d
```

## 🔐 Security Configuration

### Environment Variables

Create a `.env` file for secure configuration:

```bash
# JWT Security
JWT_SECRET_KEY=your-256-bit-secret-key-here-make-it-very-long-and-random

# Redis Security  
REDIS_PASSWORD=your-secure-redis-password
REDIS_HOST=localhost
REDIS_PORT=6379

# Redis Commander UI Authentication
REDIS_COMMANDER_USER=admin
REDIS_COMMANDER_PASSWORD=your-secure-ui-password

# Database Security
H2_PASSWORD=your-h2-password
H2_CONSOLE_ENABLED=false  # Disable in production

# Application Settings
SERVER_PORT=7070
LOG_LEVEL=INFO
SPRING_PROFILES_ACTIVE=secure
```

### Production Deployment

For production, use the secure profile and environment variables:

```bash
# Set environment variables
export JWT_SECRET_KEY="your-super-secret-jwt-key-256-bits-long"
export REDIS_PASSWORD="secure-redis-password"
export SPRING_PROFILES_ACTIVE=secure
export H2_CONSOLE_ENABLED=false

# Deploy with secure configuration
docker-compose -f docker-compose-secure.yml up -d
```

## 🌐 Available Endpoints

### Public Endpoints
- **Welcome Message**: [http://localhost:7070/api/v1/public/getWelcomeMessage](http://localhost:7070/api/v1/public/getWelcomeMessage)
- **Health Check**: [http://localhost:7070/actuator/health](http://localhost:7070/actuator/health)

### Authentication Required
- **Employee Management**: `http://localhost:7070/api/v1/employee/*`
- **User Management**: `http://localhost:7070/api/v1/user/*`

### Development Tools (Development Only)
- **H2 Console**: [http://localhost:7070/h2-console](http://localhost:7070/h2-console)
  - **JDBC URL**: `jdbc:h2:mem:testdb`
  - **Username**: `sa`
  - **Password**: (empty in development)

### Monitoring & Management
- **Redis Commander**: [http://localhost:8081](http://localhost:8081)
- **Application Health**: [http://localhost:7070/actuator/health](http://localhost:7070/actuator/health)
- **Application Info**: [http://localhost:7070/actuator/info](http://localhost:7070/actuator/info)

## 🔑 Authentication

### Getting Access Token

```bash
# Login to get JWT token
curl -X POST http://localhost:7070/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "user", "password": "password"}'
```

### Using the Token

```bash
# Use token in API calls
curl -X GET http://localhost:7070/api/v1/employee/getAll \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Example API Calls

```bash
# Get employee by ID (with caching)
curl -X GET 'http://localhost:7070/api/v1/employee/getById?id=1' \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'

# Create new employee
curl -X POST http://localhost:7070/api/v1/employee/save \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "firstName": "John",
    "lastName": "Doe", 
    "email": "john.doe@example.com",
    "phoneNumber": "123-456-7890"
  }'
```

## 🏗️ Development

### Building the Application

```bash
# Clean and build
./gradlew clean build

# Run tests
./gradlew test

# Apply code formatting
./gradlew spotlessApply

# Build Docker image
docker build -t redis-test-app .
```

### Database Access

**H2 Console** (Development only):
- URL: http://localhost:7070/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

### Redis Management

**Redis Commander** (All environments):
- URL: http://localhost:8081
- Username: `admin` (in secure mode)
- Password: Set via `REDIS_COMMANDER_PASSWORD`

## 🐳 Docker Configuration

### Development (docker-compose.yml)
- **Ports**: Exposed on all interfaces
- **Security**: Basic setup for development
- **Redis**: No authentication

### Production (docker-compose-secure.yml)
- **Ports**: Bound to localhost only (127.0.0.1)
- **Security**: Full authentication and encryption
- **Redis**: Password protected
- **Application**: Runs as non-root user

## 📊 Monitoring & Health Checks

### Application Health

```bash
# Check overall health
curl http://localhost:7070/actuator/health

# Check specific components
curl http://localhost:7070/actuator/health/redis
curl http://localhost:7070/actuator/health/db
```

### Redis Monitoring

Access Redis Commander at http://localhost:8081 to:
- View cached data
- Monitor Redis performance
- Manage Redis keys
- Execute Redis commands

## 🔧 Configuration Profiles

### Available Profiles

1. **default**: Development configuration
2. **prod**: Production configuration with Docker
3. **secure**: Enhanced security configuration

### Switching Profiles

```bash
# Via environment variable
export SPRING_PROFILES_ACTIVE=secure

# Via application argument
java -jar app.jar --spring.profiles.active=secure

# Via Docker environment
docker run -e SPRING_PROFILES_ACTIVE=secure redis-test-app
```

## 🛠️ Troubleshooting

### Common Issues

**Redis Connection Failed**:
```bash
# Check Redis is running
docker ps | grep redis

# Check Redis connectivity
docker exec -it redis-server redis-cli ping
```

**JWT Authentication Issues**:
```bash
# Verify JWT secret is set
echo $JWT_SECRET_KEY

# Check application logs
docker logs redis-test-app-secure
```

**H2 Console Access Denied**:
- Ensure `H2_CONSOLE_ENABLED=true` in development
- Check if running in secure profile (H2 console disabled)

### Log Levels

```bash
# Enable debug logging
export LOG_LEVEL=DEBUG

# Check specific component logs
docker logs redis-test-app-secure | grep -i redis
docker logs redis-test-app-secure | grep -i jwt
```

## 📁 Project Structure

```
redis-test/
├── src/main/java/com/skyeagle/redis_test/
│   ├── config/          # Security, JWT, Redis configuration
│   ├── controller/      # REST API controllers
│   ├── model/          # JPA entities
│   ├── repository/     # Data repositories
│   ├── service/        # Business logic with caching
│   └── filter/         # JWT authentication filter
├── src/main/resources/
│   ├── application.yml              # Default configuration
│   ├── application-prod.yml         # Production configuration
│   └── application-secure.yml       # Secure configuration
├── docker-compose.yml              # Development setup
├── docker-compose-secure.yml       # Production setup
├── Dockerfile                      # Application container
├── .env.template                   # Environment variables template
└── README.md                      # This documentation
```

## 🔐 Security Features

- **JWT Authentication** with environment-based secret management
- **Password-protected Redis** in production
- **H2 Console** disabled in production
- **Non-root container** execution
- **Localhost-only** port bindings in secure mode
- **Environment variable** configuration for sensitive data
- **Security headers** and CORS configuration

## 📝 License

MIT License - see LICENSE file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit changes: `git commit -am 'Add new feature'`
4. Push to branch: `git push origin feature/your-feature`
5. Submit a Pull Request

## 📞 Support

For issues and questions:
- Create an issue on GitHub
- Check the troubleshooting section above
- Review application logs for error details
