package com.suyash.review_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Review entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    private double rating;
    private Long employeeId; //    This is to establish relationship between Review and Employee microservices

    public Review(String title, String description, double rating) {
        this.title = title;
        this.description = description;
        this.rating = rating;
    }

    public Review(String title, String description, double rating, Long employeeId) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.employeeId = employeeId;
    }

}
