package jp.co.axa.apidemo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Immutable DTO Employee class
 */
@AllArgsConstructor
@Getter
public class EmployeeDto {

    private final Long id;

    @NotBlank
    private final String name;

    @NotNull
    private final Integer salary;

    @NotBlank
    private final String department;
}
