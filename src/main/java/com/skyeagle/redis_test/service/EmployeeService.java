package com.skyeagle.redis_test.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.skyeagle.redis_test.model.Employee;
import com.skyeagle.redis_test.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private com.skyeagle.redis_test.repository.EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Cacheable(value = "employees", key = "#id")
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @CachePut(value = "employees", key = "#employee.id")
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @CacheEvict(value = "employees", key = "#id")
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @CachePut(value = "employees", key = "#id")
    public Employee updateEmployee(Long id, Employee employee) {
        if (employeeRepository.existsById(id)) {
            employee.setId(id);
            return employeeRepository.save(employee);
        }
        return null;
    }
}
