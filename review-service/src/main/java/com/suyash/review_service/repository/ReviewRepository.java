package com.suyash.review_service.repository;

import com.suyash.review_service.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Review entities.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    /**
     * Finds all reviews by employee ID.
     *
     * @param employeeId The ID of the employee
     * @return A list of reviews by the employee
     */
    List<Review> findByEmployeeId(Long employeeId);
}
