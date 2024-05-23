package com.suyash.departmentservice.service;

import com.suyash.departmentservice.dto.ApiResponse;
import com.suyash.departmentservice.dto.DepartmentRequestDTO;
import com.suyash.departmentservice.dto.DepartmentResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepartmentService {
    ApiResponse<List<DepartmentResponseDTO>> findAllDepartments();

    ApiResponse<DepartmentResponseDTO> createDepartment(DepartmentRequestDTO employeeRequestDTO);

    ApiResponse<DepartmentResponseDTO> findDepartmentById(Long id);

    ApiResponse<DepartmentResponseDTO> updateDepartment(Long id, DepartmentRequestDTO employeeRequestDTO);

    ApiResponse<Void> deleteDepartment(Long id);
}
