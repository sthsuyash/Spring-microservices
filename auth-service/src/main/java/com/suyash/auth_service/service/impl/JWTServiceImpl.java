package com.suyash.auth_service.service.impl;

import com.suyash.auth_service.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the JwtService interface.
 * This service is responsible for generating and validating JWT tokens.
 */
@Component
public class JWTServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String SECRET;

    /**
     * Validates the provided JWT token.
     *
     * @param token The JWT token to validate.
     * @throws IllegalArgumentException if the token is invalid.
     */
    @Override
    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token", e);
        }
    }

    /**
     * Generates a JWT token for the provided user details.
     *
     * @param userDetails The user details for which to generate the token.
     * @return The generated JWT token.
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Creates a JWT token with the provided claims and subject.
     *
     * @param claims The claims to include in the token.
     * @param subject The subject of the token.
     * @return The created JWT token.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Retrieves the signing key for the JWT tokens.
     *
     * @return The signing key.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}