package jp.co.axa.apidemo.mappers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.model.EmployeeDto;

public class EmployeeMapperTest {

    private final EmployeeMapper employeeMapper = new EmployeeMapper();

    @Test
    public void toDtoTest() {
        Employee employee = new Employee(1L, "employee 1", 1000, "Marketing");

        EmployeeDto out = employeeMapper.toDto(employee);

        assertEquals(employee.getId(), out.getId());
        assertEquals(employee.getName(), out.getName());
        assertEquals(employee.getSalary(), out.getSalary());
        assertEquals(employee.getDepartment(), out.getDepartment());
    }

    @Test
    public void populateEntityTest() {
        Employee employee = new Employee(1L, "employee 1", 1000, "Marketing");
        EmployeeDto updatedEmployeeDto = new EmployeeDto(2L, "employee 2", 2000, "Accounting");

        Employee out = employeeMapper.populateEntity(employee, updatedEmployeeDto);

        // id is never updated
        assertEquals(employee.getId(), out.getId());
        assertEquals(updatedEmployeeDto.getName(), out.getName());
        assertEquals(updatedEmployeeDto.getSalary(), out.getSalary());
        assertEquals(updatedEmployeeDto.getDepartment(), out.getDepartment());
    }
}
