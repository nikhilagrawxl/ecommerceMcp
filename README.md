

# 📦 Ecommerce MCP Server (Java + Spring Boot + PostgreSQL)

A basic **E-commerce backend system** built using **Java + Spring Boot + JPA + PostgreSQL**, exposed as an **MCP (Model Context Protocol) Server** that can be connected directly with **Claude AI**.

This project allows Claude to act as:

- 🛒 Buyer (create orders, add items, checkout)
- 🏪 Seller (create products, view inventory)
- 👤 User Manager (create buyers/sellers)

---

## 🚀 Features Implemented

### ✅ User Management
- Create new users
- Supports two roles:
  - `BUYER`
  - `SELLER`

### ✅ Product Catalog (Seller Side)
- Seller can create products
- Seller can view own inventory
- Seller can delete only their own products

### ✅ Order System (Buyer Side)
- Buyer can create orders
- Add products to orders
- Checkout orders
- View buyer order history

### ✅ PostgreSQL Database Integration
- Persistent storage using Spring Data JPA
- Entity relationships:
  - User → Products
  - User → Orders
  - Order → OrderItems

### ✅ Claude AI Integration via MCP Tools
Claude can directly call tools like:

- `createUser`
- `createProduct`
- `deleteProduct`
- `getAllProducts`
- `getMyInventory`
- `createOrder`
- `addItem`
- `checkout`
- `getMyOrders`

---

## 🛠 Tech Stack

| Layer        | Technology                   |
|-------------|------------------------------|
| Language     | Java 8                       |
| Framework    | Spring Boot                  |
| ORM          | Spring Data JPA (Hibernate)  |
| Database     | PostgreSQL                   |
| AI Protocol  | MCP (Model Context Protocol) |
| Client       | Claude Desktop               |

---

## 📂 Project Structure

```
src/main/java/com/nikhil/ecommerce
│
├── model/         → Entities (User, Product, Order, OrderItem)
├── repository/    → JPA Repositories
├── service/       → Business Logic + Validations
├── dto/           → Request/Response DTOs
└── mcp/tools/     → MCP Tool Definitions for Claude
```

---

# ⚙️ Setup Instructions

## 1️⃣ Clone the Repository

```bash
git clone https://github.com/nikhilagrawxl/ecommerceMcp.git
cd ecommerceMcp
```

---

## 2️⃣ Setup PostgreSQL Database

Login into PostgreSQL:

```bash
psql -U postgres
```

Create database:

```sql
CREATE DATABASE ecommerce_db;
```

---

## 3️⃣ Configure Database in Spring Boot

Update your `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 4️⃣ Build the Project

```bash
mvn clean package
```

Jar file will be created at:

```
target/ecommerce-0.0.1-SNAPSHOT.jar
```

---

## 5️⃣ Run MCP Server Locally

Start the MCP server:

```bash
java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
```

Expected output:

```
MCP Server starting...
Server started successfully...
```

---

# 🤖 Connect with Claude Desktop

## 1️⃣ Install Claude Desktop

Download Claude Desktop from:

https://claude.ai/download

---

## 2️⃣ Configure MCP Server in Claude

Open Claude settings file:

```
~/Library/Application Support/Claude/claude_desktop_config.json
```

Add:

```json
{
  "mcpServers": {
    "ecommerce": {
      "command": "/usr/bin/java",
      "args": [
        "-jar",
        "/Users/<username>/Downloads/ecommerce/target/ecommerce-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

⚠️ Replace the jar path with your system path.

---

## 3️⃣ Restart Claude Desktop

After restart, Claude will automatically detect your tools.

---

# ✅ Example Usage in Claude

### Create Seller

Tool: `createUser`

```json
{
  "name": "Nikhil",
  "userType": "SELLER"
}
```

---

### Add Product

Tool: `createProduct`

```json
{
  "sellerId": "1",
  "name": "Pen",
  "price": 2,
  "stock": 20
}
```

---

### Create Buyer

Tool: `createUser`

```json
{
  "name": "Nikki",
  "userType": "BUYER"
}
```

---

### Create Order

Tool: `createOrder`

```json
{
  "userId": "2"
}
```

---

### Add Items to Order

Tool: `addItem`

```json
{
  "orderId": "1",
  "productId": "2",
  "quantity": 5
}
```

---

### Checkout Order

Tool: `checkout`

```json
{
  "orderId": "1"
}
```

---

### View Buyer Orders

Tool: `getMyOrders`

```json
{
  "buyerId": "2"
}
```

---

## 📌 Future Improvements

- Update stock + price tools
- Cart system
- JWT Authentication
- Docker deployment
- Payment simulation

---

## 👨‍💻 Author

**Nikhil Agrawal**  
Java Backend Developer | Spring Boot | PostgreSQL | MCP + AI Integration

---

⭐ If you like this project, give it a star on GitHub!