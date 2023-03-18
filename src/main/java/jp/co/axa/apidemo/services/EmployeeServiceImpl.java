package jp.co.axa.apidemo.services;

import java.util.List;
import java.util.stream.Collectors;

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

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public List<EmployeeDto> retrieveEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    public EmployeeDto getEmployee(Long employeeId) throws ResourceNotFoundException {
        return employeeRepository.findById(employeeId)
                .map(employeeMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist"));
    }

    public void saveEmployee(EmployeeDto employeeDto) {
        employeeRepository.save(
                employeeMapper.populateEntity(new Employee(), employeeDto));
    }

    public void deleteEmployee(Long employeeId) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist"));

        employeeRepository.delete(employee);
    }

    public void updateEmployee(Long employeeId, EmployeeDto employeeDto) throws ResourceNotFoundException {
        Employee employeeDB = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist"));

        employeeRepository.save(
                employeeMapper.populateEntity(employeeDB, employeeDto));
    }
}