package com.skyeagle.redis_test.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Spring JWT", version = "1.0"),
        security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(
        name = "bearerAuth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT")
public class OpenApiConfig {

    // This class is used to configure OpenAPI documentation for the application.
    // The @OpenAPIDefinition annotation specifies the title and version of the API.
    // The @SecurityScheme annotation defines the security scheme for JWT
    // authentication.
    // The @SecurityRequirement annotation indicates that the API requires
    // authentication using the specified scheme.

    // You can customize the title, version, and other details as per your
    // requirements.
    // The security scheme name "bearerAuth" is used to identify the JWT
    // authentication method.
    // The scheme is set to "bearer" and the type is set to HTTP.
    // The bearerFormat is set to "JWT" to indicate that the token format is JWT.
    // This configuration will be used by the OpenAPI documentation generator to
    // include security information in the generated documentation.
}
