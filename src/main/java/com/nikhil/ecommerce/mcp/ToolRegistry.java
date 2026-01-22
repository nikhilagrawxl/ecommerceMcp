package com.nikhil.ecommerce.mcp;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ToolRegistry {
    private final Map<String, Function<Map<String, Object>, Object>> tools = new HashMap<>();
    private final Map<String, ToolMetadata> metadata = new HashMap<>();

    public void register(String name, Function<Map<String, Object>, Object> handler, ToolMetadata meta) {
        tools.put(name, handler);
        metadata.put(name, meta);
    }

    public Function<Map<String, Object>, Object> get(String name) {
        return tools.get(name);
    }

    public Map<String, Function<Map<String, Object>, Object>> getAll() {
        return tools;
    }

    public ToolMetadata getMetadata(String name) {
        return metadata.get(name);
    }

    public Object execute(String name, Map<String, Object> args) {
        Function<Map<String, Object>, Object> handler = tools.get(name);
        if (handler == null) {
            throw new IllegalArgumentException("Tool not found: " + name);
        }
        return handler.apply(args);
    }

    public List<Map<String, Object>> getToolsMetadata() {
        return metadata.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> toolInfo = new HashMap<>();
                    toolInfo.put("name", entry.getKey());
                    toolInfo.put("description", entry.getValue().description);
                    toolInfo.put("inputSchema", entry.getValue().inputSchema);
                    return toolInfo;
                })
                .collect(Collectors.toList());
    }

    public static class ToolMetadata {
        public final String description;
        public final Map<String, Object> inputSchema;

        public ToolMetadata(String description, Map<String, Object> inputSchema) {
            this.description = description;
            this.inputSchema = inputSchema;
        }
    }
}
