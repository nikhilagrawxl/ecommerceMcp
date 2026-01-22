package com.nikhil.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String orderId;
    private final List<OrderItem> items = new ArrayList<>();
    private double discount; // percentage
    private String buyerId;

    public Order(String orderId) {
        this.orderId = orderId;
    }

    public Order(String orderId, String buyerId) {
        this.orderId = orderId;
        this.buyerId = buyerId;
    }

    public void addItem(OrderItem item) {
        items.add(item);
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

    public List<OrderItem> getItems() {
        return items;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }
}
