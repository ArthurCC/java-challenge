package jp.co.axa.apidemo.mappers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.model.EmployeeDto;

/**
 * Test class for EmployeeMapper
 * 
 * @author Arthur Campos Costa
 */
public class EmployeeMapperTest {

    /** employee mapper subject */
    private final EmployeeMapper employeeMapper = new EmployeeMapper();

    /**
     * Test convert Entity to DTO
     */
    @Test
    public void toDtoTest() {
        Employee employee = new Employee(1L, "employee 1", 1000, "Marketing");

        // test
        EmployeeDto out = employeeMapper.toDto(employee);

        // assert
        assertEquals(employee.getId(), out.getId());
        assertEquals(employee.getName(), out.getName());
        assertEquals(employee.getSalary(), out.getSalary());
        assertEquals(employee.getDepartment(), out.getDepartment());
    }

    /**
     * Test populate Entity from DTO
     */
    @Test
    public void populateEntityTest() {
        Employee employee = new Employee(1L, "employee 1", 1000, "Marketing");
        EmployeeDto updatedEmployeeDto = new EmployeeDto(2L, "employee 2", 2000, "Accounting");

        // test
        Employee out = employeeMapper.populateEntity(employee, updatedEmployeeDto);

        // assert
        // verify id is never updated by DTO, DB should handle id
        assertEquals(employee.getId(), out.getId());
        assertEquals(updatedEmployeeDto.getName(), out.getName());
        assertEquals(updatedEmployeeDto.getSalary(), out.getSalary());
        assertEquals(updatedEmployeeDto.getDepartment(), out.getDepartment());
    }
}
