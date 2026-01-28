package com.nikhil.ecommerce.service;


import com.nikhil.ecommerce.dto.CartItemResponseDTO;
import com.nikhil.ecommerce.dto.CartResponseDTO;
import com.nikhil.ecommerce.dto.OrderResponseDTO;
import com.nikhil.ecommerce.exception.BadRequestException;
import com.nikhil.ecommerce.exception.NotFoundException;
import com.nikhil.ecommerce.exception.UnauthorizedException;
import com.nikhil.ecommerce.model.*;
import com.nikhil.ecommerce.repository.CartRepository;
import com.nikhil.ecommerce.repository.OrderRepository;
import com.nikhil.ecommerce.repository.ProductRepository;
import com.nikhil.ecommerce.repository.UserRepository;
import lombok.var;
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
    public CartResponseDTO addToCart(Long buyerId, Long productId, int quantity) {

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new NotFoundException("Buyer not found"));

        if (buyer.getType() != User.UserType.BUYER) {
            throw new UnauthorizedException("Only BUYER can add items to cart");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

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

        cartRepository.save(cart);
        return viewCart(buyerId);
    }

    // ✅ View cart
    public CartResponseDTO viewCart(Long buyerId) {
        Cart cart = cartRepository.findByBuyer_UserId(buyerId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        var itemDTOs = cart.getItems().stream()
                .map(i -> new CartItemResponseDTO(
                        i.getProduct().getProductId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getProduct().getPrice()
                ))
                .toList();

        double total = itemDTOs.stream()
                .mapToDouble(CartItemResponseDTO::getTotalPrice)
                .sum();

        return new CartResponseDTO(buyerId, itemDTOs, total);
    }

    // ✅ Remove item from cart
    public CartResponseDTO removeFromCart(Long buyerId, Long productId) {

        Cart cart = cartRepository.findByBuyer_UserId(buyerId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        cart.getItems().removeIf(item ->
                item.getProduct().getProductId().equals(productId)
        );

        cartRepository.save(cart);
        return viewCart(buyerId);
    }

    // ⭐ Checkout cart → Create Order automatically
    public OrderResponseDTO checkoutCart(Long buyerId) {

        Cart cart = cartRepository.findByBuyer_UserId(buyerId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
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
