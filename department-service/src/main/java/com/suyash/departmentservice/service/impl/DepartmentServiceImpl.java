package com.suyash.departmentservice.service.impl;

import com.suyash.departmentservice.client.EmployeeClient;
import com.suyash.departmentservice.dto.*;
import com.suyash.departmentservice.exception.*;
import com.suyash.departmentservice.mapper.DepartmentMapper;
import com.suyash.departmentservice.model.Department;
import com.suyash.departmentservice.repository.DepartmentRepository;
import com.suyash.departmentservice.service.DepartmentService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of the DepartmentService interface.
 */
@Component
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceImpl.class);
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final EmployeeClient employeeClient;

    /**
     * Constructor for DepartmentServiceImpl.
     *
     * @param departmentRepository The department repository
     * @param departmentMapper     The department mapper
     * @param employeeClient       The employee client
     */
    public DepartmentServiceImpl(
            DepartmentRepository departmentRepository,
            DepartmentMapper departmentMapper,
            EmployeeClient employeeClient
    ) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.employeeClient = employeeClient;
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
                .map(departmentMapper::mapToDepartmentResponseDTO)
                .collect(Collectors.toList());

        if (departments.isEmpty()) {
            LOGGER.error("No departments found");
            return new ApiResponse<>(false, "No departments found", null);
        }

        LOGGER.info("Retrieved Departments");
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
        Objects.requireNonNull(departmentRequestDTO, "DepartmentRequestDTO must not be null");

        if (departmentRepository.existsByName(departmentRequestDTO.getName())) {
            throw new DepartmentAlreadyExistsException("Department with name " + departmentRequestDTO.getName() + " already exists");
        }

        Department department = new Department(departmentRequestDTO.getName());
        Department savedDepartment = departmentRepository.save(department);
        LOGGER.info("Created Department: {}", savedDepartment);
        return new ApiResponse<>(true, "Department created successfully", departmentMapper.mapToDepartmentResponseDTO(savedDepartment));
    }

    /**
     * Retrieves a department by its ID.
     *
     * @param id The ID of the department to retrieve
     * @return ApiResponse containing the retrieved DepartmentResponseDTO object
     */
    @Override
    public ApiResponse<DepartmentResponseDTO> findDepartmentById(Long id) {
        Objects.requireNonNull(id, "Department ID must not be null");

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
        return new ApiResponse<>(true, "Department retrieved successfully", departmentMapper.mapToDepartmentResponseDTO(department));
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
        Objects.requireNonNull(id, "Department ID must not be null");

        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));

        existingDepartment.setName(departmentRequestDTO.getName());
        Department updatedDepartment = departmentRepository.save(existingDepartment);
        LOGGER.info("Updated Department: {}", updatedDepartment);
        return new ApiResponse<>(true, "Department updated successfully", departmentMapper.mapToDepartmentResponseDTO(updatedDepartment));
    }

    /**
     * Deletes a department by its ID.
     *
     * @param id The ID of the department to delete
     * @return ApiResponse indicating the success or failure of the deletion operation
     */
    @Override
    public ApiResponse<Void> deleteDepartment(Long id) {
        Objects.requireNonNull(id, "Department ID must not be null");

        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() ->  new DepartmentNotFoundException("Department not found with id: " + id));

        departmentRepository.delete(existingDepartment);
        LOGGER.info("Deleted Department: {}", existingDepartment);
        return new ApiResponse<>(true, "Department deleted successfully", null);
    }

    /**
     * Checks if a department exists by its ID.
     *
     * @param id The ID of the department to check
     * @return ApiResponse indicating the success or failure of the operation
     */
    @Override
    public ApiResponse<Boolean> departmentExists(Long id) {
        Objects.requireNonNull(id, "Department ID must not be null");

        boolean exists = departmentRepository.existsById(id);
        return new ApiResponse<>(true, "Department with ID " + id + " exists", exists);
    }

    /**
     * Retrieves all employees in a department by its name.
     *
     * @param name The name of the department
     * @return ApiResponse containing a list of EmployeeResponseDTO objects
     */
    @Override
    public ApiResponse<List<EmployeeResponseDTO>> findEmployeesByDepartmentName(String name) {
        Objects.requireNonNull(name, "Department name must not be null");

        Department department = departmentRepository.findByName(name)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with name: " + name));

        try {
            ApiResponse<List<EmployeeResponseDTO>> apiResponse = employeeClient.findEmployeesByDepartmentId(department.getId());

            if (!apiResponse.isSuccess() || apiResponse.getData() == null) {
                throw new ServiceUnavailableException("Error occurred while retrieving employees in department: " + name);
            }

            List<EmployeeResponseDTO> employeesList = apiResponse.getData();

            if (employeesList.isEmpty()) {
                return new ApiResponse<>(true, "No employees found in department: " + name, null);
            }

            return new ApiResponse<>(true, "Employees retrieved successfully", employeesList);
        } catch (FeignException.NotFound e) {
            throw new EmployeesNotFoundException("No employees found in department: " + name);
        }
    }
}
