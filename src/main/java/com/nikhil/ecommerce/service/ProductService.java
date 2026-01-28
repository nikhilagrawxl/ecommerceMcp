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
        return productRepository.findByStockGreaterThan(0).stream()
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
        return productRepository.findBySeller_UserId(sid).stream()
                .filter(p -> p.getStock() > 0)
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
        return productRepository.findBySeller_UserId(sid).stream()
                .map(p -> new ProductResponseDTO(
                        p.getProductId(),
                        p.getName(),
                        p.getPrice(),
                        p.getStock()
                ))
                .collect(java.util.stream.Collectors.toList());
    }

    public Product findByName(String name) {
        return productRepository.findByNameIgnoreCase(name).orElse(null);
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
            // Ownership validation: seller can only update their own product
            if (existing.getSeller() == null || !existing.getSeller().getUserId().equals(sid)) {
                throw new IllegalArgumentException("You can only update your own products");
            }

            existing.addStock(stock);
            return productRepository.save(existing);
        }
        return createProduct(name, price, stock, sid);
    }
    public void deleteProduct(Long productId, Long sellerId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Ownership validation: seller can only delete their own product
        if (product.getSeller() == null || !product.getSeller().getUserId().equals(sellerId)) {
            throw new IllegalArgumentException("You can only delete your own products");
        }

        productRepository.delete(product);
    }
    public ProductResponseDTO updateStock(Long productId, Long sellerId, int newStock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Ownership validation
        if (product.getSeller() == null || !product.getSeller().getUserId().equals(sellerId)) {
            throw new IllegalArgumentException("You can only update your own products");
        }

        product.setStock(newStock);
        Product saved = productRepository.save(product);

        return new ProductResponseDTO(
                saved.getProductId(),
                saved.getName(),
                saved.getPrice(),
                saved.getStock()
        );
    }

    public ProductResponseDTO updatePrice(Long productId, Long sellerId, double newPrice) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Ownership validation
        if (product.getSeller() == null || !product.getSeller().getUserId().equals(sellerId)) {
            throw new IllegalArgumentException("You can only update your own products");
        }

        product.setPrice(newPrice);
        Product saved = productRepository.save(product);

        return new ProductResponseDTO(
                saved.getProductId(),
                saved.getName(),
                saved.getPrice(),
                saved.getStock()
        );
    }
}
