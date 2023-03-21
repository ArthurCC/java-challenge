package jp.co.axa.apidemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.model.EmployeeDto;
import jp.co.axa.apidemo.model.Response;
import jp.co.axa.apidemo.repositories.EmployeeRepository;

/**
 * End to end integration tests for Employee API
 */
@RunWith(SpringRunner.class)
// Disable security due to lack of time to implement
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "app.security.enabled=false")
public class EmployeeIntegrationTest {

    /** employee repository */
    @Autowired
    private EmployeeRepository employeeRepository;

    /** test rest template */
    @Autowired
    private TestRestTemplate testRestTemplate;

    /**
     * Init database before each test with 3 employees
     */
    @Before
    public void initDatabase() {
        employeeRepository.saveAll(
                Lists.newArrayList(
                        new Employee(null, "employee 1", 1000, "Marketing"),
                        new Employee(null, "employee 2", 2000, "Accounting"),
                        new Employee(null, "employee 3", 3000, "Marketing")));
    }

    /**
     * Clear database after each test
     */
    @After
    public void clearDatabase() {
        employeeRepository.deleteAll();
    }

    /**
     * Test retrieve all employees GET
     * 
     * @throws URISyntaxException if error when creating URI
     */
    @Test
    public void retrieveEmployeesTest() throws URISyntaxException {

        RequestEntity<Void> requestEntity = RequestEntity.get(new URI("/api/v1/employees"))
                .build();

        // test
        ResponseEntity<Response<List<EmployeeDto>>> responseEntity = testRestTemplate.exchange(
                requestEntity,
                new ParameterizedTypeReference<Response<List<EmployeeDto>>>() {
                });

        Response<List<EmployeeDto>> response = responseEntity.getBody();
        List<EmployeeDto> employees = response.getData().get("employees");

        // assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(HttpStatus.OK.getReasonPhrase(), response.getStatusMessage());
        assertNotNull(response.getTimestamp());
        assertNull(response.getErrorMessage());
        assertEquals(3, employees.size());
        assertEquals("employee 1", employees.get(0).getName());
        assertEquals("employee 2", employees.get(1).getName());
        assertEquals("employee 3", employees.get(2).getName());
    }

    /**
     * Test retrieve a single employee
     * 
     * @throws URISyntaxException if error when creating URI
     */
    @Test
    public void getEmployeeTest() throws URISyntaxException {

        RequestEntity<Void> requestEntity = RequestEntity.get(new URI("/api/v1/employees/1"))
                .build();

        // test
        ResponseEntity<Response<EmployeeDto>> responseEntity = testRestTemplate.exchange(
                requestEntity,
                new ParameterizedTypeReference<Response<EmployeeDto>>() {
                });

        Response<EmployeeDto> response = responseEntity.getBody();
        EmployeeDto employee = response.getData().get("employee");

        // assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(HttpStatus.OK.getReasonPhrase(), response.getStatusMessage());
        assertNotNull(response.getTimestamp());
        assertNull(response.getErrorMessage());
        assertEquals("employee 1", employee.getName());
        assertEquals(1000, employee.getSalary().intValue());
        assertEquals("Marketing", employee.getDepartment());
    }

    /**
     * Test save a new Employee
     * 
     * @throws URISyntaxException if error when creating URI
     */
    @Test
    public void saveEmployeeTest() throws URISyntaxException {

        RequestEntity<EmployeeDto> requestEntity = RequestEntity.post(new URI("/api/v1/employees"))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new EmployeeDto(null, "employee 4", 4000, "Finance"));

        // test
        ResponseEntity<Response<EmployeeDto>> responseEntity = testRestTemplate.exchange(
                requestEntity,
                new ParameterizedTypeReference<Response<EmployeeDto>>() {
                });

        Response<EmployeeDto> response = responseEntity.getBody();
        EmployeeDto employee = response.getData().get("employee");

        // assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(HttpStatus.CREATED.getReasonPhrase(), response.getStatusMessage());
        assertNotNull(response.getTimestamp());
        assertNull(response.getErrorMessage());
        assertEquals("employee 4", employee.getName());
        assertEquals(4000, employee.getSalary().intValue());
        assertEquals("Finance", employee.getDepartment());
        // verify that employee was inserted in db
        assertTrue(employeeRepository.findById(employee.getId())
                .isPresent());
    }

    /**
     * Test delete an existing employee
     * 
     * @throws URISyntaxException if error when creating URI
     */
    @Test
    public void deleteEmployeeTest() throws URISyntaxException {

        // Since we use identity to populate employee ids before each test we cannot
        // know what are the current ids in the database because it depends on test
        // order. To mitigate that we fetch all employees and take the first one
        Long employeeId = employeeRepository.findAll()
                .get(0)
                .getId();

        RequestEntity<Void> requestEntity = RequestEntity.delete(new URI("/api/v1/employees/" + employeeId))
                .build();

        // test
        ResponseEntity<Response<Void>> responseEntity = testRestTemplate.exchange(
                requestEntity,
                new ParameterizedTypeReference<Response<Void>>() {
                });

        Response<Void> response = responseEntity.getBody();

        // assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(HttpStatus.OK.getReasonPhrase(), response.getStatusMessage());
        assertNotNull(response.getTimestamp());
        assertNull(response.getErrorMessage());
        assertNull(response.getData());
        // Verify that employee was deleted in db
        assertFalse(employeeRepository.findById(employeeId)
                .isPresent());
    }

    /**
     * Test update an existing employee
     * 
     * @throws URISyntaxException if error when creating URI
     */
    @Test
    public void updateEmployeeTest() throws URISyntaxException {

        // Since we use identity to populate employee ids before each test we cannot
        // know what are the current ids in the database because it depends on test
        // order. To mitigate that we fetch all employees and take the first one
        Long employeeId = employeeRepository.findAll()
                .get(0)
                .getId();

        RequestEntity<EmployeeDto> requestEntity = RequestEntity.put(new URI("/api/v1/employees/" + employeeId))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new EmployeeDto(null, "employee updated", 5000, "HR"));

        // test
        ResponseEntity<Response<EmployeeDto>> responseEntity = testRestTemplate.exchange(
                requestEntity,
                new ParameterizedTypeReference<Response<EmployeeDto>>() {
                });

        Response<EmployeeDto> response = responseEntity.getBody();
        EmployeeDto employee = response.getData().get("employee");

        // assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(HttpStatus.OK.getReasonPhrase(), response.getStatusMessage());
        assertNotNull(response.getTimestamp());
        assertNull(response.getErrorMessage());
        assertEquals("employee updated", employee.getName());
        assertEquals(5000, employee.getSalary().intValue());
        assertEquals("HR", employee.getDepartment());
        // verify that employee was updated in db
        assertEquals(
                "employee updated",
                employeeRepository.findById(employee.getId())
                        .get()
                        .getName());
    }
}
