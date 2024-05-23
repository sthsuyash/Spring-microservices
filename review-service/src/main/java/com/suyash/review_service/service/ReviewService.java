package com.suyash.review_service.service;

import com.suyash.review_service.dto.ApiResponse;
import com.suyash.review_service.dto.ReviewRequestDTO;
import com.suyash.review_service.dto.ReviewResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {
    ApiResponse<List<ReviewResponseDTO>> findReviewsByEmployeeId(Long employeeId);

    ApiResponse<ReviewResponseDTO> createReview(Long employeeId, ReviewRequestDTO employeeRequestDTO);

    ApiResponse<ReviewResponseDTO> findReviewById(Long id);

    ApiResponse<ReviewResponseDTO> updateReview(Long id, ReviewRequestDTO employeeRequestDTO);

    ApiResponse<Void> deleteReview(Long id);

    ApiResponse<Double> getAverageRating(Long employeeId);
}
