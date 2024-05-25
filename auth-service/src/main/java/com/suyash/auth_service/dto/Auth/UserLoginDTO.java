package com.suyash.auth_service.dto.Auth;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
}
