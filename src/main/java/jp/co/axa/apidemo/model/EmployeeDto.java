package jp.co.axa.apidemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Immutable DTO Employee class
 */
@AllArgsConstructor
@Getter
public class EmployeeDto {

    private final Long id;
    private final String name;
    private final Integer salary;
    private final String department;
}
