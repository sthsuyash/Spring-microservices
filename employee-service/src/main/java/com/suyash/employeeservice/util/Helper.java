package com.suyash.employeeservice.util;

import com.suyash.employeeservice.client.DepartmentClient;
import com.suyash.employeeservice.dto.ApiResponse;
import com.suyash.employeeservice.exception.DepartmentNotFoundException;
import com.suyash.employeeservice.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class Helper {
    private final DepartmentClient departmentClient;

    public Helper(DepartmentClient departmentClient) {
        this.departmentClient = departmentClient;
    }

    /**
     * Validates if a department exists by department ID.
     *
     * @param departmentId The ID of the department to check
     */
    public void validateDepartmentExists(Long departmentId) {
        ApiResponse<Boolean> departmentExistsResponse = departmentClient.departmentExists(departmentId);

        if (!departmentExistsResponse.isSuccess()) {
            throw new ResourceNotFoundException("Error occurred while checking if department exists");
        }

        if (!departmentExistsResponse.getData()) {
            throw new DepartmentNotFoundException("Department not found with id: " + departmentId);
        }
    }
}
