package com.nikhil.ecommerce.mcp.tools;

import com.nikhil.ecommerce.mcp.ToolRegistry;
import com.nikhil.ecommerce.model.User;
import com.nikhil.ecommerce.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserTools {
    private final UserService userService;
    private final ToolRegistry registry;

    public UserTools(UserService userService, ToolRegistry registry) {
        this.userService = userService;
        this.registry = registry;
    }

    @PostConstruct
    public void register() {
        Map<String, Object> createSchema = new HashMap<>();
        createSchema.put("type", "object");
        Map<String, Object> createProps = new HashMap<>();
        createProps.put("name", new HashMap<String, Object>() {{ put("type", "string"); }});
        createProps.put("userType", new HashMap<String, Object>() {{ 
            put("type", "string"); 
            put("enum", new String[]{"BUYER", "SELLER"});
        }});
        createSchema.put("properties", createProps);
        createSchema.put("required", new String[]{"name", "userType"});

        registry.register("createUser", args ->
                userService.createUser(
                        (String) args.get("name"),
                        User.UserType.valueOf(((String) args.get("userType")).toUpperCase())
                ), new ToolRegistry.ToolMetadata("Create a new user (BUYER or SELLER)", createSchema));

        Map<String, Object> getSchema = new HashMap<>();
        getSchema.put("type", "object");
        Map<String, Object> getProps = new HashMap<>();
        getProps.put("userId", new HashMap<String, Object>() {{ put("type", "string"); }});
        getSchema.put("properties", getProps);
        getSchema.put("required", new String[]{"userId"});

        registry.register("getUser", args ->
                userService.getUser(Long.parseLong((String) args.get("userId"))),
                new ToolRegistry.ToolMetadata("Get user by ID", getSchema));
    }
}
