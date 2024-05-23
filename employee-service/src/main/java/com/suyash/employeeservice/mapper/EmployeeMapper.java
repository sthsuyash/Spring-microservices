package com.suyash.employeeservice.mapper;

import com.suyash.employeeservice.client.DepartmentClient;
import com.suyash.employeeservice.client.ReviewClient;
import com.suyash.employeeservice.dto.ApiResponse;
import com.suyash.employeeservice.dto.DepartmentDTO;
import com.suyash.employeeservice.dto.ReviewDTO;
import com.suyash.employeeservice.dto.EmployeeResponseDTO;
import com.suyash.employeeservice.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Mapper class responsible for mapping Employee entities to EmployeeResponseDTOs.
 */
@Component
public class EmployeeMapper {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeMapper.class);
    private final DepartmentClient departmentClient;
    private final ReviewClient reviewClient;

    /**
     * Constructor for EmployeeMapper.
     *
     * @param departmentClient The DepartmentClient for fetching Department information
     * @param reviewClient     The ReviewClient for fetching Review information
     */
    public EmployeeMapper(DepartmentClient departmentClient, ReviewClient reviewClient) {
        this.departmentClient = departmentClient;
        this.reviewClient = reviewClient;
    }

    /**
     * Maps an Employee entity to an EmployeeResponseDTO.
     *
     * @param employee The Employee entity to map
     * @return The mapped EmployeeResponseDTO
     */
    public EmployeeResponseDTO mapToEmployeeResponseDTO(Employee employee) {
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO();
        responseDTO.setId(employee.getId());
        responseDTO.setFirstName(employee.getFirstName());
        responseDTO.setLastName(employee.getLastName());
        responseDTO.setEmail(employee.getEmail());

        // Request Department information from Department microservice
        DepartmentDTO departmentDTO = fetchDepartmentInfo(employee.getDepartmentId());
        responseDTO.setDepartment(departmentDTO);

        // Request Review information from Review microservice
        List<ReviewDTO> reviewDTOList = fetchReviewInfo(employee.getId());
        responseDTO.setReview(reviewDTOList);

        return responseDTO;
    }

    /**
     * Fetches Department information from Department microservice using OpenFeign.
     *
     * @param departmentId The ID of the Department to fetch
     * @return The DepartmentDTO containing the Department information
     */
    private DepartmentDTO fetchDepartmentInfo(Long departmentId) {
        ApiResponse<DepartmentDTO> apiResponse = departmentClient.getDepartment(departmentId);
        if (apiResponse != null && apiResponse.isSuccess()) {
            DepartmentDTO departmentDTO = apiResponse.getData();
            logger.info("Fetched Department information: {}", departmentDTO);
            return departmentDTO;
        } else {
            logger.error("Failed to fetch department information for departmentId {}", departmentId);
            return null;
        }
    }

    /**
     * Fetches Review information from Review microservice using OpenFeign.
     *
     * @param employeeId The ID of the Employee to fetch Review information for corresponding Employee
     * @return The list of ReviewDTO containing the Review information
     */
    private List<ReviewDTO> fetchReviewInfo(Long employeeId) {
        ApiResponse<List<ReviewDTO>> apiResponse = reviewClient.getReviewByEmployeeId(employeeId);
        if (apiResponse != null && apiResponse.isSuccess()) {
            List<ReviewDTO> reviewDTOList = apiResponse.getData();
            logger.info("Fetched Review information: {}", reviewDTOList);
            return reviewDTOList;
        } else {
            logger.error("Failed to fetch review information for employeeId {}", employeeId);
            return Collections.emptyList();
        }
    }
}
