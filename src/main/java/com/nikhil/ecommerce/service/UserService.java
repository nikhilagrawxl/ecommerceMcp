package com.nikhil.ecommerce.service;

import com.nikhil.ecommerce.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, User> userStore = new HashMap<>();
    private int nextId = 1;

    public User createUser(String name, User.UserType type) {
        String id = String.valueOf(nextId++);
        User user = new User(id, name, type);
        userStore.put(id, user);
        return user;
    }

    public User getUser(String userId) {
        User user = userStore.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }
}
