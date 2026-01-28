package com.nikhil.ecommerce.mcp.tools;

import com.nikhil.ecommerce.mcp.ToolRegistry;
import com.nikhil.ecommerce.service.OrderService;
import com.nikhil.ecommerce.service.ProductService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import com.nikhil.ecommerce.dto.OrderResponseDTO;

@Component
public class OrderTools {
    private final OrderService orderService;
    private final ProductService productService;
    private final ToolRegistry registry;

    public OrderTools(OrderService orderService,
                      ProductService productService,
                      ToolRegistry registry) {
        this.orderService = orderService;
        this.productService = productService;
        this.registry = registry;
    }

    @PostConstruct
    public void register() {
        Map<String, Object> createSchema = new HashMap<>();
        createSchema.put("type", "object");
        Map<String, Object> createProps = new HashMap<>();
        createProps.put("userId", new HashMap<String, Object>() {{ put("type", "string"); }});
        createSchema.put("properties", createProps);
        createSchema.put("required", new String[]{"userId"});

        registry.register("createOrder", args -> {
            String userId = (String) args.get("userId");
            if (userId == null) {
                throw new IllegalArgumentException("userId is required");
            }
            return orderService.createOrder(userId);
        }, new ToolRegistry.ToolMetadata("Create a new order", createSchema));

        Map<String, Object> addItemSchema = new HashMap<>();
        addItemSchema.put("type", "object");
        Map<String, Object> addItemProps = new HashMap<>();
        addItemProps.put("orderId", new HashMap<String, Object>() {{ put("type", "string"); }});
        addItemProps.put("buyerId", new HashMap<String, Object>() {{ put("type", "string"); }});
        addItemProps.put("productId", new HashMap<String, Object>() {{ put("type", "string"); }});
        addItemProps.put("quantity", new HashMap<String, Object>() {{ put("type", "integer"); }});
        addItemSchema.put("properties", addItemProps);
        addItemSchema.put("required", new String[]{"orderId", "buyerId", "productId", "quantity"});

        registry.register("addItem", args -> {
            String buyerId = (String) args.get("buyerId");
            if (buyerId == null || buyerId.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "buyerId is required for addItem. Example call: {orderId:'1', buyerId:'2', productId:'3', quantity:1}"
                );
            }
            String orderId = (String) args.get("orderId");
            return orderService.addItem(
                    orderId,
                    buyerId,
                    (String) args.get("productId"),
                    Integer.parseInt(args.get("quantity").toString())
            );
        }, new ToolRegistry.ToolMetadata("Add item to order", addItemSchema));

        Map<String, Object> checkoutSchema = new HashMap<>();
        checkoutSchema.put("type", "object");
        Map<String, Object> checkoutProps = new HashMap<>();
        checkoutProps.put("orderId", new HashMap<String, Object>() {{ put("type", "string"); }});
        checkoutProps.put("buyerId", new HashMap<String, Object>() {{ put("type", "string"); }});
        checkoutSchema.put("properties", checkoutProps);
        checkoutSchema.put("required", new String[]{"orderId", "buyerId"});

        registry.register("checkout", args -> {
            String orderId = (String) args.get("orderId");
            String buyerId = (String) args.get("buyerId");
            return orderService.checkout(orderId, buyerId);
        }, new ToolRegistry.ToolMetadata("Checkout order", checkoutSchema));

        // Get Buyer Order History Tool
        Map<String, Object> historySchema = new HashMap<>();
        historySchema.put("type", "object");
        Map<String, Object> historyProps = new HashMap<>();
        historyProps.put("buyerId", new HashMap<String, Object>() {{ put("type", "string"); }});
        historySchema.put("properties", historyProps);
        historySchema.put("required", new String[]{"buyerId"});

        registry.register("getMyOrders", args -> {
            String buyerId = (String) args.get("buyerId");
            if (buyerId == null) {
                throw new IllegalArgumentException("buyerId is required");
            }
            return orderService.getOrdersByBuyer(buyerId);
        }, new ToolRegistry.ToolMetadata("Get all orders for a buyer (order history)", historySchema));
    }
}
