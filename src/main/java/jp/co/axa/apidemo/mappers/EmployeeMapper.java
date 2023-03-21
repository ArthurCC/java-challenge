package jp.co.axa.apidemo.mappers;

import org.springframework.stereotype.Component;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.model.EmployeeDto;

/**
 * Mapper class to convert Employee entities to/from Employee data transfer
 * objects.
 * 
 * It is generally good practice to separate JPA entities from data transfer
 * objects
 * 
 * @author Arthur Campos Costa
 */
@Component
public class EmployeeMapper {

    /**
     * Convert employee entity to DTO
     * 
     * @param employee employee entity
     * @return EmployeeDto instance
     */
    public EmployeeDto toDto(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getSalary(),
                employee.getDepartment());
    }

    /**
     * populate employee entity with DTO data
     * 
     * @param employee    employee entity to populate
     * @param employeeDto employee DTO
     * @return the populated employee entity (not necessary but simplify code for
     *         save operations)
     */
    public Employee populateEntity(Employee employee, EmployeeDto employeeDto) {
        employee.setName(employeeDto.getName());
        employee.setSalary(employeeDto.getSalary());
        employee.setDepartment(employeeDto.getDepartment());

        return employee;
    }
}
