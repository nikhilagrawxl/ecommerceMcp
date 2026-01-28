package com.nikhil.ecommerce.repository;

import com.nikhil.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Fetch all orders belonging to a specific buyer
    List<Order> findByBuyerId(Long buyerId);
}
