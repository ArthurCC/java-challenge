package jp.co.axa.apidemo.services;

import java.util.List;
import java.util.Optional;

import jp.co.axa.apidemo.model.EmployeeDto;

/**
 * Employee service interface defining operations for managing employee related
 * data
 * 
 * @author Arthur Campos Costa
 */
public interface EmployeeService {

    /**
     * Retrieve a list of employees from DB
     * 
     * @param page page number, optional
     * @return List of employees
     */
    public List<EmployeeDto> retrieveEmployees(Optional<Integer> page);

    /**
     * Retrieve an employee from DB
     * 
     * @param employeeId employee id
     * @return Employee
     */
    public EmployeeDto getEmployee(Long employeeId);

    /**
     * Create a new employee in DB
     * 
     * @param employeeDto employee data
     * @return Employee dto of created employee
     */
    public EmployeeDto saveEmployee(EmployeeDto employeeDto);

    /**
     * Delete an existing employee from DB
     * 
     * @param employeeId employee id
     */
    public void deleteEmployee(Long employeeId);

    /**
     * Update an existing employee in DB
     * 
     * @param employeeId  employee id
     * @param employeeDto employee data
     * @return Employee DTO of updated employee
     */
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto);
}