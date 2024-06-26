package com.suyash.employeeservice.service.impl;

import com.suyash.employeeservice.client.DepartmentClient;
import com.suyash.employeeservice.client.ReviewClient;
import com.suyash.employeeservice.dto.ApiResponse;
import com.suyash.employeeservice.dto.EmployeeRequestDTO;
import com.suyash.employeeservice.dto.EmployeeResponseDTO;
import com.suyash.employeeservice.dto.ReviewMessageDTO;
import com.suyash.employeeservice.exception.EmailAlreadyExistsException;
import com.suyash.employeeservice.exception.EmployeeNotFoundException;
import com.suyash.employeeservice.exception.ResourceNotFoundException;
import com.suyash.employeeservice.mapper.EmployeeMapper;
import com.suyash.employeeservice.model.Employee;
import com.suyash.employeeservice.repository.EmployeeRepository;
import com.suyash.employeeservice.service.EmployeeService;
import com.suyash.employeeservice.exception.DepartmentNotFoundException;
import com.suyash.employeeservice.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the EmployeeService interface.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentClient departmentClient;
    private final ReviewClient reviewClient;
    private final Helper helper;

    /**
     * Constructor for EmployeeServiceImpl.
     *
     * @param employeeRepository The employee repository
     * @param employeeMapper     The EmployeeMapper for mapping entities and DTOs
     * @param departmentClient   The DepartmentClient for making requests to the department service
     * @param reviewClient       The ReviewClient for making requests to the review service
     * @param helper             The Helper class for utility methods
     */
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               EmployeeMapper employeeMapper,
                               DepartmentClient departmentClient,
                               ReviewClient reviewClient,
                               Helper helper
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.departmentClient = departmentClient;
        this.reviewClient = reviewClient;
        this.helper = helper;
    }

    /**
     * Retrieves all employees.
     *
     * @return ApiResponse containing a list of EmployeeResponseDTO objects
     */
    @Override
    public ApiResponse<List<EmployeeResponseDTO>> findAllEmployees(Long departmentId) {
        List<EmployeeResponseDTO> employees;

        if (departmentId != null) {
            employees = employeeRepository.findByDepartmentId(departmentId)
                    .stream()
                    .map(employeeMapper::mapToEmployeeResponseDTO)
                    .collect(Collectors.toList());

            if (employees.isEmpty()) {
                LOGGER.info("No employees found for department with id: {}", departmentId);
                return new ApiResponse<>(true, "No employees found", null);
            }
        } else {
            employees = employeeRepository.findAll()
                    .stream()
                    .map(employeeMapper::mapToEmployeeResponseDTO)
                    .collect(Collectors.toList());

            if (employees.isEmpty()) {
                LOGGER.info("No employees found");
                return new ApiResponse<>(true, "No employees found", null);
            }
        }

        LOGGER.info("Retrieved Employees: {}", employees);
        return new ApiResponse<>(true, "Employees retrieved successfully", employees);
    }

    /**
     * Creates a new employee.
     *
     * @param employeeRequestDTO The employee details to be created
     * @return ApiResponse containing the created EmployeeResponseDTO object
     */
    @Override
    public ApiResponse<EmployeeResponseDTO> createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        if (employeeRepository.existsByEmail(employeeRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Employee already exists with email: " + employeeRequestDTO.getEmail());
        }

        helper.validateDepartmentExists(employeeRequestDTO.getDepartmentId());

        Employee employee = new Employee(
                employeeRequestDTO.getFirstName(),
                employeeRequestDTO.getLastName(),
                employeeRequestDTO.getEmail(),
                employeeRequestDTO.getDepartmentId()
        );
        Employee savedEmployee = employeeRepository.save(employee);
        LOGGER.info("Created Employee: {}", savedEmployee);

        EmployeeResponseDTO responseDTO = employeeMapper.mapToEmployeeResponseDTO(savedEmployee);

        return new ApiResponse<>(true, "Employee created successfully", responseDTO);
    }

    /**
     * Retrieves an employee by their ID.
     *
     * @param id The ID of the employee to retrieve
     * @return ApiResponse containing the retrieved EmployeeResponseDTO object
     */
    @Override
    public ApiResponse<EmployeeResponseDTO> findEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        LOGGER.info("Retrieved Employee: {}", employee);
        EmployeeResponseDTO responseDTO = employeeMapper.mapToEmployeeResponseDTO(employee);
        return new ApiResponse<>(true, "Employee retrieved successfully", responseDTO);
    }

    /**
     * Updates an existing employee.
     *
     * @param id                 The ID of the employee to update
     * @param employeeRequestDTO The updated employee details
     * @return ApiResponse containing the updated EmployeeResponseDTO object
     */
    @Override
    public ApiResponse<EmployeeResponseDTO> updateEmployee(Long id, EmployeeRequestDTO employeeRequestDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        helper.validateDepartmentExists(employeeRequestDTO.getDepartmentId());

        existingEmployee.setFirstName(employeeRequestDTO.getFirstName());
        existingEmployee.setLastName(employeeRequestDTO.getLastName());
        existingEmployee.setEmail(employeeRequestDTO.getEmail());
        existingEmployee.setDepartmentId(employeeRequestDTO.getDepartmentId());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        LOGGER.info("Updated Employee: {}", updatedEmployee);

        EmployeeResponseDTO responseDTO = employeeMapper.mapToEmployeeResponseDTO(updatedEmployee);
        return new ApiResponse<>(true, "Employee updated successfully", responseDTO);
    }

    /**
     * Updates the rating of an employee.
     *
     * @param reviewMessageDTO The review message containing the employee ID and rating
     * @return ApiResponse indicating the success or failure of the update operation
     */
    @Override
    public ApiResponse<Void> updateEmployeeRating(ReviewMessageDTO reviewMessageDTO) {
        Employee existingEmployee = employeeRepository.findById(reviewMessageDTO.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + reviewMessageDTO.getEmployeeId()));

        ApiResponse<Double> apiResponse = reviewClient.getAverageRating(reviewMessageDTO.getEmployeeId());
        if (!apiResponse.isSuccess()) {
            LOGGER.error("Error occurred while fetching average rating: {}", apiResponse.getMessage());
            return new ApiResponse<>(false, "Error occurred while fetching average rating", null);
        }

        Double averageRating = apiResponse.getData();
        existingEmployee.setAverageRating(averageRating);
        employeeRepository.save(existingEmployee);
        LOGGER.info("Updated Employee Rating: {}", existingEmployee);

        return new ApiResponse<>(true, "Employee rating updated successfully", null);
    }

    /**
     * Deletes an employee by their ID.
     *
     * @param id The ID of the employee to delete
     * @return ApiResponse indicating the success or failure of the deletion operation
     */
    @Override
    public ApiResponse<Void> deleteEmployee(Long id) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        employeeRepository.delete(existingEmployee);
        LOGGER.info("Deleted Employee: {}", existingEmployee);
        return new ApiResponse<>(true, "Employee deleted successfully", null);
    }

    /**
     * Checks if an employee exists by their ID.
     *
     * @param id The ID of the employee to check
     * @return ApiResponse indicating the success or failure of the operation
     */
    @Override
    public ApiResponse<Boolean> existsById(Long id) {
        boolean exists = employeeRepository.existsById(id);
        if (!exists) {
            LOGGER.info("Employee not found with id: {}", id);
            return new ApiResponse<>(true, "Employee not found", null);
        }
        LOGGER.info("Employee exists with id: {}", id);
        return new ApiResponse<>(true, "Employee exists", null);
    }
}
