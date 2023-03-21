package jp.co.axa.apidemo.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import jp.co.axa.apidemo.model.EmployeeDto;
import jp.co.axa.apidemo.services.EmployeeService;

/**
 * Test class for EmployeeController
 * We use WebMvcTest to load Application Context partially for this controller
 * 
 * @author Arthur Campos Costa
 */
@RunWith(SpringRunner.class)
// secure = false to prevent loading security context causing userDetail service
// bean definition error. Deprecated but couldn't find a workaround
@WebMvcTest(controllers = EmployeeController.class, secure = false)
public class EmployeeControllerTest {

    /**
     * mock mvc, allows to perform http operations on our controller and verify
     * responses
     */
    @Autowired
    private MockMvc mockMvc;

    /** employee service */
    @MockBean
    private EmployeeService employeeService;

    /**
     * Test get list of employees
     * 
     * Verify that 3 employees are returned in the response
     * @throws Exception exception
     */
    @Test
    public void getEmployeesTest() throws Exception {
        // mock
        when(employeeService.retrieveEmployees(Optional.empty()))
            .thenReturn(
                Lists.newArrayList(
                    new EmployeeDto(1L, "employee 1", 1000, "Marketing"),
                    new EmployeeDto(2L, "employee 2", 2000, "Accounting"),
                    new EmployeeDto(3L, "employee 3", 3000, "Development")
                )
            );

        // Perform GET request and verify response
        mockMvc.perform(get("/api/v1/employees"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.employees", hasSize(3)))
            .andExpect(jsonPath("$.data.employees[0].name", is("employee 1")))
            .andExpect(jsonPath("$.data.employees[1].name", is("employee 2")))
            .andExpect(jsonPath("$.data.employees[2].name", is("employee 3")));

        // verify service called
        verify(employeeService, times(1))
            .retrieveEmployees(Optional.empty());
    }

    /**
     * Test get employee
     * 
     * Verify that a single employee is return in the response
     * @throws Exception exception
     */
    @Test
    public void getEmployeeTest() throws Exception {
        // mock
        when(employeeService.getEmployee(eq(1L)))
            .thenReturn(new EmployeeDto(1L, "employee 1", 1000, "Marketing"));

        // Perform GET request and verify response
        mockMvc.perform(get("/api/v1/employees/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.employee.name", is("employee 1")));

        // verify service call
        verify(employeeService, times(1))
            .getEmployee(eq(1L));
    }

    /**
     * Test save a new employee
     * 
     * Verify that the new employee is returned in the response
     * 
     * @throws JsonProcessingException if error while converting dto to String
     * @throws Exception               exception
     */
    @Test
    public void saveEmployeeTest() throws JsonProcessingException, Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        EmployeeDto employeeDto = new EmployeeDto(1L, "employee 1", 1000, "Marketing");

        // mock
        when(employeeService.saveEmployee(argThat(
                e -> e.getId() == employeeDto.getId())))
                .thenReturn(employeeDto);

        // Perform POST request and verify response
        mockMvc.perform(
                post("/api/v1/employees")
                        .content(objectMapper.writeValueAsString(employeeDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.employee.name", is("employee 1")));

        // verify service call
        verify(employeeService, times(1))
                .saveEmployee(any(EmployeeDto.class));
    }

    /**
     * Test delete an existing employee
     * 
     * Verify that response data is empty
     * 
     * @throws Exception exception
     */
    @Test
    public void deleteEmployeeTest() throws Exception {
        // Perform DELETE request and verify response
        mockMvc.perform(delete("/api/v1/employees/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", nullValue()));

        // verify service called
        verify(employeeService, times(1))
                .deleteEmployee(eq(1L));
    }

    /**
     * Test update an existing employee
     * 
     * Verify that the updated employee is returned in the response
     * 
     * @throws JsonProcessingException if error while converting dto to String
     * @throws Exception               exception
     */
    @Test
    public void updateEmployeeTest() throws JsonProcessingException, Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        EmployeeDto employeeDto = new EmployeeDto(1L, "employee 1", 1000, "Marketing");

        // mock
        when(employeeService.updateEmployee(
                eq(1L),
                argThat(
                        e -> e.getId() == employeeDto.getId())))
                .thenReturn(employeeDto);

        // perform PUT request and verify response
        mockMvc.perform(
                put("/api/v1/employees/1")
                        .content(objectMapper.writeValueAsString(employeeDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.employee.name", is("employee 1")));

        // verify service call
        verify(employeeService, times(1))
                .updateEmployee(eq(1L), any(EmployeeDto.class));
    }
}
