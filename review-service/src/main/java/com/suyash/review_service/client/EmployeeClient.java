package com.suyash.review_service.client;

import com.suyash.review_service.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * EmployeeClient is a Feign client that communicates with the employee-service.
 */
@FeignClient(name = "employee-service")
public interface EmployeeClient {
    /**
     * This method is used to check if an employee exists by the given employeeId.
     * @param employeeId
     * @return ApiResponse<Boolean>
     */
    @GetMapping("/employees/exists/{employeeId}")
    ApiResponse<Boolean> existsById(@PathVariable Long employeeId);
}
