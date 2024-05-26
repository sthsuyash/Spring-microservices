package com.suyash.departmentservice.repository;

import com.suyash.departmentservice.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A repository for the Department entity.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>{
    /**
     * Checks if a department with the given name exists.
     *
     * @param name The name of the department
     * @return True if a department with the given name exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Finds a department by name.
     *
     * @param name The name of the department
     * @return An Optional object containing the department if found
     */
    Optional<Department> findByName(String name);
}
