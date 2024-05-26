package com.suyash.employeeservice.repository;

import com.suyash.employeeservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

/**
 * Repository interface for Employee entities.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    /**
     * Finds an employee by email.
     *
     * @param email The email of the employee
     * @return The employee with the given email
     */
    boolean existsByEmail(String email);

    /**
     * Finds all employees by department ID.
     *
     * @param departmentId The ID of the department
     * @return A list of employees by the department
     */
    List<Employee> findByDepartmentId(Long departmentId);
}
