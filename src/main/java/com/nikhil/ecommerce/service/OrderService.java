package com.nikhil.ecommerce.service;

import com.nikhil.ecommerce.model.Order;
import com.nikhil.ecommerce.model.OrderItem;
import com.nikhil.ecommerce.model.Product;
import com.nikhil.ecommerce.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private int nextId = 1;
    
    @Autowired
    private UserService userService;

    public Order createOrder(String userId) {
        User user = userService.getUser(userId);
        if (user.getType() != User.UserType.BUYER && user.getType() != User.UserType.SELLER) {
            throw new IllegalArgumentException("Invalid user type for order creation");
        }
        String orderId = String.valueOf(nextId++);
        return new Order(orderId, userId);
    }

    public void addItem(Order order, Product product, int qty) {
        product.reduceStock(qty);
        order.addItem(new OrderItem(product, qty));
    }

    public double checkout(Order order) {
        return order.calculateTotal();
    }
}
