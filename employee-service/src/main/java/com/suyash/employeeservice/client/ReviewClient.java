package com.suyash.employeeservice.client;

import com.suyash.employeeservice.dto.ApiResponse;
import com.suyash.employeeservice.dto.ReviewDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "review-service")
public interface ReviewClient {
    @GetMapping("/reviews")
    ApiResponse<List<ReviewDTO>> getReviewByEmployeeId(@RequestParam("employeeId") Long employeeId);
}
