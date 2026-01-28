package com.nikhil.ecommerce.mcp.tools;

import com.nikhil.ecommerce.mcp.ToolRegistry;
import com.nikhil.ecommerce.service.ProductService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProductTools {
    private final ProductService productService;
    private final ToolRegistry registry;

    public ProductTools(ProductService productService, ToolRegistry registry) {
        this.productService = productService;
        this.registry = registry;
    }

    @PostConstruct
    public void register() {
        Map<String, Object> createSchema = new HashMap<>();
        createSchema.put("type", "object");
        Map<String, Object> createProps = new HashMap<>();
        createProps.put("name", new HashMap<String, Object>() {{ put("type", "string"); }});
        createProps.put("price", new HashMap<String, Object>() {{ put("type", "number"); }});
        createProps.put("stock", new HashMap<String, Object>() {{ put("type", "integer"); }});
        createProps.put("sellerId", new HashMap<String, Object>() {{ put("type", "string"); }});
        createSchema.put("properties", createProps);
        createSchema.put("required", new String[]{"name", "price", "stock", "sellerId"});

        registry.register("createProduct", args -> {
            return productService.createOrUpdateProduct(
                    (String) args.get("name"),
                    Double.parseDouble(args.get("price").toString()),
                    Integer.parseInt(args.get("stock").toString()),
                    (String) args.get("sellerId")
            );
        }, new ToolRegistry.ToolMetadata("Create a new product or add stock to existing product with same name", createSchema));

        Map<String, Object> getSchema = new HashMap<>();
        getSchema.put("type", "object");
        Map<String, Object> getProps = new HashMap<>();
        getProps.put("productId", new HashMap<String, Object>() {{ put("type", "string"); }});
        getSchema.put("properties", getProps);
        getSchema.put("required", new String[]{"productId"});

        registry.register("getProduct", args ->
                productService.getProduct(Long.parseLong(args.get("productId").toString())),
                new ToolRegistry.ToolMetadata("Get product by ID", getSchema));

        Map<String, Object> listSchema = new HashMap<>();
        listSchema.put("type", "object");
        listSchema.put("properties", new HashMap<>());

        registry.register("getAllProducts", args ->
                productService.getAllProductsInStock(),
                new ToolRegistry.ToolMetadata("Show all products that are in stock", listSchema));

        Map<String, Object> sellerSchema = new HashMap<>();
        sellerSchema.put("type", "object");
        Map<String, Object> sellerProps = new HashMap<>();
        sellerProps.put("sellerId", new HashMap<String, Object>() {{ put("type", "string"); }});
        sellerSchema.put("properties", sellerProps);
        sellerSchema.put("required", new String[]{"sellerId"});

        registry.register("getSellerProducts", args -> {
            String sellerId = (String) args.get("sellerId");
            return productService.getProductsBySeller(sellerId);
        }, new ToolRegistry.ToolMetadata("Show products for a specific seller", sellerSchema));

        registry.register("getMyInventory", args -> {
            String sellerId = (String) args.get("sellerId");
            return productService.getSellerInventory(sellerId);
        }, new ToolRegistry.ToolMetadata("Show seller's own inventory with stock levels", sellerSchema));

        // Delete Product Tool
        Map<String, Object> deleteSchema = new HashMap<>();
        deleteSchema.put("type", "object");
        Map<String, Object> deleteProps = new HashMap<>();
        deleteProps.put("productId", new HashMap<String, Object>() {{ put("type", "string"); }});
        deleteProps.put("sellerId", new HashMap<String, Object>() {{ put("type", "string"); }});
        deleteSchema.put("properties", deleteProps);
        deleteSchema.put("required", new String[]{"productId", "sellerId"});

        registry.register("deleteProduct", args -> {
            Long productId = Long.parseLong(args.get("productId").toString());
            Long sellerId = Long.parseLong(args.get("sellerId").toString());

            productService.deleteProduct(productId, sellerId);
            return "Product deleted successfully";
        }, new ToolRegistry.ToolMetadata("Delete a product (seller can only delete own products)", deleteSchema));

        // Update Stock Tool
        Map<String, Object> stockSchema = new HashMap<>();
        stockSchema.put("type", "object");
        Map<String, Object> stockProps = new HashMap<>();
        stockProps.put("productId", new HashMap<String, Object>() {{ put("type", "string"); }});
        stockProps.put("sellerId", new HashMap<String, Object>() {{ put("type", "string"); }});
        stockProps.put("stock", new HashMap<String, Object>() {{ put("type", "integer"); }});
        stockSchema.put("properties", stockProps);
        stockSchema.put("required", new String[]{"productId", "sellerId", "stock"});

        registry.register("updateStock", args -> {
            Long productId = Long.parseLong(args.get("productId").toString());
            Long sellerId = Long.parseLong(args.get("sellerId").toString());
            int stock = Integer.parseInt(args.get("stock").toString());

            return productService.updateStock(productId, sellerId, stock);
        }, new ToolRegistry.ToolMetadata("Update stock of a product (seller-only)", stockSchema));


        // Update Price Tool
        Map<String, Object> priceSchema = new HashMap<>();
        priceSchema.put("type", "object");
        Map<String, Object> priceProps = new HashMap<>();
        priceProps.put("productId", new HashMap<String, Object>() {{ put("type", "string"); }});
        priceProps.put("sellerId", new HashMap<String, Object>() {{ put("type", "string"); }});
        priceProps.put("price", new HashMap<String, Object>() {{ put("type", "number"); }});
        priceSchema.put("properties", priceProps);
        priceSchema.put("required", new String[]{"productId", "sellerId", "price"});

        registry.register("updatePrice", args -> {
            Long productId = Long.parseLong(args.get("productId").toString());
            Long sellerId = Long.parseLong(args.get("sellerId").toString());
            double price = Double.parseDouble(args.get("price").toString());

            return productService.updatePrice(productId, sellerId, price);
        }, new ToolRegistry.ToolMetadata("Update price of a product (seller-only)", priceSchema));
    }
}
