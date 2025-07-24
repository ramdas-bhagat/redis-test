package com.skyeagle.redis_test.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.skyeagle.redis_test.model.Employee;
import com.skyeagle.redis_test.repository.EmployeeRepository;

@Configuration
public class InitialEmployeeLoader {

    @Bean
    public CommandLineRunner loadInitialEmployees(EmployeeRepository employeeRepository) {
        return args -> {
            for (int i = 1; i <= 20; i++) {
                employeeRepository.save(
                        Employee.builder()
                                .firstName("FirstName" + i)
                                .lastName("LastName" + i)
                                .email("employee" + i + "@example.com")
                                .phoneNumber("123-456-789" + i)
                                .build());
            }
        };
    }
}
