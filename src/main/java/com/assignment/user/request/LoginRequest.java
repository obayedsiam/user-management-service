package com.assignment.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Login request payload containing username and password")
public class LoginRequest {

    @Schema(description = "User's email or username", example = "super.admin@email.com")
    private String username;

    @Schema(description = "User's password", example = "admin123", format = "password")
    private String password;
}

