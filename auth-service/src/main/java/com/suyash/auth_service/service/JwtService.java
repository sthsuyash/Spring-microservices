package com.suyash.auth_service.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {
    void validateToken(String token);
    String generateToken(UserDetails userDetails);
}
