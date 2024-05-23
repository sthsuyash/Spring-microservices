package com.suyash.review_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewMessageDTO {
    private Long id;
    private String title;
    private String description;
    private double rating;
    private Long employeeId;
}
