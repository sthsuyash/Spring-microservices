package com.suyash.employeeservice.service;

import com.suyash.employeeservice.dto.ApiResponse;
import com.suyash.employeeservice.dto.EmployeeRequestDTO;
import com.suyash.employeeservice.dto.EmployeeResponseDTO;
import com.suyash.employeeservice.model.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployeeService {
    ApiResponse<List<EmployeeResponseDTO>> findAllEmployees();

    ApiResponse<EmployeeResponseDTO> createEmployee(EmployeeRequestDTO employeeRequestDTO);

    ApiResponse<EmployeeResponseDTO> findEmployeeById(Long id);

    ApiResponse<EmployeeResponseDTO> updateEmployee(Long id, EmployeeRequestDTO employeeRequestDTO);

    ApiResponse<Void> deleteEmployee(Long id);
}
