package com.suyash.employeeservice.service;

import com.suyash.employeeservice.dto.ApiResponse;
import com.suyash.employeeservice.dto.EmployeeRequestDTO;
import com.suyash.employeeservice.dto.EmployeeResponseDTO;
import com.suyash.employeeservice.dto.ReviewMessageDTO;
import com.suyash.employeeservice.model.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmployeeService {
    ApiResponse<List<EmployeeResponseDTO>> findAllEmployees(Long departmentId);

    ApiResponse<EmployeeResponseDTO> createEmployee(EmployeeRequestDTO employeeRequestDTO);

    ApiResponse<EmployeeResponseDTO> findEmployeeById(Long id);

    ApiResponse<EmployeeResponseDTO> updateEmployee(Long id, EmployeeRequestDTO employeeRequestDTO);

    ApiResponse<Void> updateEmployeeRating(ReviewMessageDTO reviewMessageDTO);

    ApiResponse<Void> deleteEmployee(Long id);

    ApiResponse<Boolean> existsById(Long id);
}
