package com.nikhil.ecommerce.service;

import com.nikhil.ecommerce.dto.ProductResponseDTO;
import com.nikhil.ecommerce.model.Product;
import com.nikhil.ecommerce.model.User;
import com.nikhil.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private com.nikhil.ecommerce.repository.UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(String name, double price, int stock, Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
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

    public java.util.List<ProductResponseDTO> getAllProductsInStock() {
        return productRepository.findAll().stream()
                .filter(p -> p.getStock() > 0)
                .map(p -> new ProductResponseDTO(
                        p.getProductId(),
                        p.getName(),
                        p.getPrice(),
                        p.getStock()
                ))
                .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<ProductResponseDTO> getProductsBySeller(String sellerId) {
        Long sid = Long.parseLong(sellerId);
        return productRepository.findAll().stream()
                .filter(p -> p.getSeller() != null && sid.equals(p.getSeller().getUserId()) && p.getStock() > 0)
                .map(p -> new ProductResponseDTO(
                        p.getProductId(),
                        p.getName(),
                        p.getPrice(),
                        p.getStock()
                ))
                .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<ProductResponseDTO> getSellerInventory(String sellerId) {
        Long sid = Long.parseLong(sellerId);
        return productRepository.findAll().stream()
                .filter(p -> p.getSeller() != null && sid.equals(p.getSeller().getUserId()))
                .map(p -> new ProductResponseDTO(
                        p.getProductId(),
                        p.getName(),
                        p.getPrice(),
                        p.getStock()
                ))
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
        User seller = userRepository.findById(sid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
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
