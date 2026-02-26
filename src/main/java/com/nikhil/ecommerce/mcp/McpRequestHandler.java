package com.nikhil.ecommerce.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class McpRequestHandler {
    private final ToolRegistry toolRegistry;
    private final ObjectMapper objectMapper;

    public McpRequestHandler(ToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, Object> handleRequest(Map<String, Object> request) {
        String method = (String) request.get("method");
        Object id = request.get("id");
        Map<String, Object> params = (Map<String, Object>) request.get("params");

        if (method == null) {
            return createErrorResponse(id, "Missing method");
        }

        switch (method) {
            case "initialize":
                return createResponse(id, createInitializeResult());
            case "initialized":
                return null;
            case "tools/list":
                return createResponse(id, createToolsListResult());
            case "tools/call":
                return handleToolCall(id, params);
            default:
                return createErrorResponse(id, "Method not found: " + method);
        }
    }

    private Map<String, Object> createInitializeResult() {
        Map<String, Object> result = new HashMap<>();
        result.put("protocolVersion", "2024-11-05");
        Map<String, Object> capabilities = new HashMap<>();
        capabilities.put("tools", new HashMap<String, Object>() {
            {
                put("listChanged", true);
            }
        });
        result.put("capabilities", capabilities);
        result.put("serverInfo", new HashMap<String, Object>() {
            {
                put("name", "ecommerce-mcp-server");
                put("version", "1.0.0");
            }
        });
        return result;
    }

    private Map<String, Object> createToolsListResult() {
        return new HashMap<String, Object>() {
            {
                put("tools", toolRegistry.getToolsMetadata());
            }
        };
    }

    private Map<String, Object> handleToolCall(Object id, Map<String, Object> params) {
        try {
            if (params == null) {
                return createErrorResponse(id, "Missing params for tools/call");
            }

            String name = (String) params.get("name");
            Map<String, Object> arguments = (Map<String, Object>) params.get("arguments");
            Object result = toolRegistry.execute(name, arguments);

            Map<String, Object> content = new HashMap<>();
            content.put("type", "text");
            content.put("text", objectMapper.writeValueAsString(result));

            return createResponse(id, new HashMap<String, Object>() {
                {
                    put("content", new Object[] { content });
                }
            });
        } catch (Exception e) {
            return createErrorResponse(id, "Tool execution failed: " + e.getMessage());
        }
    }

    private Map<String, Object> createResponse(Object id, Object result) {
        Map<String, Object> response = new HashMap<>();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        response.put("result", result);
        return response;
    }

    public Map<String, Object> createErrorResponse(Object id, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", -1);
        error.put("message", message);

        Map<String, Object> response = new HashMap<>();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        response.put("error", error);
        return response;
    }
}
