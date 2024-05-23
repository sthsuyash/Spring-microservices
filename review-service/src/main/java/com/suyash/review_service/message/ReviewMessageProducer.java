package com.suyash.review_service.message;

import com.suyash.review_service.config.RabbitMQConfig;
import com.suyash.review_service.dto.ReviewMessageDTO;
import com.suyash.review_service.dto.ReviewResponseDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReviewMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public ReviewMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(ReviewResponseDTO reviewResponseDTO){
        ReviewMessageDTO reviewMessageDTO = new ReviewMessageDTO();
        reviewMessageDTO.setId(reviewResponseDTO.getId());
        reviewMessageDTO.setTitle(reviewResponseDTO.getTitle());
        reviewMessageDTO.setDescription(reviewResponseDTO.getDescription());
        reviewMessageDTO.setRating(reviewResponseDTO.getRating());
        reviewMessageDTO.setEmployeeId(reviewResponseDTO.getEmployeeId());

        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, reviewMessageDTO);
    }
}
