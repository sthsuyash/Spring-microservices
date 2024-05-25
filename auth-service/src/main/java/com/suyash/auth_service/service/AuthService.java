package com.suyash.auth_service.service;

import com.suyash.auth_service.dto.ApiResponse;
import com.suyash.auth_service.dto.Auth.LoginResponseDTO;
import com.suyash.auth_service.dto.Auth.UserLoginDTO;
import com.suyash.auth_service.dto.Auth.UserRegisterDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ApiResponse<LoginResponseDTO> login(UserLoginDTO userLoginDTO);

    ApiResponse<Void> register(UserRegisterDTO userRegisterDTO);

    ApiResponse<Void> verifyToken(String token);
}
