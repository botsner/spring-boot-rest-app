package ru.botsner.springboot.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.botsner.springboot.rest.entity.Employee;
import ru.botsner.springboot.rest.exception.EntityNotFoundException;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/employee-test-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/employee-test-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class EmployeeRESTControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void mockMvcLoads() {
        assertNotNull(mockMvc);
    }

    @Test
    void listAllEmployees_getEmployees_status200() throws Exception {
        mockMvc.perform(
                get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void getEmployee_getExistingEmployee_status200andEmployeeReturned() throws Exception {
        mockMvc.perform(
                get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Smith"))
                .andExpect(jsonPath("$.department").value("IT"));
    }

    @Test
    void getEmployee_getNotExistingEmployee_status404andExceptionThrown() throws Exception {
        mockMvc.perform(
                get("/api/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult ->
                        assertTrue(mvcResult.getResolvedException() instanceof EntityNotFoundException));
    }

    @Test
    void createEmployee_addNewEmployee_status201andEmployeeReturned() throws Exception {
        Employee employee = new Employee("Nick", "Taylor", "HR", 555);

        mockMvc.perform(
                post("/api/employees")
                        .content(objectMapper.writeValueAsString(employee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Nick"))
                .andExpect(jsonPath("$.surname").value("Taylor"))
                .andExpect(jsonPath("$.salary").value(555));
    }


    @Test
    void updateEmployee_updateExistingEmployee_status200andUpdatedReturns() throws Exception {
        Employee employee = new Employee("Nick", "Taylor", "IT", 100);

        mockMvc.perform(
                put("/api/employees/1")
                        .content(objectMapper.writeValueAsString(employee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nick"));
    }

    @Test
    void updateEmployee_updateNotExistingEmployee_status404andExceptionThrown() throws Exception {
        mockMvc.perform(
                put("/api/employees/99")
                        .content(objectMapper.writeValueAsString(new Employee()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult ->
                        assertTrue(mvcResult.getResolvedException() instanceof EntityNotFoundException));
    }

    @Test
    void deleteEmployee_deleteExistingEmployee_status200andDeletedReturns() throws Exception {
        mockMvc.perform(
                delete("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void deleteEmployee_deleteNotExistingEmployee_status404andExceptionThrown() throws Exception {
        mockMvc.perform(
                delete("/api/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult ->
                        assertTrue(mvcResult.getResolvedException() instanceof EntityNotFoundException));
    }
}