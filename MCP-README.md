# E-commerce MCP Server

A Model Context Protocol (MCP) server implementation in Java using Spring Boot for e-commerce operations.

## Architecture

- **McpServer**: Reads JSON-RPC requests from stdin and writes responses to stdout
- **McpRequestHandler**: Processes MCP protocol methods (initialize, tools/list, tools/call)
- **ToolRegistry**: Manages tool registration with metadata (descriptions and JSON schemas)
- **ProductTools**: Exposes product operations (createProduct, getProduct)
- **OrderTools**: Exposes order operations (createOrder, addItem, checkout)

## Available Tools

### Product Tools
- `createProduct`: Create a new product with id, name, price, and stock
- `getProduct`: Retrieve product details by ID

### Order Tools
- `createOrder`: Create a new order
- `addItem`: Add product to an order
- `checkout`: Calculate order total

## Running the Server (stdio)

```bash
mvn clean package -DskipTests
java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
```

## Running the Server (HTTP)

Disable stdio and expose HTTP JSON-RPC:

```bash
MCP_STDIO_ENABLED=false java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
```

Send JSON-RPC via HTTP:

```bash
curl -sS -X POST http://localhost:8080/mcp \
  -H 'Content-Type: application/json' \
  -d '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{}}'
```

## Testing

Send JSON-RPC requests via stdin:

```bash
# Initialize
echo '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{}}' | java -jar target/ecommerce-0.0.1-SNAPSHOT.jar

# List tools
echo '{"jsonrpc":"2.0","id":2,"method":"tools/list"}' | java -jar target/ecommerce-0.0.1-SNAPSHOT.jar

# Create product
echo '{"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"createProduct","arguments":{"productId":"p1","name":"Laptop","price":999.99,"stock":10}}}' | java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
```

## MCP Protocol Compliance

- Protocol version: 2024-11-05
- Implements: initialize, tools/list, tools/call
- Error handling with JSON-RPC error codes
- Tool metadata with descriptions and JSON schemas
