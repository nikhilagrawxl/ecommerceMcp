#!/bin/bash

# Build the project
mvn clean package -DskipTests -q

# Test MCP server
echo '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{}}' | java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
