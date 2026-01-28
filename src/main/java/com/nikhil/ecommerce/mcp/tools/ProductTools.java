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
    }
}
