package com.suyash.auth_service.service.impl;

import com.suyash.auth_service.dto.ApiResponse;
import com.suyash.auth_service.dto.User.UserPasswordUpdateDTO;
import com.suyash.auth_service.dto.User.UserResponseDTO;
import com.suyash.auth_service.dto.User.UserUpdateRequestDTO;
import com.suyash.auth_service.exception.UserNotFoundException;
import com.suyash.auth_service.mapper.UserMapper;
import com.suyash.auth_service.model.User;
import com.suyash.auth_service.repository.UserRepository;
import com.suyash.auth_service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the UserService interface.
 */
@Component
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * Retrieves all users.
     *
     * @return ApiResponse containing a list of all users.
     */
    @Override
    public ApiResponse<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userRepository.findAll().stream()
                .map(userMapper::mapToUserResponseDTO)
                .collect(Collectors.toList());
        LOGGER.info("Users retrieved successfully");
        return new ApiResponse<>(true, "Users retrieved successfully", users);
    }

    /**
     * Updates a user by ID.
     *
     * @param id                   The ID of the user to update.
     * @param userUpdateRequestDto The user update request data.
     * @return ApiResponse containing the updated user.
     */
    @Override
    public ApiResponse<UserResponseDTO> updateUser(Long id, UserUpdateRequestDTO userUpdateRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setFirstName(userUpdateRequestDto.getFirstName());
        user.setLastName(userUpdateRequestDto.getLastName());
        userRepository.save(user);

        UserResponseDTO updatedUser = userMapper.mapToUserResponseDTO(user);
        LOGGER.info("User updated successfully: {}", updatedUser);
        return new ApiResponse<>(true, "User updated successfully", updatedUser);
    }

    /**
     * Updates a user's password by ID.
     *
     * @param id                    The ID of the user to update.
     * @param userPasswordUpdateDto The user password update request data.
     * @return ApiResponse with no content.
     */
    @Override
    public ApiResponse<Void> updateUserPassword(Long id, UserPasswordUpdateDTO userPasswordUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(userPasswordUpdateDto.getNewPassword()));
        userRepository.save(user);

        return new ApiResponse<>(true, "User password updated successfully", null);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ApiResponse containing the user.
     */
    @Override
    public ApiResponse<UserResponseDTO> getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserResponseDTO userResponse = userMapper.mapToUserResponseDTO(user);
        return new ApiResponse<>(true, "User retrieved successfully", userResponse);
    }

    /**
     * Retrieves a user by email.
     *
     * @param email The email of the user to retrieve.
     * @return ApiResponse containing the user.
     */
    @Override
    public ApiResponse<UserResponseDTO> getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserResponseDTO userResponse = userMapper.mapToUserResponseDTO(user);
        return new ApiResponse<>(true, "User retrieved successfully", userResponse);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     * @return ApiResponse with no content.
     */
    @Override
    public ApiResponse<Void> deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(id);
        return new ApiResponse<>(true, "User deleted successfully", null);
    }
}
