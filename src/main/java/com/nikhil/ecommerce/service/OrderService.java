package com.nikhil.ecommerce.service;

import com.nikhil.ecommerce.model.Order;
import com.nikhil.ecommerce.model.OrderItem;
import com.nikhil.ecommerce.model.Product;
import com.nikhil.ecommerce.model.User;
import com.nikhil.ecommerce.repository.OrderRepository;
import com.nikhil.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import com.nikhil.ecommerce.dto.OrderItemResponseDTO;
import com.nikhil.ecommerce.dto.OrderResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final com.nikhil.ecommerce.repository.UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            com.nikhil.ecommerce.repository.UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new order for a buyer
     */
    public OrderResponseDTO createOrder(String userId) {
        Long buyerId = Long.parseLong(userId);

        User user = userRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getType() != User.UserType.BUYER) {
            throw new IllegalArgumentException("Only BUYER can create orders");
        }

        Order order = new Order(buyerId);
        Order saved = orderRepository.save(order);
        return mapToDTO(saved);
    }

    /**
     * Add product to an existing order
     */
    public OrderResponseDTO addItem(String orderId, String productId, int quantity) {
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
                product,
                quantity,
                product.getPrice()
        );

        order.addItem(item);
        item.setOrder(order);

        productRepository.save(product);
        Order saved = orderRepository.save(order);
        return mapToDTO(saved);
    }

    /**
     * Checkout an order
     */
    public OrderResponseDTO checkout(String orderId) {
        Long oid = Long.parseLong(orderId);

        Order order = orderRepository.findById(oid)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.checkout();
        Order saved = orderRepository.save(order);
        return mapToDTO(saved);
    }

    private OrderResponseDTO mapToDTO(Order order) {
        List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
                .map(i -> new OrderItemResponseDTO(
                        i.getProduct().getProductId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPrice()
                ))
                .collect(Collectors.toList());

        double totalAmount = itemDTOs.stream()
                .mapToDouble(OrderItemResponseDTO::getTotalPrice)
                .sum();

        return new OrderResponseDTO(
                order.getOrderId(),
                order.getBuyerId(),
                order.getStatus().name(),
                totalAmount,
                itemDTOs
        );
    }
}
