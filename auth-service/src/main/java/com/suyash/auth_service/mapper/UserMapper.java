package com.suyash.auth_service.mapper;

import com.suyash.auth_service.dto.User.UserResponseDTO;
import com.suyash.auth_service.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    /**
     * Maps a User entity to a UserResponseDTO.
     *
     * @param user The User entity.
     * @return The UserResponseDTO.
     */
    public UserResponseDTO mapToUserResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        return userResponseDTO;
    }
}
