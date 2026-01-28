package com.nikhil.ecommerce.mcp.tools;

import com.nikhil.ecommerce.mcp.ToolRegistry;
import com.nikhil.ecommerce.service.CartService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class CartTools {

    private final CartService cartService;
    private final ToolRegistry registry;

    public CartTools(CartService cartService, ToolRegistry registry) {
        this.cartService = cartService;
        this.registry = registry;
    }

    @PostConstruct
    public void register() {

        // ----------------------------
        // Add To Cart Tool
        // ----------------------------
        Map<String, Object> addSchema = new HashMap<>();
        addSchema.put("type", "object");

        Map<String, Object> addProps = new HashMap<>();
        addProps.put("buyerId", new HashMap<String, Object>() {{
            put("type", "string");
        }});
        addProps.put("productId", new HashMap<String, Object>() {{
            put("type", "string");
        }});
        addProps.put("quantity", new HashMap<String, Object>() {{
            put("type", "integer");
        }});

        addSchema.put("properties", addProps);
        addSchema.put("required", new String[]{"buyerId", "productId", "quantity"});

        registry.register("addToCart", args -> {
            Long buyerId = Long.parseLong(args.get("buyerId").toString());
            Long productId = Long.parseLong(args.get("productId").toString());
            int quantity = Integer.parseInt(args.get("quantity").toString());

            return cartService.addToCart(buyerId, productId, quantity);
        }, new ToolRegistry.ToolMetadata("Add a product to buyer cart", addSchema));


        // ----------------------------
        // View Cart Tool
        // ----------------------------
        Map<String, Object> viewSchema = new HashMap<>();
        viewSchema.put("type", "object");

        Map<String, Object> viewProps = new HashMap<>();
        viewProps.put("buyerId", new HashMap<String, Object>() {{
            put("type", "string");
        }});

        viewSchema.put("properties", viewProps);
        viewSchema.put("required", new String[]{"buyerId"});

        registry.register("viewCart", args -> {
            Long buyerId = Long.parseLong(args.get("buyerId").toString());
            return cartService.viewCart(buyerId);
        }, new ToolRegistry.ToolMetadata("View buyer cart", viewSchema));


        // ----------------------------
        // Remove From Cart Tool
        // ----------------------------
        Map<String, Object> removeSchema = new HashMap<>();
        removeSchema.put("type", "object");

        Map<String, Object> removeProps = new HashMap<>();
        removeProps.put("buyerId", new HashMap<String, Object>() {{
            put("type", "string");
        }});
        removeProps.put("productId", new HashMap<String, Object>() {{
            put("type", "string");
        }});

        removeSchema.put("properties", removeProps);
        removeSchema.put("required", new String[]{"buyerId", "productId"});

        registry.register("removeFromCart", args -> {
            Long buyerId = Long.parseLong(args.get("buyerId").toString());
            Long productId = Long.parseLong(args.get("productId").toString());

            return cartService.removeFromCart(buyerId, productId);
        }, new ToolRegistry.ToolMetadata("Remove a product from buyer cart", removeSchema));


        // ----------------------------
        // Checkout Cart Tool
        // ----------------------------
        Map<String, Object> checkoutSchema = new HashMap<>();
        checkoutSchema.put("type", "object");

        Map<String, Object> checkoutProps = new HashMap<>();
        checkoutProps.put("buyerId", new HashMap<String, Object>() {{
            put("type", "string");
        }});

        checkoutSchema.put("properties", checkoutProps);
        checkoutSchema.put("required", new String[]{"buyerId"});

        registry.register("checkoutCart", args -> {
            Long buyerId = Long.parseLong(args.get("buyerId").toString());
            return cartService.checkoutCart(buyerId);
        }, new ToolRegistry.ToolMetadata(
                "Checkout buyer cart and create a new order",
                checkoutSchema
        ));
    }
}