package com.suyash.review_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representing a review message.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewMessageDTO {
    private Long id;
    private String title;
    private String description;
    private double rating;
    @JsonProperty("employee_id")
    private Long employeeId;
}
