package com.nikhil.ecommerce.mcp.tools;

import com.nikhil.ecommerce.mcp.ToolRegistry;
import com.nikhil.ecommerce.model.Order;
import com.nikhil.ecommerce.service.OrderService;
import com.nikhil.ecommerce.service.ProductService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderTools {
    private final OrderService orderService;
    private final ProductService productService;
    private final ToolRegistry registry;
    private final Map<String, Order> orderCache = new ConcurrentHashMap<>();

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
            Order order = orderService.createOrder(userId);
            orderCache.put(order.getOrderId(), order);
            return order;
        }, new ToolRegistry.ToolMetadata("Create a new order", createSchema));

        Map<String, Object> addItemSchema = new HashMap<>();
        addItemSchema.put("type", "object");
        Map<String, Object> addItemProps = new HashMap<>();
        addItemProps.put("orderId", new HashMap<String, Object>() {{ put("type", "string"); }});
        addItemProps.put("productId", new HashMap<String, Object>() {{ put("type", "string"); }});
        addItemProps.put("quantity", new HashMap<String, Object>() {{ put("type", "integer"); }});
        addItemSchema.put("properties", addItemProps);
        addItemSchema.put("required", new String[]{"orderId", "productId", "quantity"});

        registry.register("addItem", args -> {
            Order order = orderCache.get((String) args.get("orderId"));
            orderService.addItem(
                    order,
                    productService.getProduct((String) args.get("productId")),
                    Integer.parseInt(args.get("quantity").toString())
            );
            return "Item added";
        }, new ToolRegistry.ToolMetadata("Add item to order", addItemSchema));

        Map<String, Object> checkoutSchema = new HashMap<>();
        checkoutSchema.put("type", "object");
        Map<String, Object> checkoutProps = new HashMap<>();
        checkoutProps.put("orderId", new HashMap<String, Object>() {{ put("type", "string"); }});
        checkoutSchema.put("properties", checkoutProps);
        checkoutSchema.put("required", new String[]{"orderId"});

        registry.register("checkout", args -> {
            Order order = orderCache.get((String) args.get("orderId"));
            return orderService.checkout(order);
        }, new ToolRegistry.ToolMetadata("Checkout order", checkoutSchema));
    }
}
