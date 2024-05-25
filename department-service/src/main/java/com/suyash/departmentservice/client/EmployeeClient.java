package com.suyash.departmentservice.client;

import com.suyash.departmentservice.dto.ApiResponse;
import com.suyash.departmentservice.dto.EmployeeResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "employee-service")
public interface EmployeeClient {
    @GetMapping("/employees?departmentId={id}")
    ApiResponse<List<EmployeeResponseDTO>> findEmployeesByDepartmentId(@PathVariable Long id);
}
