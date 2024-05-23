package com.suyash.employeeservice.message;

import com.suyash.employeeservice.config.RabbitMQConfig;
import com.suyash.employeeservice.dto.ReviewMessageDTO;
import com.suyash.employeeservice.service.EmployeeService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ReviewMessageConsumer {
    private final EmployeeService employeeService;

    public ReviewMessageConsumer(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumeMessage(ReviewMessageDTO reviewMessageDTO){
        employeeService.updateEmployeeRating(reviewMessageDTO);
    }
}
