package jp.co.axa.apidemo.controllers;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

import jp.co.axa.apidemo.model.EmployeeDto;
import jp.co.axa.apidemo.model.Response;
import jp.co.axa.apidemo.services.EmployeeService;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public ResponseEntity<Response<List<EmployeeDto>>> getEmployees() {
        List<EmployeeDto> employees = employeeService.retrieveEmployees();
        LOGGER.info("Employees retrieved successfully");

        return ResponseEntity.ok(
                new Response<>(
                        LocalDateTime.now(),
                        HttpStatus.OK,
                        ImmutableMap.of("employees", employees)));
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Response<EmployeeDto>> getEmployee(@PathVariable Long employeeId) {
        EmployeeDto employee = employeeService.getEmployee(employeeId);
        LOGGER.info("Employee retrieved successfully");

        return ResponseEntity.ok(
                new Response<>(
                        LocalDateTime.now(),
                        HttpStatus.OK,
                        ImmutableMap.of("employee", employee)));
    }

    @PostMapping("/employees")
    public ResponseEntity<Response<EmployeeDto>> saveEmployee(
            @RequestBody @Valid EmployeeDto employeeDto) {
        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto);
        LOGGER.info("Employee Saved Successfully");

        return new ResponseEntity<>(
                new Response<>(
                        LocalDateTime.now(),
                        HttpStatus.CREATED,
                        ImmutableMap.of("employee", savedEmployee)),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Response<Void>> deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        LOGGER.info("Employee Deleted Successfully");

        return ResponseEntity.ok(
                new Response<>(LocalDateTime.now(), HttpStatus.OK));
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<Response<EmployeeDto>> updateEmployee(
            @RequestBody @Valid EmployeeDto employeeDto,
            @PathVariable Long employeeId) {
        EmployeeDto updatedEmployee = employeeService.updateEmployee(employeeId, employeeDto);
        LOGGER.info("Employee updated successfully");

        return ResponseEntity.ok(
                new Response<>(
                        LocalDateTime.now(),
                        HttpStatus.OK,
                        ImmutableMap.of("employee", updatedEmployee)));
    }
}
