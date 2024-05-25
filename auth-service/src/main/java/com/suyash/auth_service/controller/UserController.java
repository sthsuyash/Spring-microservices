package com.suyash.auth_service.controller;

import com.suyash.auth_service.dto.*;
import com.suyash.auth_service.dto.User.UserPasswordUpdateDTO;
import com.suyash.auth_service.dto.User.UserResponseDTO;
import com.suyash.auth_service.dto.User.UserUpdateRequestDTO;
import com.suyash.auth_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable Long id, @RequestBody UserUpdateRequestDTO userUpdateDto) {
        return ResponseEntity.ok(userService.updateUser(id, userUpdateDto));
    }

    @PatchMapping("/update-password/{id}")
    public ResponseEntity<ApiResponse<Void>> updateUserPassword(
            @PathVariable Long id, @RequestBody UserPasswordUpdateDTO userPasswordUpdateDto) {
        return ResponseEntity.ok(userService.updateUserPassword(id, userPasswordUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
