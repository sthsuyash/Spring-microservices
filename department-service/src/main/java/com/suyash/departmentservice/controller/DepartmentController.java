package com.suyash.departmentservice.controller;

import com.suyash.departmentservice.dto.ApiResponse;
import com.suyash.departmentservice.dto.DepartmentRequestDTO;
import com.suyash.departmentservice.dto.DepartmentResponseDTO;
import com.suyash.departmentservice.dto.EmployeeResponseDTO;
import com.suyash.departmentservice.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.findAllDepartments());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> createDepartment(@RequestBody DepartmentRequestDTO departmentRequestDTO) {
        return new ResponseEntity<>(departmentService.createDepartment(departmentRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.findDepartmentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> updateDepartment(@PathVariable Long id, @RequestBody DepartmentRequestDTO departmentRequestDTO) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.deleteDepartment(id));
    }

    @GetMapping("/employees/{name}")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDTO>>> getEmployeesByDepartmentName(@PathVariable String name) {
        return ResponseEntity.ok(departmentService.findEmployeesByDepartmentName(name));
    }

    @GetMapping("/{id}/exists")
    public ApiResponse<Boolean> departmentExists(@PathVariable Long id) {
        return departmentService.departmentExists(id);
    }
}
