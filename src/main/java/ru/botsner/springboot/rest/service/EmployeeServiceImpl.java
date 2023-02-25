package ru.botsner.springboot.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.botsner.springboot.rest.dao.EmployeeRepository;
import ru.botsner.springboot.rest.entity.Employee;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployee(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.orElse(null);
    }

    @Override
    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee, int id) {
        Optional<Employee> emp = employeeRepository.findById(id);
        if (emp.isEmpty()) {
            return null;
        }
        employee.setId(id);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee deleteEmployee(int id) {
        Optional<Employee> deletedEmp = employeeRepository.findById(id);
        if (deletedEmp.isEmpty()) {
            return null;
        }
        employeeRepository.deleteById(id);
        return deletedEmp.get();
    }
}
