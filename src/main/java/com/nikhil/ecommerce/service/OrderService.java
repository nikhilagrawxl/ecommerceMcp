package com.nikhil.ecommerce.service;

import com.nikhil.ecommerce.model.Order;
import com.nikhil.ecommerce.model.OrderItem;
import com.nikhil.ecommerce.model.Product;
import com.nikhil.ecommerce.model.User;
import com.nikhil.ecommerce.repository.OrderRepository;
import com.nikhil.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            UserService userService
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userService = userService;
    }

    /**
     * Create a new order for a buyer
     */
    public Order createOrder(String userId) {
        Long buyerId = Long.parseLong(userId);

        User user = userService.getUser(buyerId);
        if (user.getType() != User.UserType.BUYER) {
            throw new IllegalArgumentException("Only BUYER can create orders");
        }

        Order order = new Order(buyerId);
        return orderRepository.save(order);
    }

    /**
     * Add product to an existing order
     */
    public Order addItem(String orderId, String productId, int quantity) {
        Long oid = Long.parseLong(orderId);
        Long pid = Long.parseLong(productId);

        Order order = orderRepository.findById(oid)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Product product = productRepository.findById(pid)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        product.reduceStock(quantity);

        OrderItem item = new OrderItem(
                product.getProductId(),
                quantity,
                product.getPrice()
        );

        order.addItem(item);

        productRepository.save(product);
        return orderRepository.save(order);
    }

    /**
     * Checkout an order
     */
    public Order checkout(String orderId) {
        Long oid = Long.parseLong(orderId);

        Order order = orderRepository.findById(oid)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.checkout();
        return orderRepository.save(order);
    }
}
