package com.nikhil.ecommerce.model;

public class Product {
    private final String productId;
    private final String name;
    private final double price;
    private int stock;
    private String sellerId;

    public Product(String productId, String name, double price, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Product(String productId, String name, double price, int stock, String sellerId) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.sellerId = sellerId;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void reduceStock(int qty) {
        if (qty > stock) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        this.stock -= qty;
    }

    public void addStock(int qty) {
        this.stock += qty;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
