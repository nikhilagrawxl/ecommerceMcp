package com.nikhil.ecommerce.repository;

import com.nikhil.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Fetch only products that are in stock
    List<Product> findByStockGreaterThan(int stock);

    // Fetch all products belonging to a specific seller
    List<Product> findBySeller_UserId(Long sellerId);

    // Find product by name (case-insensitive)
    Optional<Product> findByNameIgnoreCase(String name);
}
