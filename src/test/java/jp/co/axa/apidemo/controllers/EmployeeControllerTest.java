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

@RunWith(SpringRunner.class)
// secure = false to prevent loading security context causing userDetail service
// bean definition error. Deprecated but couldn't find a workaround
@WebMvcTest(controllers = EmployeeController.class, secure = false)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void getEmployeesTest() throws Exception {
        when(employeeService.retrieveEmployees(Optional.empty()))
            .thenReturn(
                Lists.newArrayList(
                    new EmployeeDto(1L, "employee 1", 1000, "Marketing"),
                    new EmployeeDto(2L, "employee 2", 2000, "Accounting"),
                    new EmployeeDto(3L, "employee 3", 3000, "Development")
                )
            );

        mockMvc.perform(get("/api/v1/employees"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.employees", hasSize(3)))
            .andExpect(jsonPath("$.data.employees[0].name", is("employee 1")))
            .andExpect(jsonPath("$.data.employees[1].name", is("employee 2")))
            .andExpect(jsonPath("$.data.employees[2].name", is("employee 3")));

        verify(employeeService, times(1))
            .retrieveEmployees(Optional.empty());
    }

    @Test
    public void getEmployeeTest() throws Exception {
        when(employeeService.getEmployee(eq(1L)))
            .thenReturn(new EmployeeDto(1L, "employee 1", 1000, "Marketing"));

        mockMvc.perform(get("/api/v1/employees/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.employee.name", is("employee 1")));

        verify(employeeService, times(1))
            .getEmployee(eq(1L));
    }

    @Test
    public void saveEmployeeTest() throws JsonProcessingException, Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        EmployeeDto employeeDto = new EmployeeDto(1L, "employee 1", 1000, "Marketing");

        when(employeeService.saveEmployee(argThat(
                e -> e.getId() == employeeDto.getId())))
                .thenReturn(employeeDto);

        mockMvc.perform(
                post("/api/v1/employees")
                        .content(objectMapper.writeValueAsString(employeeDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.employee.name", is("employee 1")));

        verify(employeeService, times(1))
                .saveEmployee(any(EmployeeDto.class));
    }

    @Test
    public void deleteEmployeeTest() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", nullValue()));

        verify(employeeService, times(1))
                .deleteEmployee(eq(1L));
    }

    @Test
    public void updateEmployeeTest() throws JsonProcessingException, Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        EmployeeDto employeeDto = new EmployeeDto(1L, "employee 1", 1000, "Marketing");

        when(employeeService.updateEmployee(
                eq(1L),
                argThat(
                        e -> e.getId() == employeeDto.getId())))
                .thenReturn(employeeDto);

        mockMvc.perform(
                put("/api/v1/employees/1")
                        .content(objectMapper.writeValueAsString(employeeDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.employee.name", is("employee 1")));

        verify(employeeService, times(1))
                .updateEmployee(eq(1L), any(EmployeeDto.class));
    }
}
