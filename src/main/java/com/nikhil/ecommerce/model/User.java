package com.nikhil.ecommerce.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType type;

    protected User() {
    }

    public User(String name, UserType type) {
        this.name = name;
        this.type = type;
    }

    public Long getUserId() {
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
