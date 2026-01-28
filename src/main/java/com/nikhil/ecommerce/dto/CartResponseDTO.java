package com.nikhil.ecommerce.dto;

import java.util.List;

public class CartResponseDTO {
    private Long buyerId;
    private List<CartItemResponseDTO> items;
    private double totalAmount;

    public CartResponseDTO(Long buyerId,
                           List<CartItemResponseDTO> items,
                           double totalAmount) {
        this.buyerId = buyerId;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public List<CartItemResponseDTO> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
