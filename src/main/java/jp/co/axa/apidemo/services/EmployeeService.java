package jp.co.axa.apidemo.services;

import java.util.List;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;

public interface EmployeeService {

    public List<Employee> retrieveEmployees();

    public Employee getEmployee(Long employeeId);

    public void saveEmployee(Employee employee);

    public void deleteEmployee(Long employeeId);

    public void updateEmployee(Long employeeId, Employee employee) throws ResourceNotFoundException;
}