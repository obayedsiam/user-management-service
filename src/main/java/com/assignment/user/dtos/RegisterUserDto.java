package com.assignment.user.dtos;

import com.assignment.user.enums.PermissionEnum;
import com.assignment.user.enums.RoleEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;


@Data
@Accessors(chain = true)
public class RegisterUserDto {

    @Schema(description = "User's email address", example = "rahim@example.com")
    private String email;

    @Schema(description = "User's password", example = "password123")
    private String password;

    @Schema(description = "User's full name", example = "Abdur Rahim")
    private String fullName;

    @Schema(description = "Role of the user", example = "ADMIN")
    private RoleEnum roleEnum;

    @Schema(
            description = "Set of permissions assigned to the user",
            example = "[\"CREATE\", \"READ\", \"UPDATE\", \"DELETE\"]",
            required = false
    )
    private Set<PermissionEnum> permissions;
}

