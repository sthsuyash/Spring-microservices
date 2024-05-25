package com.suyash.departmentservice.mapper;

import com.suyash.departmentservice.dto.DepartmentResponseDTO;
import com.suyash.departmentservice.model.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
    /**
     * Maps a Department entity to a DepartmentResponseDTO object.
     *
     * @param department The Department entity to map
     * @return DepartmentResponseDTO object
     */
    public DepartmentResponseDTO mapToDepartmentResponseDTO(Department department) {
        DepartmentResponseDTO responseDTO = new DepartmentResponseDTO();
        responseDTO.setId(department.getId());
        responseDTO.setName(department.getName());
        return responseDTO;
    }
}
