package com.nikhil.ecommerce.mcp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mcp")
public class McpHttpController {
    private final McpRequestHandler requestHandler;

    public McpHttpController(McpRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @PostMapping
    public ResponseEntity<Object> handle(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = requestHandler.handleRequest(request);
        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
}
