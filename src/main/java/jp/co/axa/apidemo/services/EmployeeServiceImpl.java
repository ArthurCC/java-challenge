package jp.co.axa.apidemo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.mappers.EmployeeMapper;
import jp.co.axa.apidemo.model.EmployeeDto;
import jp.co.axa.apidemo.repositories.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    private final Integer pageSize;

    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper,
            @Value("${app.employees.page-size:5}") Integer pageSize) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.pageSize = pageSize;
    }

    public List<EmployeeDto> retrieveEmployees(Optional<Integer> page) {
        List<Employee> employees;
        if (page.isPresent()) {
            // first page is 0 so we substract 1
            Pageable pageable = PageRequest.of(page.get() - 1, pageSize);
            employees = employeeRepository.findAll(pageable)
                    .getContent();
        } else {
            employees = employeeRepository.findAll();
        }

        return employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    public EmployeeDto getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .map(employeeMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist"));
    }

    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeRepository.save(
                employeeMapper.populateEntity(new Employee(), employeeDto));

        return employeeMapper.toDto(employee);
    }

    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist"));

        employeeRepository.delete(employee);
    }

    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto) {
        Employee employeeDB = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist"));

        employeeDB = employeeRepository.save(
                employeeMapper.populateEntity(employeeDB, employeeDto));

        return employeeMapper.toDto(employeeDB);
    }
}