package com.suyash.departmentservice.client;

import com.suyash.departmentservice.dto.ApiResponse;
import com.suyash.departmentservice.dto.EmployeeResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * This interface is used to call employee-service APIs.
 */
@FeignClient(name = "employee-service")
public interface EmployeeClient {
    /**
     * This method is used to call employee-service API to get all employees by department id.
     * @param id
     * @return List<EmployeeResponseDTO>
     */
    @GetMapping("/employees?departmentId={id}")
    ApiResponse<List<EmployeeResponseDTO>> findEmployeesByDepartmentId(@PathVariable Long id);
}
