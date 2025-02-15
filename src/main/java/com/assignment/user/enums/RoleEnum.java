package com.assignment.user.enums;

public enum RoleEnum {
    USER("USER"),
    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPER_ADMIN");

    private final String roleName;

    // Constructor to set the role name
    RoleEnum(String roleName) {
        this.roleName = roleName;
    }

    // Getter method to access the role name
    public String getRoleName() {
        return roleName;
    }
}
