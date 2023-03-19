package jp.co.axa.apidemo.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.mappers.EmployeeMapper;
import jp.co.axa.apidemo.model.EmployeeDto;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.services.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

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

    // Since there is no control on page argument, we don't want to cache if page is
    // specified and result is empty because some malicious user could overload the
    // cache by sending multiple requests increasing the page every time
    @Cacheable(value = "employees", key = "#page", unless = "#page.isPresent() and #result.isEmpty()")
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

        LOGGER.info("Retrieving employees from DB...");

        return employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "employee", key = "#employeeId")
    public EmployeeDto getEmployee(Long employeeId) {

        LOGGER.info("Retrieving employee from DB...");

        return employeeRepository.findById(employeeId)
                .map(employeeMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist"));
    }

    @CachePut(value = "employee", key = "#result.id")
    @CacheEvict(value = "employees", allEntries = true)
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeRepository.save(
                employeeMapper.populateEntity(new Employee(), employeeDto));

        return employeeMapper.toDto(employee);
    }

    @Caching(evict = {
            @CacheEvict(value = "employee", key = "#employeeId"),
            @CacheEvict(value = "employees", allEntries = true)
    })
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist"));

        employeeRepository.delete(employee);
    }

    @CachePut(value = "employee", key = "#result.id")
    @CacheEvict(value = "employees", allEntries = true)
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto) {
        Employee employeeDB = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist"));

        employeeDB = employeeRepository.save(
                employeeMapper.populateEntity(employeeDB, employeeDto));

        return employeeMapper.toDto(employeeDB);
    }
}