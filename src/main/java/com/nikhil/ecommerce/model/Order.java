package com.nikhil.ecommerce.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Column(name = "discount", nullable = false)
    private double discount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    protected Order() {}

    public Order(Long buyerId) {
        this.buyerId = buyerId;
        this.status = OrderStatus.CREATED;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void applyDiscount(double discount) {
        this.discount = discount;
    }

    public double calculateTotal() {
        double total = items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
        return total - (total * discount / 100);
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void checkout() {
        this.status = OrderStatus.CHECKED_OUT;
    }
}
