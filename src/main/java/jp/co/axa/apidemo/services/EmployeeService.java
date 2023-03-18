package jp.co.axa.apidemo.services;

import java.util.List;

import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.model.EmployeeDto;

public interface EmployeeService {

    public List<EmployeeDto> retrieveEmployees();

    public EmployeeDto getEmployee(Long employeeId) throws ResourceNotFoundException;

    public EmployeeDto saveEmployee(EmployeeDto employeeDto);

    public void deleteEmployee(Long employeeId) throws ResourceNotFoundException;

    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto) throws ResourceNotFoundException;
}