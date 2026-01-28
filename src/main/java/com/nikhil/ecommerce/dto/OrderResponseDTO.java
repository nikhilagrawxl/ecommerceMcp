package com.nikhil.ecommerce.dto;

import java.util.List;

public class OrderResponseDTO {
    private Long orderId;
    private Long buyerId;
    private String status;
    private double totalAmount;
    private List<OrderItemResponseDTO> items;

    public OrderResponseDTO(Long orderId,
                            Long buyerId,
                            String status,
                            double totalAmount,
                            List<OrderItemResponseDTO> items) {
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public List<OrderItemResponseDTO> getItems() {
        return items;
    }
}
