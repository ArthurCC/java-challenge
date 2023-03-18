package jp.co.axa.apidemo.services;

import java.util.List;
import java.util.Optional;

import jp.co.axa.apidemo.model.EmployeeDto;

public interface EmployeeService {

    public List<EmployeeDto> retrieveEmployees(Optional<Integer> page);

    public EmployeeDto getEmployee(Long employeeId);

    public EmployeeDto saveEmployee(EmployeeDto employeeDto);

    public void deleteEmployee(Long employeeId);

    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto);
}