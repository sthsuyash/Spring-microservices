package com.suyash.departmentservice.service.impl;

import com.suyash.departmentservice.dto.ApiResponse;
import com.suyash.departmentservice.dto.DepartmentRequestDTO;
import com.suyash.departmentservice.dto.DepartmentResponseDTO;
import com.suyash.departmentservice.exception.ResourceNotFoundException;
import com.suyash.departmentservice.model.Department;
import com.suyash.departmentservice.repository.DepartmentRepository;
import com.suyash.departmentservice.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the DepartmentService interface.
 */
@Component
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceImpl.class);
    private final DepartmentRepository departmentRepository;

    /**
     * Constructor for DepartmentServiceImpl.
     *
     * @param departmentRepository The department repository
     */
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * Retrieves all departments.
     *
     * @return ApiResponse containing a list of DepartmentResponseDTO objects
     */
    @Override
    public ApiResponse<List<DepartmentResponseDTO>> findAllDepartments() {
        List<DepartmentResponseDTO> departments = departmentRepository.findAll()
                .stream()
                .map(this::mapToDepartmentResponseDTO)
                .collect(Collectors.toList());

        if (departments.isEmpty()) {
            return new ApiResponse<>(false, "No departments found", null);
        }

        return new ApiResponse<>(true, "Departments retrieved successfully", departments);
    }

    /**
     * Creates a new department.
     *
     * @param departmentRequestDTO The department details to be created
     * @return ApiResponse containing the created DepartmentResponseDTO object
     */
    @Override
    public ApiResponse<DepartmentResponseDTO> createDepartment(DepartmentRequestDTO departmentRequestDTO) {
        Department department = new Department(departmentRequestDTO.getName());
        Department savedDepartment = departmentRepository.save(department);
        LOGGER.info("Created Department: {}", savedDepartment);
        return new ApiResponse<>(true, "Department created successfully", mapToDepartmentResponseDTO(savedDepartment));
    }

    /**
     * Retrieves a department by its ID.
     *
     * @param id The ID of the department to retrieve
     * @return ApiResponse containing the retrieved DepartmentResponseDTO object
     */
    @Override
    public ApiResponse<DepartmentResponseDTO> findDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return new ApiResponse<>(true, "Department retrieved successfully", mapToDepartmentResponseDTO(department));
    }

    /**
     * Updates an existing department.
     *
     * @param id                   The ID of the department to update
     * @param departmentRequestDTO The updated department details
     * @return ApiResponse containing the updated DepartmentResponseDTO object
     */
    @Override
    public ApiResponse<DepartmentResponseDTO> updateDepartment(Long id, DepartmentRequestDTO departmentRequestDTO) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        existingDepartment.setName(departmentRequestDTO.getName());
        Department updatedDepartment = departmentRepository.save(existingDepartment);
        LOGGER.info("Updated Department: {}", updatedDepartment);
        return new ApiResponse<>(true, "Department updated successfully", mapToDepartmentResponseDTO(updatedDepartment));
    }

    /**
     * Deletes a department by its ID.
     *
     * @param id The ID of the department to delete
     * @return ApiResponse indicating the success or failure of the deletion operation
     */
    @Override
    public ApiResponse<Void> deleteDepartment(Long id) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        departmentRepository.delete(existingDepartment);
        LOGGER.info("Deleted Department: {}", existingDepartment);
        return new ApiResponse<>(true, "Department deleted successfully", null);
    }

    /**
     * Maps a Department entity to a DepartmentResponseDTO object.
     *
     * @param department The Department entity to map
     * @return DepartmentResponseDTO object
     */
    private DepartmentResponseDTO mapToDepartmentResponseDTO(Department department) {
        DepartmentResponseDTO responseDTO = new DepartmentResponseDTO();
        responseDTO.setId(department.getId());
        responseDTO.setName(department.getName());
        return responseDTO;
    }
}