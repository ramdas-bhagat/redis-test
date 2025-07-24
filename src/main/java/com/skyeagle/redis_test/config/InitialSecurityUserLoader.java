package com.skyeagle.redis_test.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.skyeagle.redis_test.model.SecurityUser;
import com.skyeagle.redis_test.repository.SecurityUserRepository;

@Configuration
public class InitialSecurityUserLoader {

    @Bean
    public CommandLineRunner loadInitialSecurityUsers(
            PasswordEncoder passwordEncoder, SecurityUserRepository securityUserRepository) {
        return (args) -> {
            createUserIfNotExists(
                    "user", "User@123", Set.of("USER"), passwordEncoder, securityUserRepository);
            createUserIfNotExists(
                    "admin", "Admin@123", Set.of("ADMIN"), passwordEncoder, securityUserRepository);
        };
    }

    private void createUserIfNotExists(
            String username,
            String password,
            Set<String> roles,
            PasswordEncoder passwordEncoder,
            SecurityUserRepository securityUserRepository) {
        if (!securityUserRepository.existsByUsername(username)) {
            SecurityUser userDetails =
                    SecurityUser.builder()
                            .username(username)
                            .password(passwordEncoder.encode(password))
                            .roles(roles)
                            .active(true)
                            .build();
            securityUserRepository.save(userDetails);
        }
    }
}
