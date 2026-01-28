package com.nikhil.ecommerce.dto;

public class UserResponseDTO {
    private Long userId;
    private String name;
    private String type;

    public UserResponseDTO(Long userId, String name, String type) {
        this.userId = userId;
        this.name = name;
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
