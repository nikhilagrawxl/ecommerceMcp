package com.nikhil.ecommerce.service;

import com.nikhil.ecommerce.model.Product;
import com.nikhil.ecommerce.model.User;
import com.nikhil.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(String name, double price, int stock, Long sellerId) {
        User seller = userService.getUser(sellerId);
        if (seller.getType() != User.UserType.SELLER) {
            throw new IllegalArgumentException("Only sellers can add products");
        }
        Product product = new Product(name, price, stock, seller);
        return productRepository.save(product);
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public java.util.List<Product> getAllProductsInStock() {
        return productRepository.findAll().stream()
                .filter(p -> p.getStock() > 0)
                .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<Product> getProductsBySeller(String sellerId) {
        Long sid = Long.parseLong(sellerId);
        return productRepository.findAll().stream()
                .filter(p -> p.getSeller() != null && sid.equals(p.getSeller().getUserId()) && p.getStock() > 0)
                .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<Product> getSellerInventory(String sellerId) {
        Long sid = Long.parseLong(sellerId);
        return productRepository.findAll().stream()
                .filter(p -> p.getSeller() != null && sid.equals(p.getSeller().getUserId()))
                .collect(java.util.stream.Collectors.toList());
    }

    public Product findByName(String name) {
        return productRepository.findAll().stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Product createOrUpdateProduct(String name, double price, int stock, String sellerId) {
        Long sid = Long.parseLong(sellerId);
        User seller = userService.getUser(sid);
        if (seller.getType() != User.UserType.SELLER) {
            throw new IllegalArgumentException("Only sellers can add products");
        }
        Product existing = findByName(name);
        if (existing != null) {
            existing.addStock(stock);
            return existing;
        }
        return createProduct(name, price, stock, sid);
    }
}
