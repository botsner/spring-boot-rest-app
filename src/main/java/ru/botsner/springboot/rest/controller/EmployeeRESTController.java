package ru.botsner.springboot.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.botsner.springboot.rest.entity.Employee;
import ru.botsner.springboot.rest.exception.EntityNotFoundException;
import ru.botsner.springboot.rest.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRESTController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeRESTController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> listAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok().body(employees);
    }

    @GetMapping("/{empId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable int empId) {
        Employee employee = employeeService.getEmployee(empId);

        if (employee == null) {
            throw new EntityNotFoundException(empId);
        }
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        employeeService.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @PutMapping("/{empId}")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee, @PathVariable int empId) {
        Employee updatedEmployee = employeeService.updateEmployee(employee, empId);

        if (updatedEmployee == null) {
            throw new EntityNotFoundException(empId);
        }
        return ResponseEntity.ok().body(updatedEmployee);
    }

    @DeleteMapping("/{empId}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable int empId) {
        Employee deletedEmp = employeeService.deleteEmployee(empId);

        if (deletedEmp == null) {
            throw new EntityNotFoundException(empId);
        }
        return ResponseEntity.ok().body(deletedEmp);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Employee>> listAllEmployeesByName(@PathVariable("name") String name) {
        List<Employee> employees = employeeService.getAllEmployeesByName(name);
        return ResponseEntity.ok().body(employees);
    }
}
