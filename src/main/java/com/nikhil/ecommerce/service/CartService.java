package com.nikhil.ecommerce.service;


import com.nikhil.ecommerce.dto.OrderResponseDTO;
import com.nikhil.ecommerce.model.*;
import com.nikhil.ecommerce.repository.CartRepository;
import com.nikhil.ecommerce.repository.OrderRepository;
import com.nikhil.ecommerce.repository.ProductRepository;
import com.nikhil.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    private final OrderService orderService; // reuse DTO mapping

    public CartService(CartRepository cartRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository,
                       OrderRepository orderRepository,
                       OrderService orderService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    // ✅ Add item to cart
    public Cart addToCart(Long buyerId, Long productId, int quantity) {

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("Buyer not found"));

        if (buyer.getType() != User.UserType.BUYER) {
            throw new IllegalArgumentException("Only BUYER can add items to cart");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Cart cart = cartRepository.findByBuyer_UserId(buyerId)
                .orElseGet(() -> cartRepository.save(new Cart(buyer)));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().increaseQty(quantity);
        } else {
            CartItem item = new CartItem(cart, product, quantity);
            cart.getItems().add(item);
        }

        return cartRepository.save(cart);
    }

    // ✅ View cart
    public Cart viewCart(Long buyerId) {
        return cartRepository.findByBuyer_UserId(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
    }

    // ✅ Remove item from cart
    public Cart removeFromCart(Long buyerId, Long productId) {
        Cart cart = viewCart(buyerId);

        cart.getItems().removeIf(item ->
                item.getProduct().getProductId().equals(productId)
        );

        return cartRepository.save(cart);
    }

    // ⭐ Checkout cart → Create Order automatically
    public OrderResponseDTO checkoutCart(Long buyerId) {

        Cart cart = viewCart(buyerId);

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        // Create new Order
        Order order = new Order(buyerId);

        // Convert CartItems → OrderItems
        for (CartItem cartItem : cart.getItems()) {

            Product product = cartItem.getProduct();

            // Reduce stock
            product.reduceStock(cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem(
                    product,
                    cartItem.getQuantity(),
                    product.getPrice()
            );

            order.addItem(orderItem);
        }

        Order savedOrder = orderRepository.save(order);

        // Clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        return orderService.mapToDTO(savedOrder);
    }
}
