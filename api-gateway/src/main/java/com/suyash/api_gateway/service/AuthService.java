package com.suyash.api_gateway.service;

import com.suyash.api_gateway.dto.ApiResponse;
import com.suyash.api_gateway.dto.AuthResponseDTO;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public interface AuthService {
    ApiResponse<AuthResponseDTO> login(OAuth2AuthorizedClient client, OidcUser user, Model model);
}
