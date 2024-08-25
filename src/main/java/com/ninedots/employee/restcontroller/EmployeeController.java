package com.ninedots.employee.restcontroller;


import com.ninedots.employee.entities.Employee;
import com.ninedots.employee.repositories.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeRepository.addEmployee(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
        Optional<Employee> employee = employeeRepository.findEmployeeById(id);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Double fromSalary,
        @RequestParam(required = false) Double toSalary) {
        List<Employee> employees = employeeRepository.findEmployeesByCriteria(name, fromSalary, toSalary);
        return ResponseEntity.ok(employees);
    }
}