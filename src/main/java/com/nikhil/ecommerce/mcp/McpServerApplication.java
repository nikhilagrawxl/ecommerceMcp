package com.nikhil.ecommerce.mcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.transport.WebFluxSseServerTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import com.nikhil.ecommerce.service.SearchService;

@SpringBootApplication
@ComponentScan(basePackages = "com.nikhil.ecommerce")
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }

    @Bean
    public WebFluxSseServerTransport webFluxSseServerTransport() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new WebFluxSseServerTransport(mapper, "/mcp/message");
    }

    @Bean
    public RouterFunction<?> mcpRouterFunction(WebFluxSseServerTransport transport) {
        return transport.getRouterFunction();
    }

    @Bean
    public ToolCallbackProvider searchToolCallbackProvider(SearchService searchService) {
        return MethodToolCallbackProvider.builder().toolObjects(searchService).build();
    }
}
