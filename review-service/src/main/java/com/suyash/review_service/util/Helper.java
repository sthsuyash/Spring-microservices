package com.suyash.review_service.util;

import com.suyash.review_service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Helper class that contains utility methods.
 */
@Component
public class Helper {
    /**
     * Validates the employee ID.
     *
     * @param employeeId The ID of the employee
     */
    public void validateEmployeeId(Long employeeId) {
        if (employeeId == null) {
            throw new ResourceNotFoundException("Employee ID cannot be null");
        }
    }
}
