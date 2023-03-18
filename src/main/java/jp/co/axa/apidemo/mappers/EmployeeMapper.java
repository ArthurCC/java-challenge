package jp.co.axa.apidemo.mappers;

import org.springframework.stereotype.Component;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.model.EmployeeDto;

/**
 * Mapper class from Employee entity to DTO
 */
@Component
public class EmployeeMapper {

    public EmployeeDto toDto(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getSalary(),
                employee.getDepartment());
    }

    public Employee populateEntity(Employee employee, EmployeeDto employeeDto) {
        employee.setName(employeeDto.getName());
        employee.setSalary(employeeDto.getSalary());
        employee.setDepartment(employeeDto.getDepartment());

        return employee;
    }
}
