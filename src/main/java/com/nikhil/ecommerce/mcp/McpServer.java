package com.nikhil.ecommerce.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class McpServer {
    private final ObjectMapper objectMapper;
    private final McpRequestHandler requestHandler;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public McpServer(McpRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
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
            Map<String, Object> response = requestHandler.handleRequest(request);
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

    private void sendError(Object id, String message) {
        try {
            writer.println(objectMapper.writeValueAsString(requestHandler.createErrorResponse(id, message)));
        } catch (Exception e) {
            System.err.println("Failed to send error: " + e.getMessage());
        }
    }
}
