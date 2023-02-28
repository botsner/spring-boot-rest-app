package ru.botsner.springboot.rest.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.botsner.springboot.rest.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findAllByName(String name);
}
