package com.nikhil.ecommerce.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CreateOrderRequest {
    @NotBlank
    private String orderId;

    @NotEmpty
    private List<OrderItemRequest> items;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "CreateOrderRequest{" +
                "orderId='" + orderId + '\'' +
                ", items=" + items +
                '}';
    }
}
