package ru.botsner.springboot.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.botsner.springboot.rest.entity.Employee;
import ru.botsner.springboot.rest.exception.EntityNotFoundException;
import ru.botsner.springboot.rest.service.EmployeeService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class EmployeeRESTControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee("Mike", "Smith", "IT", 1000);
        employee.setId(1);
    }

    @Test
    void mockMvcLoads() {
        assertNotNull(mockMvc);
    }

    @Test
    void listAllEmployees_getEmployees_status200() throws Exception {
        Employee emp1 = new Employee("John", "Miller", "HR", 1000);
        Employee emp2 = new Employee("Maria", "Brown", "IT", 1200);

        Mockito.doReturn(Arrays.asList(emp1, emp2))
                .when(employeeService)
                .getAllEmployees();

        mockMvc.perform(
                get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(emp1, emp2))));

        Mockito.verify(employeeService, Mockito.only()).getAllEmployees();
    }

    @Test
    void getEmployee_getExistingEmployee_status200andEmployeeReturned() throws Exception {
        Mockito.doReturn(employee)
                .when(employeeService)
                .getEmployee(Mockito.anyInt());

        mockMvc.perform(
                get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(employee)));

        Mockito.verify(employeeService, Mockito.only()).getEmployee(Mockito.anyInt());
    }

    @Test
    void getEmployee_getNotExistingEmployee_status404andExceptionThrown() throws Exception {
        Mockito.doReturn(null)
                .when(employeeService)
                .getEmployee(Mockito.anyInt());

        mockMvc.perform(
                get("/api/employees/1"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult ->
                        assertTrue(mvcResult.getResolvedException() instanceof EntityNotFoundException));
    }

    @Test
    void createEmployee_addNewEmployee_status201andEmployeeReturned() throws Exception {
        mockMvc.perform(
                post("/api/employees")
                        .content(objectMapper.writeValueAsString(employee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(employee)));

        Mockito.verify(employeeService, Mockito.only()).saveEmployee(Mockito.any(Employee.class));
    }


    @Test
    void updateEmployee_updateExistingEmployee_status200andUpdatedReturns() throws Exception {
        Mockito.doReturn(employee)
                .when(employeeService)
                .updateEmployee(Mockito.any(Employee.class), Mockito.anyInt());

        mockMvc.perform(
                put("/api/employees/1")
                        .content(objectMapper.writeValueAsString(new Employee()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Mike"))
                .andExpect(jsonPath("$.surname").value("Smith"));

        Mockito.verify(employeeService, Mockito.only()).updateEmployee(Mockito.any(Employee.class), Mockito.anyInt());
    }

    @Test
    void updateEmployee_updateNotExistingEmployee_status404andExceptionThrown() throws Exception {
        Mockito.doReturn(null)
                .when(employeeService)
                .updateEmployee(Mockito.any(Employee.class), Mockito.anyInt());

        mockMvc.perform(
                put("/api/employees/1")
                        .content(objectMapper.writeValueAsString(new Employee()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult ->
                        assertTrue(mvcResult.getResolvedException() instanceof EntityNotFoundException));
    }

    @Test
    void deleteEmployee_deleteExistingEmployee_status200andDeletedReturns() throws Exception {
        Mockito.doReturn(employee)
                .when(employeeService)
                .deleteEmployee(Mockito.anyInt());

        mockMvc.perform(
                delete("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(employee)));

        Mockito.verify(employeeService, Mockito.only()).deleteEmployee(Mockito.anyInt());
    }

    @Test
    void deleteEmployee_deleteNotExistingEmployee_status404andExceptionThrown() throws Exception {
        Mockito.doReturn(null)
                .when(employeeService)
                .deleteEmployee(Mockito.anyInt());

        mockMvc.perform(
                delete("/api/employees/1"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult ->
                        assertTrue(mvcResult.getResolvedException() instanceof EntityNotFoundException));
    }
}
