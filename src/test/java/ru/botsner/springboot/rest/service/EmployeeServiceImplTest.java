package ru.botsner.springboot.rest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.botsner.springboot.rest.dao.EmployeeRepository;
import ru.botsner.springboot.rest.entity.Employee;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmployeeServiceImplTest {

    @Autowired
    private EmployeeService empService;

    @MockBean
    private EmployeeRepository empRepo;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee("Mike", "Smith", "IT", 1000);
        employee.setId(1);
    }

    @Test
    void getAllEmployees() {
        Employee emp1 = new Employee("John", "Miller", "HR", 1000);
        Employee emp2 = new Employee("Maria", "Brown", "IT", 1200);

        Mockito.doReturn(Arrays.asList(emp1, emp2))
                .when(empRepo)
                .findAll();

        assertIterableEquals(Arrays.asList(emp1, emp2), empService.getAllEmployees());

        Mockito.verify(empRepo, Mockito.times(1)).findAll();
    }

    @Test
    void getEmployee() {
        Mockito.doReturn(Optional.of(employee))
                .when(empRepo)
                .findById(1);

        assertSame(employee, empService.getEmployee(1));

        Mockito.verify(empRepo, Mockito.times(1)).findById(1);
    }

    @Test
    void getEmployee_getNotExistingEmployee_null() {
        Mockito.doReturn(Optional.empty())
                .when(empRepo)
                .findById(5);

        assertNull(empService.getEmployee(5));

        Mockito.verify(empRepo, Mockito.times(1)).findById(5);
    }

    @Test
    void saveEmployee() {
        empService.saveEmployee(employee);

        Mockito.verify(empRepo, Mockito.times(1)).save(employee);
    }

    @Test
    void updateEmployee() {
        Employee updatedEmp = new Employee();

        Mockito.doReturn(Optional.of(employee))
                .when(empRepo)
                .findById(1);

        empService.updateEmployee(updatedEmp, 1);

        assertEquals(1, updatedEmp.getId());

        Mockito.verify(empRepo, Mockito.times(1)).findById(1);
        Mockito.verify(empRepo, Mockito.times(1)).save(updatedEmp);
    }

    @Test
    void updateEmployee_updateNotExistingEmployee_null() {
        Mockito.doReturn(Optional.empty())
                .when(empRepo)
                .findById(5);

        assertNull(empService.updateEmployee(employee, 5));

        Mockito.verify(empRepo, Mockito.times(1)).findById(5);
        Mockito.verify(empRepo, Mockito.times(0)).save(Mockito.any(Employee.class));
    }

    @Test
    void deleteEmployee() {
        Mockito.doReturn(Optional.of(employee))
                .when(empRepo)
                .findById(1);

        assertSame(employee, empService.deleteEmployee(1));

        Mockito.verify(empRepo, Mockito.times(1)).findById(1);
        Mockito.verify(empRepo, Mockito.times(1)).deleteById(1);
    }

    @Test
    void deleteEmployee_deleteNotExistingEmployee_null() {
        Mockito.doReturn(Optional.empty())
                .when(empRepo)
                .findById(5);

        assertNull(empService.deleteEmployee(5));

        Mockito.verify(empRepo, Mockito.times(1)).findById(5);
        Mockito.verify(empRepo, Mockito.times(0)).deleteById(Mockito.anyInt());
    }
}