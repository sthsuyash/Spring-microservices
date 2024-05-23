package com.suyash.employeeservice.service.impl;

import com.suyash.employeeservice.client.ReviewClient;
import com.suyash.employeeservice.dto.ApiResponse;
import com.suyash.employeeservice.dto.EmployeeRequestDTO;
import com.suyash.employeeservice.dto.EmployeeResponseDTO;
import com.suyash.employeeservice.dto.ReviewMessageDTO;
import com.suyash.employeeservice.exception.ResourceNotFoundException;
import com.suyash.employeeservice.mapper.EmployeeMapper;
import com.suyash.employeeservice.model.Employee;
import com.suyash.employeeservice.repository.EmployeeRepository;
import com.suyash.employeeservice.service.EmployeeService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the EmployeeService interface.
 */
@Component
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private ReviewClient reviewClient;

    /**
     * Constructor for EmployeeServiceImpl.
     *
     * @param employeeRepository The employee repository
     * @param employeeMapper     The EmployeeMapper for mapping entities and DTOs
     * @param reviewClient       The ReviewClient for making requests to the review service
     */
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, ReviewClient reviewClient) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.reviewClient = reviewClient;
    }

    /**
     * Retrieves all employees.
     *
     * @return ApiResponse containing a list of EmployeeResponseDTO objects
     */
    @Override
    @RateLimiter(name = "default", fallbackMethod = "defaultFallback")
    public ApiResponse<List<EmployeeResponseDTO>> findAllEmployees() {
        List<EmployeeResponseDTO> employees = employeeRepository.findAll()
                .stream()
                .map(employeeMapper::mapToEmployeeResponseDTO)
                .collect(Collectors.toList());

        if (employees.isEmpty()) {
            logger.info("No employees found");
            return new ApiResponse<>(false, "No employees found", null);
        }

        logger.info("Retrieved Employees: {}", employees);
        return new ApiResponse<>(true, "Employees retrieved successfully", employees);
    }


    /**
     * Creates a new employee.
     *
     * @param employeeRequestDTO The employee details to be created
     * @return ApiResponse containing the created EmployeeResponseDTO object
     */
    @Override
    @RateLimiter(name = "default", fallbackMethod = "defaultFallback")
    public ApiResponse<EmployeeResponseDTO> createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        Employee employee = new Employee(
                employeeRequestDTO.getFirstName(),
                employeeRequestDTO.getLastName(),
                employeeRequestDTO.getEmail(),
                employeeRequestDTO.getDepartmentId()
        );
        Employee savedEmployee = employeeRepository.save(employee);
        logger.info("Created Employee: {}", savedEmployee);

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
    @RateLimiter(name = "default", fallbackMethod = "defaultFallback")
    public ApiResponse<EmployeeResponseDTO> findEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        logger.info("Retrieved Employee: {}", employee);
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
    @RateLimiter(name = "default", fallbackMethod = "defaultFallback")
    public ApiResponse<EmployeeResponseDTO> updateEmployee(Long id, EmployeeRequestDTO employeeRequestDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        existingEmployee.setFirstName(employeeRequestDTO.getFirstName());
        existingEmployee.setLastName(employeeRequestDTO.getLastName());
        existingEmployee.setEmail(employeeRequestDTO.getEmail());
        existingEmployee.setDepartmentId(employeeRequestDTO.getDepartmentId());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        logger.info("Updated Employee: {}", updatedEmployee);

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
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + reviewMessageDTO.getEmployeeId()));

        ApiResponse<Double> apiResponse = reviewClient.getAverageRating(reviewMessageDTO.getEmployeeId());
        if (!apiResponse.isSuccess()) {
            logger.error("Error occurred while fetching average rating: {}", apiResponse.getMessage());
            return new ApiResponse<>(false, "Error occurred while fetching average rating", null);
        }

        Double averageRating = apiResponse.getData();
        existingEmployee.setAverageRating(averageRating);
        employeeRepository.save(existingEmployee);
        logger.info("Updated Employee Rating: {}", existingEmployee);

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
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(existingEmployee);
        logger.info("Deleted Employee: {}", existingEmployee);
        return new ApiResponse<>(true, "Employee deleted successfully", null);
    }

    public ApiResponse<?> defaultFallback(Exception e) {
        logger.error("Error occurred while fetching employees: {}", e.getMessage());
        return new ApiResponse<>(false, "Error occurred while fetching employees", null);
    }
}