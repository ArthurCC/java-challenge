package jp.co.axa.apidemo.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.google.common.collect.Lists;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.mappers.EmployeeMapper;
import jp.co.axa.apidemo.mappers.EmployeeMapperTest;
import jp.co.axa.apidemo.model.EmployeeDto;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.services.EmployeeService;

/**
 * Test class for EmployeeService. No Spring context here we simply mock the
 * repository.
 * 
 * Note : we don't verify each and every field of employee objects here because
 * this is
 * already been verified in {@link EmployeeMapperTest}
 * 
 * @author Arthur Campos Costa
 */
@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest {

    /** rule for verifying exceptions */
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    /** Employee argument captor */
    @Captor
    public ArgumentCaptor<Employee> employeeCaptor;

    /** employee repository */
    @Mock
    private EmployeeRepository employeeRepository;

    /** employee service, subject */
    private EmployeeService employeeService;

    /**
     * Initialize subject and inject dependencies
     */
    @Before
    public void init() {
        employeeService = new EmployeeServiceImpl(employeeRepository,
                new EmployeeMapper(), 2);
    }

    /**
     * Test retrieve all employees
     */
    @Test
    public void testRetrieveEmployeesAll() {
        // mock
        when(employeeRepository.findAll())
            .thenReturn(Lists.newArrayList(
                new Employee(1L, "employee 1", 1000, "Marketing"),
                new Employee(2L, "employee 2", 2000, "Accounting"),
                new Employee(3L, "employee 3", 3000, "Development")
            ));

        // test
        List<EmployeeDto> out = employeeService.retrieveEmployees(Optional.empty());

        // assert
        assertEquals(3, out.size());
        verify(employeeRepository, times(1)).findAll();
    }

    /**
     * Test retrieve a page of employees
     */
    @Test
    public void testRetrieveEmployeesPage() {
        int pageNumber = 0;

        // mock
        when(employeeRepository.findAll(argThat(
                (Pageable p) -> p.getPageSize() == 2 && p.getPageNumber() == pageNumber)))
                .thenReturn(new PageImpl<>(
                        Lists.newArrayList(
                                new Employee(1L, "employee 1", 1000, "Marketing"),
                                new Employee(2L, "employee 2", 2000, "Accounting"))));

        // test
        List<EmployeeDto> out = employeeService.retrieveEmployees(Optional.of(pageNumber));

        // assert
        assertEquals(2, out.size());
        verify(employeeRepository, times(1)).findAll(any(Pageable.class));
    }

    /**
     * Test retrieve a single employee
     */
    @Test
    public void testGetEmployee() {
        Long employeeId = 1L;

        // mock
        when(employeeRepository.findById(eq(employeeId)))
                .thenReturn(Optional.of(
                        new Employee(1L, "employee 1", 1000, "Marketing")));

        // test
        EmployeeDto out = employeeService.getEmployee(employeeId);

        // assert
        assertEquals(employeeId, out.getId());
        verify(employeeRepository, times(1)).findById(eq(employeeId));
    }

    /**
     * Test retrieve employee with ResourceNotFoundException thrown
     */
    @Test
    public void testGetEmployeeNotFound() {
        Long employeeId = 1L;

        // mock
        when(employeeRepository.findById(eq(employeeId)))
                .thenReturn(Optional.empty());

        // setup expected exception
        exceptionRule.expect(ResourceNotFoundException.class);
        exceptionRule.expectMessage("Employee does not exist");

        // test
        employeeService.getEmployee(employeeId);
    }

    /**
     * Test create a new employee
     */
    @Test
    public void saveEmployeeTest() {
        EmployeeDto employeeDto = new EmployeeDto(null, "employee 1", 1000, "Marketing");

        // mock
        when(employeeRepository.save(argThat(
                e -> e.getName().equals(employeeDto.getName())))).thenReturn(
                        new Employee(1L, "employee 1", 1000, "Marketing"));

        // test
        EmployeeDto out = employeeService.saveEmployee(employeeDto);

        // assert
        assertEquals(1L, out.getId().longValue());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    /**
     * Test delete an existing employee
     */
    @Test
    public void deleteEmployeeTest() {
        Long employeeId = 1L;

        // mock
        when(employeeRepository.findById(eq(employeeId)))
                .thenReturn(Optional.of(
                        new Employee(1L, "employee 1", 1000, "Marketing")));

        // test
        employeeService.deleteEmployee(employeeId);

        // assert
        // capture argument
        verify(employeeRepository, times(1))
                .delete(employeeCaptor.capture());

        assertEquals(employeeId, employeeCaptor.getValue().getId());
    }

    /**
     * Test delete an existing employee with ResourceNotFoundException thrown
     */
    @Test
    public void deleteEmployeeNotFoundTest() {
        Long employeeId = 1L;

        // mock
        when(employeeRepository.findById(eq(employeeId)))
                .thenReturn(Optional.empty());

        // setup expected exception
        exceptionRule.expect(ResourceNotFoundException.class);
        exceptionRule.expectMessage("Employee does not exist");

        // test
        employeeService.deleteEmployee(employeeId);
    }

    /**
     * Test update an existing employee
     */
    @Test
    public void updateEmployeeTest() {
        Long employeeId = 1L;
        EmployeeDto employeeDto = new EmployeeDto(null, "employee 1 updated", 2000, "Marketing");

        // mock
        when(employeeRepository.findById(eq(employeeId)))
                .thenReturn(Optional.of(
                        new Employee(employeeId, "employee 1", 1000, "Marketing")));

        when(employeeRepository.save(argThat(
                e -> e.getId() == employeeId && e.getName().equals(employeeDto.getName()))))
                .thenReturn(
                        new Employee(employeeId, employeeDto.getName(), 1000, "Marketing"));

        // test
        EmployeeDto out = employeeService.updateEmployee(employeeId, employeeDto);

        // assert
        assertEquals(employeeId, out.getId());
        assertEquals(employeeDto.getName(), out.getName());
    }

    /**
     * Test update existing employee with ResourceNotFoundException thrown
     */
    @Test
    public void updateEmployeeNotFoundTest() {
        Long employeeId = 1L;

        // mock
        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.empty());

        // setup expected exception
        exceptionRule.expect(ResourceNotFoundException.class);
        exceptionRule.expectMessage("Employee does not exist");

        // test
        employeeService.updateEmployee(employeeId,
                new EmployeeDto(null, "employee 1 updated", 2000, "Marketing"));
    }
}
