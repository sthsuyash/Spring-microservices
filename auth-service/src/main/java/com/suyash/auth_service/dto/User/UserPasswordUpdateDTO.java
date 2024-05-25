package com.suyash.auth_service.dto.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordUpdateDTO {
    @Column(nullable = false)
    @JsonProperty("old_password")
    private String oldPassword;
    @Column(nullable = false)
    @JsonProperty("new_password")
    private String newPassword;
}
