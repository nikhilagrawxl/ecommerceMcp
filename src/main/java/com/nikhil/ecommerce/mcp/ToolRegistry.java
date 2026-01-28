package com.nikhil.ecommerce.mcp;

import org.springframework.stereotype.Component;

import com.nikhil.ecommerce.dto.ErrorResponseDTO;

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
            return new ErrorResponseDTO("Tool not found: " + name);
        }

        try {
            return handler.apply(args);
        } catch (Exception ex) {
            // Return clean error message to Claude instead of stack trace
            return new ErrorResponseDTO(ex.getMessage());
        }
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
