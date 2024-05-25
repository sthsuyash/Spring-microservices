package com.suyash.auth_service.service.impl;

import com.suyash.auth_service.dto.ApiResponse;
import com.suyash.auth_service.dto.Auth.LoginResponseDTO;
import com.suyash.auth_service.dto.Auth.UserLoginDTO;
import com.suyash.auth_service.dto.Auth.UserRegisterDTO;
import com.suyash.auth_service.model.User;
import com.suyash.auth_service.repository.UserRepository;
import com.suyash.auth_service.service.AuthService;
import com.suyash.auth_service.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Implementation of the AuthService interface.
 */
@Component
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor for AuthServiceImpl.
     *
     * @param userRepository The user repository
     */
    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    /**
     * Login a user with the provided credentials.
     *
     * @param userLoginDTO The user's login credentials.
     * @return ApiResponse with the login response data.
     * @throws IllegalArgumentException if the user is not found or the credentials are invalid.
     */
    @Override
    public ApiResponse<LoginResponseDTO> login(UserLoginDTO userLoginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (!authentication.isAuthenticated()) {
            LOGGER.error("Invalid credentials for user with email: {}", userLoginDTO.getEmail());
            throw new IllegalArgumentException("Invalid credentials");
        }

        // generate a JWT token
        String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());
        LOGGER.info("User with email: {} successfully logged in", userLoginDTO.getEmail());

        return new ApiResponse<>(true, "Login successful", new LoginResponseDTO(token));
    }

    /**
     * Register a new user.
     *
     * @param userRegisterDTO The user's registration data.
     * @return ApiResponse with the registration response data.
     * @throws IllegalArgumentException if the email already exists.
     */
    @Override
    public ApiResponse<Void> register(UserRegisterDTO userRegisterDTO) {
        // check if the required fields are present
        if (userRegisterDTO.getEmail() == null || userRegisterDTO.getPassword() == null
                || userRegisterDTO.getFirstName() == null || userRegisterDTO.getLastName() == null) {
            LOGGER.error("User registration data not provided");
            throw new IllegalArgumentException("Required fields not provided");
        }
        // check if the email already exists
        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            LOGGER.error("Email already exists: {}", userRegisterDTO.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }

        // create a new user
        User newUser = new User();
        newUser.setFirstName(userRegisterDTO.getFirstName());
        newUser.setLastName(userRegisterDTO.getLastName());
        newUser.setEmail(userRegisterDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        userRepository.save(newUser);

        LOGGER.info("User registered successfully with email: {}", userRegisterDTO.getEmail());

        return new ApiResponse<>(true, "User registered successfully", null);
    }

    /**
     * Verify a JWT token.
     *
     * @param token The JWT token to verify.
     * @return ApiResponse with the verification response data.
     */
    @Override
    public ApiResponse<Void> verifyToken(String token) {
        LOGGER.info("Verifying token: {}", token);
        jwtService.validateToken(token);
        LOGGER.info("Token verified: {}", token);
        return new ApiResponse<>(true, "Token verified successfully", null);
    }
}