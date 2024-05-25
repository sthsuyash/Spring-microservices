package com.suyash.auth_service.controller;

import com.suyash.auth_service.dto.*;
import com.suyash.auth_service.dto.Auth.LoginResponseDTO;
import com.suyash.auth_service.dto.Auth.TokenValidationRequestDTO;
import com.suyash.auth_service.dto.Auth.UserLoginDTO;
import com.suyash.auth_service.dto.Auth.UserRegisterDTO;
import com.suyash.auth_service.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody UserLoginDTO userLoginDto) {
       return ResponseEntity.ok(authService.login(userLoginDto));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody UserRegisterDTO userRegisterDto) {
        return new ResponseEntity<>(authService.register(userRegisterDto), HttpStatus.CREATED);
    }

    @PostMapping("/verify-token")
    public ResponseEntity<ApiResponse<Void>> verifyToken(@RequestBody TokenValidationRequestDTO token) {
        return ResponseEntity.ok(authService.verifyToken(token.getToken()));
    }
}