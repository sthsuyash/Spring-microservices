package com.suyash.employeeservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO class that represents an employee request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDTO {
    @JsonProperty("first_name")
    @Column(nullable = false)
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @JsonProperty("department_id")
    @Column(nullable = false)
    private Long departmentId;
}
