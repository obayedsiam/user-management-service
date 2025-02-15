package com.assignment.user.enums;

public enum SortType {
    DESC("DESC"),
    ASC("ASC");
    private final String SortType;

    SortType(String SortType) {
        this.SortType = SortType;
    }
    public String getSortType() {
        return SortType;
    }
}