package com.suyash.employeeservice.client;

import com.suyash.employeeservice.dto.ApiResponse;
import com.suyash.employeeservice.dto.DepartmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "department-service")
public interface DepartmentClient {
    @GetMapping("/departments/{departmentId}")
    ApiResponse<DepartmentDTO> getDepartment(@PathVariable Long departmentId);

    @GetMapping("/departments/{departmentId}/exists")
    ApiResponse<Boolean> departmentExists(@PathVariable Long departmentId);
}
