package com.suyash.review_service.service.impl;

import com.suyash.review_service.client.EmployeeClient;
import com.suyash.review_service.dto.ApiResponse;
import com.suyash.review_service.dto.ReviewRequestDTO;
import com.suyash.review_service.dto.ReviewResponseDTO;
import com.suyash.review_service.exception.EmployeeNotFoundException;
import com.suyash.review_service.exception.ResourceNotFoundException;
import com.suyash.review_service.exception.ReviewNotFoundException;
import com.suyash.review_service.mapper.ReviewMapper;
import com.suyash.review_service.message.ReviewMessageProducer;
import com.suyash.review_service.model.Review;
import com.suyash.review_service.repository.ReviewRepository;
import com.suyash.review_service.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ReviewService interface.
 */
@Component
public class ReviewServiceImpl implements ReviewService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ReviewMessageProducer reviewMessageProducer;
    private final EmployeeClient employeeClient;

    /**
     * Constructor for ReviewServiceImpl.
     *
     * @param reviewRepository      The review repository
     * @param reviewMapper          The ReviewMapper for mapping entities and DTOs
     * @param reviewMessageProducer The ReviewMessageProducer for sending messages to the RabbitMQ queue
     */
    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            ReviewMapper reviewMapper,
            ReviewMessageProducer reviewMessageProducer,
            EmployeeClient employeeClient
    ) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.reviewMessageProducer = reviewMessageProducer;
        this.employeeClient = employeeClient;
    }

    /**
     * Retrieves all reviews by employee ID.
     *
     * @param employeeId The ID of the employee
     * @return ApiResponse containing a list of ReviewResponseDTO objects
     */
    @Override
    public ApiResponse<List<ReviewResponseDTO>> findReviewsByEmployeeId(Long employeeId) {
        // check if the employeeId is not null
        if (employeeId == null) {
            LOGGER.error("Employee ID cannot be null");
            return new ApiResponse<>(false, "Employee ID cannot be null", null);
        }

        List<ReviewResponseDTO> reviews = reviewRepository.findByEmployeeId(employeeId)
                .stream()
                .map(reviewMapper::mapToReviewResponseDTO)
                .collect(Collectors.toList());

        if (reviews.isEmpty()) {
            LOGGER.info("No reviews found for employee with ID: {}", employeeId);
            return new ApiResponse<>(true, "No reviews found for employee with ID: " + employeeId, null);
        }

        LOGGER.info("Retrieved Reviews");
        return new ApiResponse<>(true, "Reviews retrieved successfully", reviews);
    }

    /**
     * Creates a new review.
     *
     * @param reviewRequestDTO The review details to be created
     * @return ApiResponse containing the created ReviewResponseDTO object
     */
    @Override
    public ApiResponse<ReviewResponseDTO> createReview(Long employeeId, ReviewRequestDTO reviewRequestDTO) {
        // check if the employeeId is not null
        if (employeeId == null) {
            LOGGER.error("Employee ID cannot be null");
            return new ApiResponse<>(false, "Employee ID cannot be null", null);
        }

        // check if the employee exists
        ApiResponse<Boolean> apiResponse = employeeClient.existsById(employeeId);
        if (!apiResponse.isSuccess()) {
            LOGGER.error("Failed to check if employee with ID: {} exists", employeeId);
            return new ApiResponse<>(false, "Failed to check if employee exists", null);
        }

        if (!apiResponse.getData()) {
            LOGGER.error("Employee with ID: {} does not exist", employeeId);
            throw new EmployeeNotFoundException("Employee not found with ID: " + employeeId);
        }

        Review review = new Review(
                reviewRequestDTO.getTitle(),
                reviewRequestDTO.getDescription(),
                reviewRequestDTO.getRating()
        );
        review.setEmployeeId(employeeId);
        Review savedReview = reviewRepository.save(review);
        LOGGER.info("Created Review: {}", savedReview);

        ReviewResponseDTO responseDTO = reviewMapper.mapToReviewResponseDTO(savedReview);

        reviewMessageProducer.sendMessage(responseDTO);

        return new ApiResponse<>(true, "Review created successfully", responseDTO);
    }

    /**
     * Retrieves a review by their ID.
     *
     * @param id The ID of the review to retrieve
     * @return ApiResponse containing the retrieved ReviewResponseDTO object
     */
    @Override
    public ApiResponse<ReviewResponseDTO> findReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Review not found with id: {}", id);
                    return new ReviewNotFoundException("Review not found with id: " + id);
                });

        LOGGER.info("Retrieved Review: {}", review);
        ReviewResponseDTO responseDTO = reviewMapper.mapToReviewResponseDTO(review);
        return new ApiResponse<>(true, "Review retrieved successfully", responseDTO);
    }

    /**
     * Updates an existing review.
     *
     * @param id               The ID of the review to update
     * @param reviewRequestDTO The updated review details
     * @return ApiResponse containing the updated ReviewResponseDTO object
     */
    @Override
    public ApiResponse<ReviewResponseDTO> updateReview(Long id, ReviewRequestDTO reviewRequestDTO) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Review not found with id: {}", id);
                    return new ReviewNotFoundException("Review not found with id: " + id);
                });

        existingReview.setTitle(reviewRequestDTO.getTitle());
        existingReview.setDescription(reviewRequestDTO.getDescription());
        existingReview.setRating(reviewRequestDTO.getRating());
        existingReview.setEmployeeId(existingReview.getEmployeeId());

        Review updatedReview = reviewRepository.save(existingReview);
        LOGGER.info("Updated Review: {}", updatedReview);

        ReviewResponseDTO responseDTO = reviewMapper.mapToReviewResponseDTO(updatedReview);
        return new ApiResponse<>(true, "Review updated successfully", responseDTO);
    }

    /**
     * Deletes a review by their ID.
     *
     * @param id The ID of the review to delete
     * @return ApiResponse indicating the success or failure of the deletion operation
     */
    @Override
    public ApiResponse<Void> deleteReview(Long id) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Review not found with id: {}", id);
                    return new ReviewNotFoundException("Review not found with id: " + id);
                });
        reviewRepository.delete(existingReview);
        LOGGER.info("Deleted Review: {}", existingReview);
        return new ApiResponse<>(true, "Review deleted successfully", null);
    }

    /**
     * Retrieves the average rating of an employee.
     *
     * @param employeeId The ID of the employee
     * @return ApiResponse containing the average rating
     */
    @Override
    public ApiResponse<Double> getAverageRating(Long employeeId) {
        List<Review> reviews = reviewRepository.findByEmployeeId(employeeId);
        if (reviews.isEmpty()) {
            LOGGER.info("No reviews found for employee with ID: {}", employeeId);
            throw new ReviewNotFoundException("No reviews found for employee with ID: " + employeeId);
        }

        double averageRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        LOGGER.info("Average Rating: {}", averageRating);
        return new ApiResponse<>(true, "Average rating retrieved successfully", averageRating);
    }
}