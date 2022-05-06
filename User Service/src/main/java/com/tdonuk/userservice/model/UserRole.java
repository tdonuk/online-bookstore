package com.tdonuk.userservice.model;

public enum UserRole {
    ADMIN("Admin"), USER("User");

    private String text;
    String getText() {
        return this.text;
    }

    UserRole(String text) {
        this.text = text;
    }
}
