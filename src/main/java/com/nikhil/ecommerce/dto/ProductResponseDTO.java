package com.nikhil.ecommerce.dto;

public class ProductResponseDTO {
    private Long productId;
    private String name;
    private double price;
    private int stock;

    public ProductResponseDTO(Long productId, String name, double price, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Long getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
}
