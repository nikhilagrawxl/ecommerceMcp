package com.nikhil.ecommerce.model;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int stock;

    private Long sellerId;

    // Required by JPA
    protected Product() {
    }

    public Product(String name, double price, int stock, Long sellerId) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.sellerId = sellerId;
    }

    public Long getProductId() {
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

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
}
