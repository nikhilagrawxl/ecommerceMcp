package com.nikhil.ecommerce.model;

public class User {
    private final String userId;
    private final String name;
    private final UserType type;

    public User(String userId, String name, UserType type) {
        this.userId = userId;
        this.name = name;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public UserType getType() {
        return type;
    }

    public enum UserType {
        BUYER, SELLER
    }
}
