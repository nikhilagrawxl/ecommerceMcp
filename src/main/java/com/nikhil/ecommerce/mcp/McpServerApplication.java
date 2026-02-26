package com.nikhil.ecommerce.mcp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.nikhil.ecommerce")
public class McpServerApplication implements CommandLineRunner {
    private final McpServer mcpServer;
    private final boolean stdioEnabled;

    public McpServerApplication(McpServer mcpServer,
                                @Value("${mcp.stdio.enabled:true}") boolean stdioEnabled) {
        this.mcpServer = mcpServer;
        this.stdioEnabled = stdioEnabled;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(McpServerApplication.class);
        app.setBannerMode(org.springframework.boot.Banner.Mode.OFF);
        app.setLogStartupInfo(false);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        try {
            if (!stdioEnabled) {
                System.err.println("MCP Server stdio transport disabled.");
                return;
            }
            mcpServer.start();
        } catch (Exception e) {
            System.err.println("MCP Server failed to start: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
