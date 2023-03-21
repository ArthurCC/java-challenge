package jp.co.axa.apidemo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Immutable Employee Data Transfer Object model.
 * Validated by Spring on POST and PUT methods
 */
@AllArgsConstructor
@Getter
public class EmployeeDto {

    /** id */
    private final Long id;

    /** name, not blank */
    @NotBlank
    private final String name;

    /** salary */
    @NotNull
    private final Integer salary;

    /** department */
    @NotBlank
    private final String department;
}
