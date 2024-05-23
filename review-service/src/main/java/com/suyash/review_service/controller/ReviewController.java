package com.suyash.review_service.controller;

import com.suyash.review_service.dto.ApiResponse;
import com.suyash.review_service.dto.ReviewRequestDTO;
import com.suyash.review_service.dto.ReviewResponseDTO;
import com.suyash.review_service.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponseDTO>>> getReviewsByEmployeeId(@RequestParam Long employeeId) {
        return ResponseEntity.ok(reviewService.findReviewsByEmployeeId(employeeId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> createReview(@RequestParam Long employeeId, @RequestBody ReviewRequestDTO reviewRequestDTO) {
        return new ResponseEntity<>(reviewService.createReview(employeeId, reviewRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.findReviewById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> updateReview(@PathVariable Long id, @RequestBody ReviewRequestDTO reviewRequestDTO) {
        return ResponseEntity.ok(reviewService.updateReview(id, reviewRequestDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteReview(@PathVariable Long id) {
        return reviewService.deleteReview(id);
    }

    @GetMapping("/average-rating")
    public ResponseEntity<ApiResponse<Double>> getAverageRating(@RequestParam Long employeeId) {
        return ResponseEntity.ok(reviewService.getAverageRating(employeeId));
    }
}