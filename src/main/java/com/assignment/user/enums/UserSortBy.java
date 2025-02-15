package com.assignment.user.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserSortBy {
    USER_ID("userId"),
    NAME("name"),
    EMAIL("email");

    private final String UserSortBy;

    UserSortBy(String UserSortBy) {
        this.UserSortBy = UserSortBy;
    }
    public String getSortType() {
        return UserSortBy;
    }
}

