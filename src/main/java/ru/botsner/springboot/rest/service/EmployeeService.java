package ru.botsner.springboot.rest.service;


import ru.botsner.springboot.rest.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();

    Employee getEmployee(int id);

    void saveEmployee(Employee employee);

    Employee updateEmployee(Employee employee, int id);

    Employee deleteEmployee(int id);
}
