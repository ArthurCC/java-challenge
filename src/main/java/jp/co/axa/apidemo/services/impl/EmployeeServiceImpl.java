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

/**
 * Service implementation class for employee operations business logic.
 * Perform DB operations and conversions from Entity to DTO
 * 
 * @author Arthur Campos Costa
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    /** employee repository */
    private final EmployeeRepository employeeRepository;

    /** employee mapper */
    private final EmployeeMapper employeeMapper;

    /**
     * page size for retrieving multiple employees
     * Picked up from application.properties
     */
    private final Integer pageSize;

    /**
     * Constructor
     * 
     * @param employeeRepository employee repository
     * @param employeeMapper     employee mapper
     * @param pageSize           page size, defaults to 5
     */
    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper,
            @Value("${app.employees.page-size:5}") Integer pageSize) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.pageSize = pageSize;
    }

    /**
     * {@inheritDoc}
     */
    // We cache the list with page as the key, if page is undefined then null is the
    // key for all employees
    // Since there is no control on page argument, we don't want to cache if page is
    // specified and result is empty because some malicious user could overload the
    // cache by sending multiple requests increasing the page every time
    @Cacheable(value = "employees", key = "#page", unless = "#page.isPresent() and #result.isEmpty()")
    public List<EmployeeDto> retrieveEmployees(Optional<Integer> page) {
        List<Employee> employees;
        if (page.isPresent()) {
            // first page is 0 so we substract 1
            Pageable pageable = PageRequest.of(page.get(), pageSize);
            employees = employeeRepository.findAll(pageable)
                    .getContent();
        } else {
            employees = employeeRepository.findAll();
        }

        // logging to verify cache working properly
        LOGGER.info("Retrieving employees from DB...");

        return employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ResourceNotFoundException if employee was not found
     */
    // Employee is cached/retrieved employee with the id as the key
    @Cacheable(value = "employee", key = "#employeeId")
    public EmployeeDto getEmployee(Long employeeId) {

        // logging to verify cache working properly
        LOGGER.info("Retrieving employee from DB...");

        return employeeRepository.findById(employeeId)
                .map(employeeMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist"));
    }

    /**
     * {@inheritDoc}
     */
    // We put created employee in cache with id as the key
    // We also remove lists of employees from the cache
    @CachePut(value = "employee", key = "#result.id")
    @CacheEvict(value = "employees", allEntries = true)
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeRepository.save(
                employeeMapper.populateEntity(new Employee(), employeeDto));

        return employeeMapper.toDto(employee);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ResourceNotFoundException if employee was not found
     */
    // We remove the deleted employee as well as the lists of employees from the
    // cache
    @Caching(evict = {
            @CacheEvict(value = "employee", key = "#employeeId"),
            @CacheEvict(value = "employees", allEntries = true)
    })
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist"));

        employeeRepository.delete(employee);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ResourceNotFoundException if employee was not found
     */
    // We override the updated employee in the cache with key as id
    // We also remove the lists of employees
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