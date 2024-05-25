package com.suyash.api_gateway.service.impl;

import com.suyash.api_gateway.dto.ApiResponse;
import com.suyash.api_gateway.dto.AuthResponseDTO;
import com.suyash.api_gateway.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public ApiResponse<AuthResponseDTO> login(OAuth2AuthorizedClient client, OidcUser user, Model model) {
        LOGGER.info("User logged in: {}", user.getFullName());
        AuthResponseDTO authResponseDTO =  new AuthResponseDTO();

        authResponseDTO.setUserId(user.getEmail());
        authResponseDTO.setFullName(user.getFullName());
        authResponseDTO.setAccessToken(client.getAccessToken().getTokenValue());
        authResponseDTO.setRefreshToken(client.getRefreshToken().getTokenValue());
        authResponseDTO.setExpiresAt(client.getAccessToken().getExpiresAt().getEpochSecond());

        List<String> authorities =  user.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList());
        authResponseDTO.setAuthorities(authorities);

        return new ApiResponse<>(true, "User logged in successfully", authResponseDTO);
    }

}
