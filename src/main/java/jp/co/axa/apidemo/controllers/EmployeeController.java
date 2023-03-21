package jp.co.axa.apidemo.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

        @ApiOperation(value = "Retrieve a list of employees", notes = "Can provide the page number as request parameter")
        @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", value = "page number with first page being 0")
        @ApiResponses({
                        @ApiResponse(code = 200, message = "OK", response = Response.class),
                        @ApiResponse(code = 400, message = "Invalid page parameter"),
                        @ApiResponse(code = 401, message = "Unauthorized")
        })
        @GetMapping("/employees")
        public ResponseEntity<Response<List<EmployeeDto>>> getEmployees(
                        @RequestParam Optional<Integer> page) {
                List<EmployeeDto> employees = employeeService.retrieveEmployees(page);
                LOGGER.info("Employees retrieved successfully");

                return ResponseEntity.ok(
                                new Response<>(
                                                LocalDateTime.now(),
                                                HttpStatus.OK,
                                                ImmutableMap.of("employees", employees)));
        }

        @ApiOperation(value = "Retrieve an employee by id")
        @ApiImplicitParam(name = "employeeId", dataType = "long", paramType = "path", value = "employee id", required = true)
        @ApiResponses({
                        @ApiResponse(code = 200, message = "OK", response = Response.class),
                        @ApiResponse(code = 400, message = "Invalid id parameter"),
                        @ApiResponse(code = 401, message = "Unauthorized"),
                        @ApiResponse(code = 404, message = "Employee not found")
        })
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

        @ApiOperation(value = "Create a new employee", notes = "Return created employee data")
        @ApiImplicitParam(name = "employeeDto", dataType = "EmployeeDto", paramType = "body", value = "employee data", required = true)
        @ApiResponses({
                        @ApiResponse(code = 201, message = "OK", response = Response.class),
                        @ApiResponse(code = 400, message = "Invalid body"),
                        @ApiResponse(code = 401, message = "Unauthorized"),
                        @ApiResponse(code = 403, message = "Forbidden")
        })
        @ResponseStatus(code = HttpStatus.CREATED)
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

        @ApiOperation(value = "Delete an employee by id")
        @ApiImplicitParam(name = "employeeId", dataType = "long", paramType = "path", value = "employee id", required = true)
        @ApiResponses({
                        @ApiResponse(code = 200, message = "OK", response = Response.class),
                        @ApiResponse(code = 400, message = "Invalid body"),
                        @ApiResponse(code = 401, message = "Unauthorized"),
                        @ApiResponse(code = 403, message = "Forbidden"),
                        @ApiResponse(code = 404, message = "Employee not found")
        })
        @DeleteMapping("/employees/{employeeId}")
        public ResponseEntity<Response<Void>> deleteEmployee(@PathVariable Long employeeId) {
                employeeService.deleteEmployee(employeeId);
                LOGGER.info("Employee Deleted Successfully");

                return ResponseEntity.ok(
                                new Response<>(LocalDateTime.now(), HttpStatus.OK));
        }

        @ApiOperation(value = "Update an employee by id")
        @ApiImplicitParams({
                        @ApiImplicitParam(name = "employeeId", dataType = "long", paramType = "path", value = "employee id", required = true),
                        @ApiImplicitParam(name = "employeeDto", dataType = "EmployeeDto", paramType = "body", value = "employee data", required = true)
        })
        @ApiResponses({
                        @ApiResponse(code = 200, message = "OK", response = Response.class),
                        @ApiResponse(code = 400, message = "Invalid body"),
                        @ApiResponse(code = 401, message = "Unauthorized"),
                        @ApiResponse(code = 403, message = "Forbidden"),
                        @ApiResponse(code = 404, message = "Employee not found")
        })
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
