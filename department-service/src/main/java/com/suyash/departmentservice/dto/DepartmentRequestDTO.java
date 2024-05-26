package com.suyash.departmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for request to department endpoint.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequestDTO {
    private String name;
}
