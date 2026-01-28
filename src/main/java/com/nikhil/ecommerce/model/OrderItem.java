package com.nikhil.ecommerce.model;

import javax.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Required by JPA
    protected OrderItem() {}

    public OrderItem(Long productId, int quantity, double price) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    void setOrder(Order order) {
        this.order = order;
    }
}
