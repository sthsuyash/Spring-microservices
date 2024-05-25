package com.suyash.review_service.client;

import com.suyash.review_service.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service")
public interface EmployeeClient {
    @GetMapping("/employees/exists/{employeeId}")
    ApiResponse<Boolean> existsById(@PathVariable Long employeeId);
}
