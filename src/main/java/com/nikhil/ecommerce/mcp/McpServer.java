package com.nikhil.ecommerce.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class McpServer {
    private final ToolRegistry toolRegistry;
    private final ObjectMapper objectMapper;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public McpServer(ToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
        this.objectMapper = new ObjectMapper();
        this.reader = new BufferedReader(new InputStreamReader(System.in, java.nio.charset.StandardCharsets.UTF_8));
        this.writer = new PrintWriter(new OutputStreamWriter(System.out, java.nio.charset.StandardCharsets.UTF_8),
                true);
    }

    public void start() {
        try {
            System.err.println("MCP Server starting...");
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    System.err.println("Received message: " + line);
                    processMessage(line);
                }
            }
            System.err.println("MCP Server: Input stream closed");
        } catch (IOException e) {
            System.err.println("MCP Server IO error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("MCP Server unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processMessage(String message) {
        try {
            System.err.println("Processing message: " + message);
            Map<String, Object> request = objectMapper.readValue(message, Map.class);
            Map<String, Object> response = handleRequest(request);
            if (response != null) {
                String responseJson = objectMapper.writeValueAsString(response);
                System.err.println("Sending response: " + responseJson);
                writer.println(responseJson);
                writer.flush();
            } else {
                System.err.println("No response needed (notification)");
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
            sendError(null, "Invalid request: " + e.getMessage());
        }
    }

    private Map<String, Object> handleRequest(Map<String, Object> request) {
        String method = (String) request.get("method");
        Object id = request.get("id");
        Map<String, Object> params = (Map<String, Object>) request.get("params");

        switch (method) {
            case "initialize":
                return createResponse(id, createInitializeResult());
            case "initialized":
                // Notification - no response needed
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

    private Map<String, Object> createErrorResponse(Object id, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", -1);
        error.put("message", message);

        Map<String, Object> response = new HashMap<>();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        response.put("error", error);
        return response;
    }

    private void sendError(Object id, String message) {
        try {
            writer.println(objectMapper.writeValueAsString(createErrorResponse(id, message)));
        } catch (Exception e) {
            System.err.println("Failed to send error: " + e.getMessage());
        }
    }
}