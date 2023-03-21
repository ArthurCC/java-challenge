package jp.co.axa.apidemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.axa.apidemo.entities.Employee;

/**
 * Employee repository interface
 * 
 * @author Arthur Campos Costa
 */
// This annotation is not necessary as long as we extend Spring repositories
// interfaces
// @Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
