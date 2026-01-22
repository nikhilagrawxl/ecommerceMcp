package com.nikhil.ecommerce.service;

import com.nikhil.ecommerce.model.Product;
import com.nikhil.ecommerce.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductService {
    private final Map<String, Product> productStore = new HashMap<>();
    private int nextId = 1;
    
    @Autowired
    private UserService userService;

    public Product createProduct(String name, double price, int stock, String sellerId) {
        User seller = userService.getUser(sellerId);
        if (seller.getType() != User.UserType.SELLER) {
            throw new IllegalArgumentException("Only sellers can add products");
        }
        String id = String.valueOf(nextId++);
        Product product = new Product(id, name, price, stock, sellerId);
        productStore.put(id, product);
        return product;
    }

    public Product getProduct(String productId) {
        Product product = productStore.get(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        return product;
    }

    public java.util.List<Product> getAllProductsInStock() {
        return productStore.values().stream()
                .filter(p -> p.getStock() > 0)
                .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<Product> getProductsBySeller(String sellerId) {
        return productStore.values().stream()
                .filter(p -> sellerId.equals(p.getSellerId()) && p.getStock() > 0)
                .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<Product> getSellerInventory(String sellerId) {
        return productStore.values().stream()
                .filter(p -> sellerId.equals(p.getSellerId()))
                .collect(java.util.stream.Collectors.toList());
    }

    public Product findByName(String name) {
        return productStore.values().stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Product createOrUpdateProduct(String name, double price, int stock, String sellerId) {
        User seller = userService.getUser(sellerId);
        if (seller.getType() != User.UserType.SELLER) {
            throw new IllegalArgumentException("Only sellers can add products");
        }
        Product existing = findByName(name);
        if (existing != null) {
            existing.addStock(stock);
            return existing;
        }
        return createProduct(name, price, stock, sellerId);
    }
}
