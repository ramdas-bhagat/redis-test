package com.skyeagle.redis_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skyeagle.redis_test.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    public Employee findByEmail(String email);

    public Employee findByPhoneNumber(String phoneNumber);

    public Employee findByFirstNameAndLastName(String firstName, String lastName);
}
