package com.assignment.user.response;

import com.assignment.user.entities.User;
import com.assignment.user.enums.PermissionEnum;
import lombok.Data;

import java.util.Set;

@Data
public class UserResponseDto {
    private Boolean enabled;
    private Long id;
    private String uuid;
    private String username;
    private String email;
    private String createdBy;
    private String role;
    private Set<PermissionEnum> permissionSet;

    public static UserResponseDto getResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setUuid(user.getId());
        dto.setId(user.getUserId());
        dto.setUsername(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().getRoleName().getRoleName());
        dto.setPermissionSet(user.getPermissionSet());
        dto.setEnabled(user.getEnabled());
        dto.setCreatedBy(user.getCreatedBy() != null ? user.getCreatedBy().getId() : null);
        return dto;
    }
}
