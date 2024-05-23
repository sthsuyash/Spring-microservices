package com.suyash.review_service.mapper;

import com.suyash.review_service.dto.ReviewResponseDTO;
import com.suyash.review_service.model.Review;
import org.springframework.stereotype.Component;

/**
 * Mapper class responsible for mapping Review entities to ReviewResponseDTOs.
 */
@Component
public class ReviewMapper {
    /**
     * Maps a Review entity to an ReviewResponseDTO.
     *
     * @param review The Review entity to map
     * @return The mapped ReviewResponseDTO
     */
    public ReviewResponseDTO mapToReviewResponseDTO(Review review) {
        ReviewResponseDTO responseDTO = new ReviewResponseDTO();
        responseDTO.setId(review.getId());
        responseDTO.setTitle(review.getTitle());
        responseDTO.setDescription(review.getDescription());
        responseDTO.setRating(review.getRating());
        responseDTO.setEmployeeId(review.getEmployeeId());

        return responseDTO;
    }
}
