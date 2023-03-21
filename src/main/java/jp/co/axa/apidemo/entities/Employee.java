package jp.co.axa.apidemo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Employee JPA entity
 * 
 * @author Arthur Campos Costa
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EMPLOYEE")
public class Employee {

    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** name, not null constraint */
    @Column(name = "EMPLOYEE_NAME", nullable = false)
    private String name;

    /** salary, not null constraint */
    @Column(name = "EMPLOYEE_SALARY", nullable = false)
    private Integer salary;

    /** department, not null */
    @Column(name = "EMPLOYEE_DEPARTMENT", nullable = false)
    private String department;
}
