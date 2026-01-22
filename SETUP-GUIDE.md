# MCP Server Setup Guide

## ✅ Setup Complete!

Your ecommerce MCP server is ready to use with Claude Desktop.

## What's Configured

1. **MCP Server Built**: JAR file created at `target/ecommerce-0.0.1-SNAPSHOT.jar`
2. **Startup Script**: `run-mcp.sh` is executable and ready
3. **Claude Desktop**: Already configured in `~/Library/Application Support/Claude/claude_desktop_config.json`

## How to Use

### 1. Restart Claude Desktop
Close and reopen Claude Desktop to load the MCP server configuration.

### 2. Verify Connection
In Claude Desktop, you should see the ecommerce server connected. Look for an MCP icon or server indicator.

### 3. Available Tools
Once connected, Claude can use these tools:

- **createProduct**: Create a new product
  ```
  Create a product with id "laptop1", name "MacBook Pro", price 2499.99, stock 5
  ```

- **getProduct**: Get product details
  ```
  Get product with id "laptop1"
  ```

- **createOrder**: Create a new order
  ```
  Create an order with id "order1"
  ```

- **addItem**: Add items to an order
  ```
  Add product "laptop1" with quantity 2 to order "order1"
  ```

- **checkout**: Calculate order total
  ```
  Checkout order "order1"
  ```

## Testing Manually

Test the server from command line:
```bash
# List available tools
echo '{"jsonrpc":"2.0","id":1,"method":"tools/list"}' | ./run-mcp.sh

# Create a product
echo '{"jsonrpc":"2.0","id":2,"method":"tools/call","params":{"name":"createProduct","arguments":{"productId":"p1","name":"Laptop","price":999.99,"stock":10}}}' | ./run-mcp.sh
```

## Troubleshooting

If the server doesn't connect:
1. Check Claude Desktop logs: `~/Library/Logs/Claude/`
2. Test the script manually: `./run-mcp.sh`
3. Verify JAR exists: `ls -lh target/ecommerce-0.0.1-SNAPSHOT.jar`
4. Rebuild if needed: `mvn clean package -DskipTests`

## Rebuilding After Changes

```bash
mvn clean package -DskipTests
# Restart Claude Desktop
```
