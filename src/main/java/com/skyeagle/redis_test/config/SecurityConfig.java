package com.skyeagle.redis_test.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.skyeagle.redis_test.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(
                cors ->
                        cors.configurationSource(
                                request -> {
                                    var corsConfig =
                                            new org.springframework.web.cors.CorsConfiguration();
                                    // corsConfig.setAllowedOrigins(List.of("http://localhost:4200"
                                    // Allow all origins for Cloud Run deployment (adjust for
                                    // production as needed)
                                    corsConfig.setAllowedOriginPatterns(List.of("*"));
                                    corsConfig.setAllowedMethods(
                                            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                                    corsConfig.setAllowedHeaders(
                                            List.of("Authorization", "Content-Type"));
                                    return corsConfig;
                                }));

        http.authorizeHttpRequests(
                auth ->
                        auth.requestMatchers("/api/v1/auth/**")
                                .permitAll()
                                .requestMatchers(
                                        "/index.html",
                                        "/css/**",
                                        "/js/**",
                                        "/h2-console/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/actuator/health")
                                .permitAll()
                                .anyRequest()
                                .authenticated());

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement(
                manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}
