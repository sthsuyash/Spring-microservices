package com.suyash.review_service.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representing a review request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    private double rating;
}
