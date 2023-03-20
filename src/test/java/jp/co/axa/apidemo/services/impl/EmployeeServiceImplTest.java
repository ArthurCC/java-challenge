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
import jp.co.axa.apidemo.model.EmployeeDto;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.services.EmployeeService;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Captor
    public ArgumentCaptor<Employee> employeeCaptor;

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    @Before
    public void init() {
        employeeService = new EmployeeServiceImpl(employeeRepository,
                new EmployeeMapper(), 2);
    }

    @Test
    public void testRetrieveEmployeesAll() {
        when(employeeRepository.findAll())
            .thenReturn(Lists.newArrayList(
                new Employee(1L, "employee 1", 1000, "Marketing"),
                new Employee(2L, "employee 2", 2000, "Accounting"),
                new Employee(3L, "employee 3", 3000, "Development")
            ));

        List<EmployeeDto> out = employeeService.retrieveEmployees(Optional.empty());

        assertEquals(3, out.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testRetrieveEmployeesPage() {

        when(employeeRepository.findAll(argThat(
                (Pageable p) -> p.getPageSize() == 2 && p.getPageNumber() == 0)))
                .thenReturn(new PageImpl<>(
                        Lists.newArrayList(
                                new Employee(1L, "employee 1", 1000, "Marketing"),
                                new Employee(2L, "employee 2", 2000, "Accounting"))));

        List<EmployeeDto> out = employeeService.retrieveEmployees(Optional.of(1));

        assertEquals(2, out.size());
        verify(employeeRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testGetEmployee() {
        Long employeeId = 1L;
        when(employeeRepository.findById(eq(employeeId)))
                .thenReturn(Optional.of(
                        new Employee(1L, "employee 1", 1000, "Marketing")));

        EmployeeDto out = employeeService.getEmployee(employeeId);

        assertEquals(employeeId, out.getId());
        verify(employeeRepository, times(1)).findById(eq(employeeId));
    }

    @Test
    public void testGetEmployeeNotFound() {
        Long employeeId = 1L;
        when(employeeRepository.findById(eq(employeeId)))
                .thenReturn(Optional.empty());

        exceptionRule.expect(ResourceNotFoundException.class);
        exceptionRule.expectMessage("Employee does not exist");

        employeeService.getEmployee(employeeId);
    }

    @Test
    public void saveEmployeeTest() {
        EmployeeDto employeeDto = new EmployeeDto(null, "employee 1", 1000, "Marketing");
        when(employeeRepository.save(argThat(
                e -> e.getName().equals(employeeDto.getName())))).thenReturn(
                        new Employee(1L, "employee 1", 1000, "Marketing"));

        EmployeeDto out = employeeService.saveEmployee(employeeDto);

        assertEquals(1L, out.getId().longValue());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void deleteEmployeeTest() {
        Long employeeId = 1L;
        when(employeeRepository.findById(eq(employeeId)))
                .thenReturn(Optional.of(
                        new Employee(1L, "employee 1", 1000, "Marketing")));

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository, times(1))
                .delete(employeeCaptor.capture());

        assertEquals(employeeId, employeeCaptor.getValue().getId());
    }

    @Test
    public void deleteEmployeeNotFoundTest() {
        Long employeeId = 1L;
        when(employeeRepository.findById(eq(employeeId)))
                .thenReturn(Optional.empty());

        exceptionRule.expect(ResourceNotFoundException.class);
        exceptionRule.expectMessage("Employee does not exist");

        employeeService.deleteEmployee(employeeId);
    }

    @Test
    public void updateEmployeeTest() {
        Long employeeId = 1L;
        EmployeeDto employeeDto = new EmployeeDto(null, "employee 1 updated", 2000, "Marketing");
        when(employeeRepository.findById(eq(employeeId)))
                .thenReturn(Optional.of(
                        new Employee(employeeId, "employee 1", 1000, "Marketing")));

        when(employeeRepository.save(argThat(
                e -> e.getId() == employeeId && e.getName().equals(employeeDto.getName()))))
                .thenReturn(
                        new Employee(employeeId, employeeDto.getName(), 1000, "Marketing"));

        EmployeeDto out = employeeService.updateEmployee(employeeId, employeeDto);
        assertEquals(employeeId, out.getId());
        assertEquals(employeeDto.getName(), out.getName());
    }

    @Test
    public void updateEmployeeNotFoundTest() {
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.empty());

        exceptionRule.expect(ResourceNotFoundException.class);
        exceptionRule.expectMessage("Employee does not exist");

        employeeService.updateEmployee(employeeId,
                new EmployeeDto(null, "employee 1 updated", 2000, "Marketing"));
    }
}
