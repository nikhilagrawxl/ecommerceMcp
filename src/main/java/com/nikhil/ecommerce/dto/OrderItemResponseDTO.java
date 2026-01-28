package com.nikhil.ecommerce.dto;

public class OrderItemResponseDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private double price;
    private double totalPrice;

    public OrderItemResponseDTO(Long productId, String productName, int quantity, double price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = price * quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
