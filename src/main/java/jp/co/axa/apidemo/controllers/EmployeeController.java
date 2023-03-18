package jp.co.axa.apidemo.controllers;

import java.time.LocalDateTime;
import java.util.List;

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

import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
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
        return ResponseEntity.ok(
                new Response<>(
                        LocalDateTime.now(),
                        HttpStatus.OK,
                        ImmutableMap.of("employees", employeeService.retrieveEmployees())));
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Response<EmployeeDto>> getEmployee(@PathVariable(name = "employeeId") Long employeeId)
            throws ResourceNotFoundException {

        return ResponseEntity.ok(
                new Response<>(
                        LocalDateTime.now(),
                        HttpStatus.OK,
                        ImmutableMap.of("employee", employeeService.getEmployee(employeeId))));
        // return employeeService.getEmployee(employeeId);
    }

    @PostMapping("/employees")
    public ResponseEntity<Response<EmployeeDto>> saveEmployee(@RequestBody EmployeeDto employeeDto) {
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
    public ResponseEntity<Response<Void>> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId)
            throws ResourceNotFoundException {
        employeeService.deleteEmployee(employeeId);
        LOGGER.info("Employee Deleted Successfully");

        return ResponseEntity.ok(
                new Response<>(LocalDateTime.now(), HttpStatus.OK));
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<Response<EmployeeDto>> updateEmployee(
            @RequestBody EmployeeDto employeeDto,
            @PathVariable(name = "employeeId") Long employeeId)
            throws ResourceNotFoundException {
        EmployeeDto updatedEmployee = employeeService.updateEmployee(employeeId, employeeDto);
        LOGGER.info("Employee updated successfully");

        return ResponseEntity.ok(
                new Response<>(
                        LocalDateTime.now(),
                        HttpStatus.OK,
                        ImmutableMap.of("employee", updatedEmployee)));
    }
}
