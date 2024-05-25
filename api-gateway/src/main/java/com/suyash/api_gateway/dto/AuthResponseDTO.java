package com.suyash.api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String userId;
    private String fullName;
    private String accessToken;
    private String refreshToken;
    private Long expiresAt;
    private Collection<String> authorities;
}
