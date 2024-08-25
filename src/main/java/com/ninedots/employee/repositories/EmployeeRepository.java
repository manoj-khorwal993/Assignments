package com.ninedots.employee.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninedots.employee.entities.Employee;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository {
    private static final String FILE_PATH = "employees.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Employee> employees = new ArrayList<>();

    public EmployeeRepository() {
        loadEmployees();
    }

    private void loadEmployees() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                employees = objectMapper.readValue(file, new TypeReference<List<Employee>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveEmployees() {
        try {
            objectMapper.writeValue(new File(FILE_PATH), employees);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Employee addEmployee(Employee employee) {
        employee.setId(employees.size() + 1); // Simple ID generation
        employees.add(employee);
        saveEmployees();
        return employee;
    }

    public Optional<Employee> findEmployeeById(int id) {
        return employees.stream().filter(e -> e.getId() == id).findFirst();
    }

    public List<Employee> findAllEmployees() {
        return employees;
    }

    public List<Employee> findEmployeesByCriteria(String name, Double fromSalary, Double toSalary) {
        return employees.stream().filter(e -> {
            boolean matches = true;
            if (name != null && !name.isEmpty()) {
                matches &= e.getFirstName().contains(name) || e.getLastName().contains(name);
            }
            if (fromSalary != null) {
                matches &= e.getSalary() >= fromSalary;
            }
            if (toSalary != null) {
                matches &= e.getSalary() <= toSalary;
            }
            return matches;
        }).toList();
    }
}