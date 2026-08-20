# <img src="https://img.icons8.com/?size=30&id=12458&format=png"/> LeafLore

A full-stack bookstore management application built with **Spring Boot**, **H2 (in-memory)**, and **Thymeleaf**. LeafLore manages books, carts, orders, and payments end to end with a clean, layered architecture and a custom Bootstrap-based frontend.

**🔗 Live demo: [https://leaf-lore.onrender.com/](https://leaf-lore.onrender.com/)**

> **This is a personal portfolio project — not a place to store real data.** There's no self-registration on purpose: the login page lists two shared demo accounts (one admin, one customer) that anyone can use, no sign-up needed. The database itself lives entirely in memory, so every time the app restarts — including Render's free-tier sleep/wake cycle — it comes back with a completely clean slate: the two demo accounts and the starter book catalog are reseeded automatically, and anything a visitor added (cart items, orders, edited books) is gone. Explore freely — nothing you do here sticks around, and nothing you do affects anyone else's session in a way that outlives a restart.

**Developed by Bhavini Awasthi**

---

## Features

### Product Catalog
- Browse books with search by title or author
- View detailed book information: title, author, category, ISBN, price, stock, and description
- Categorized book listing

### Shopping Cart
- Add books to cart with quantity selection
- Update item quantities or remove items
- Real-time total amount calculation
- Cart persistence per user (for the life of the current in-memory database)

### Payment Simulation
- Multiple payment methods: UPI, Credit Card, Debit Card, Cash on Delivery
- Simulated success/failure responses
- Unique transaction ID generation
- Order confirmation on successful payment

### Authentication & Authorization
- Two shared demo accounts (Admin / Customer) — no self-registration
- BCrypt password encryption
- Login / Logout functionality
- Role-based access control on pages and actions

### Order Management
- Place orders from cart items
- View order history with full details
- Track payment and order status
- Admin can update order status (Pending → Confirmed → Shipped → Delivered → Cancelled)

### Admin Dashboard
- Overview of total books, orders, and customers
- Full CRUD operations for books
- View all registered customers
- Manage all orders and update their status

---

## Technology Stack

| Technology        | Version     |
|-------------------|-------------|
| Java              | 21          |
| Spring Boot       | 3.2.1       |
| Spring MVC        | 6           |
| Spring Data JPA   | (Hibernate) |
| Spring Security   | 6           |
| H2 (in-memory)    | latest      |
| Thymeleaf         | 3           |
| Bootstrap         | 5.3         |
| JavaScript        | (Vanilla)   |
| Maven             | 3.8+        |
| Lombok            | (Optional)  |

---

## Project Structure

```
bookstore-management-system/
├── Dockerfile                                          # Multi-stage build for deployment
├── render.yaml                                         # Render Blueprint (web service config)
├── .gitignore
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/bookstore/
        │       ├── BookstoreApplication.java           # Entry point
        │       ├── config/
        │       │   ├── DataInitializer.java            # Seeds demo accounts & sample data
        │       │   ├── DemoAccounts.java               # The fixed admin/student roster
        │       │   └── SecurityConfig.java             # Spring Security config
        │       ├── controller/
        │       │   ├── AdminController.java            # Admin panel routes
        │       │   ├── AuthController.java             # Login route
        │       │   ├── BookController.java             # Book listing & detail
        │       │   ├── CartController.java             # Cart operations
        │       │   ├── CheckoutController.java         # Checkout & payment flow
        │       │   ├── HomeController.java             # Home page
        │       │   ├── OrderController.java            # Order history & detail
        │       │   └── ProfileController.java          # User profile
        │       ├── dto/
        │       │   ├── BookDto.java
        │       │   ├── CartItemDto.java
        │       │   └── OrderDto.java
        │       ├── entity/
        │       │   ├── Book.java
        │       │   ├── Cart.java
        │       │   ├── CartItem.java
        │       │   ├── Order.java
        │       │   ├── OrderItem.java
        │       │   ├── OrderStatus.java                # Enum
        │       │   ├── Payment.java
        │       │   ├── PaymentMethod.java              # Enum
        │       │   ├── PaymentStatus.java              # Enum
        │       │   ├── Role.java                       # Enum
        │       │   └── User.java
        │       ├── exception/
        │       │   ├── BadRequestException.java
        │       │   ├── GlobalExceptionHandler.java
        │       │   └── ResourceNotFoundException.java
        │       ├── repository/
        │       │   ├── BookRepository.java
        │       │   ├── CartItemRepository.java
        │       │   ├── CartRepository.java
        │       │   ├── OrderItemRepository.java
        │       │   ├── OrderRepository.java
        │       │   ├── PaymentRepository.java
        │       │   └── UserRepository.java
        │       ├── security/
        │       │   └── CustomUserDetailsService.java
        │       └── service/
        │           ├── BookService.java
        │           ├── CartService.java
        │           ├── OrderService.java
        │           ├── PaymentService.java
        │           └── UserService.java
        └── resources/
            ├── application.properties
            ├── static/
            │   ├── css/style.css
            │   ├── js/main.js
            │   ├── favicon.ico
            │   ├── favicon.svg
            │   └── apple-touch-icon.png
            └── templates/
                ├── admin/
                │   ├── book-form.html
                │   ├── books.html
                │   ├── customers.html
                │   ├── dashboard.html
                │   └── orders.html
                ├── fragments/common.html               # Navbar, footer, alerts
                ├── book-detail.html
                ├── books.html
                ├── cart.html
                ├── checkout.html
                ├── error.html
                ├── home.html
                ├── login.html
                ├── order-detail.html
                ├── orders.html
                └── profile.html
```

---

## Database Tables

### `users`
| Column     | Type                          | Constraints                |
|------------|-------------------------------|----------------------------|
| id         | BIGINT                        | PK, AUTO_INCREMENT         |
| name       | VARCHAR                       | NOT NULL                   |
| email      | VARCHAR                       | NOT NULL, UNIQUE           |
| password   | VARCHAR                       | NOT NULL                   |
| role       | ENUM (ADMIN, CUSTOMER)        | NOT NULL                   |
| address    | VARCHAR                       | NULLABLE                   |
| phone      | VARCHAR                       | NULLABLE                   |
| created_at | DATETIME                      |                            |
| updated_at | DATETIME                      |                            |

### `books`
| Column      | Type                | Constraints          |
|-------------|---------------------|----------------------|
| id          | BIGINT              | PK, AUTO_INCREMENT   |
| title       | VARCHAR             | NOT NULL             |
| author      | VARCHAR             | NOT NULL             |
| category    | VARCHAR             | NOT NULL             |
| isbn        | VARCHAR             | NOT NULL, UNIQUE     |
| price       | DECIMAL(10,2)       | NOT NULL             |
| stock       | INT                 | NOT NULL             |
| description | TEXT                | NULLABLE             |
| image_url   | VARCHAR             | NULLABLE             |
| created_at  | DATETIME            |                      |
| updated_at  | DATETIME            |                      |

### `carts`
| Column     | Type   | Constraints                      |
|------------|--------|----------------------------------|
| id         | BIGINT | PK, AUTO_INCREMENT               |
| user_id    | BIGINT | FK → users.id, UNIQUE            |
| created_at | DATETIME|                                 |

### `cart_items`
| Column     | Type    | Constraints            |
|------------|---------|------------------------|
| id         | BIGINT  | PK, AUTO_INCREMENT     |
| cart_id    | BIGINT  | FK → carts.id          |
| book_id    | BIGINT  | FK → books.id          |
| quantity   | INT     | NOT NULL               |
| created_at | DATETIME|                        |

### `orders`
| Column           | Type                                         | Constraints      |
|------------------|----------------------------------------------|------------------|
| id               | BIGINT                                       |PK, AUTO_INCREMENT|
| user_id          | BIGINT                                       | FK → users.id    |
| order_date       | DATETIME                                     | NOT NULL         |
| total_amount     | DECIMAL(10,2)                                | NOT NULL         |
| status           | ENUM (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED) | NOT NULL |
| shipping_address | VARCHAR                                      | NULLABLE         |
| created_at       | DATETIME                                     |                  |
| updated_at       | DATETIME                                     |                  |

### `order_items`
| Column     | Type          | Constraints         |
|------------|---------------|---------------------|
| id         | BIGINT        | PK, AUTO_INCREMENT  |
| order_id   | BIGINT        | FK → orders.id      |
| book_id    | BIGINT        | FK → books.id       |
| quantity   | INT           | NOT NULL            |
| price      | DECIMAL(10,2) | NOT NULL            |
| created_at | DATETIME      |                     |

### `payments`
| Column         | Type                                 | Constraints          |
|----------------|--------------------------------------|----------------------|
| id             | BIGINT                               | PK, AUTO_INCREMENT   |
| order_id       | BIGINT                               | FK → orders.id, UNIQUE |
| amount         | DECIMAL(10,2)                        | NOT NULL             |
| payment_method | ENUM (UPI, CREDIT_CARD, DEBIT_CARD, COD) | NOT NULL         |
| status         | ENUM (SUCCESS, FAILED)               | NOT NULL             |
| transaction_id | VARCHAR                              | UNIQUE               |
| payment_date   | DATETIME                             |                      |
| created_at     | DATETIME                             |                      |

---

## How to Run

### Prerequisites

- **Java 21** (JDK) — [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.8+** — Included with IntelliJ IDEA or [Download](https://maven.apache.org/download.cgi)

That's it — no database to install. LeafLore runs on an in-memory H2 database, so it's fully self-contained.

### Step-by-Step Setup (local development)

#### 1. Build and Run

Open a terminal in the project directory and run:

```bash
mvn spring-boot:run
```

Wait until you see:
```
Started BookstoreApplication in X seconds
```

#### 2. Open the Application

Go to **http://localhost:8080** in your browser.

> All tables and sample data are created **automatically** on every startup, in memory. Restarting the app (locally or in production) wipes it and reseeds it fresh — there's nothing to install, migrate, or clean up.

#### Optional: inspect the database directly

Set `H2_CONSOLE=true` before running (`export H2_CONSOLE=true`, then `mvn spring-boot:run`) and open **http://localhost:8080/h2-console** in your browser. Use JDBC URL `jdbc:h2:mem:bookstore_db`, username `sa`, and an empty password. Leave this off (the default) for any public deployment.

### Demo Accounts

There's no registration form — the login page itself lists these two accounts with a **"Use this"** button that autofills the login form for you. Both are shared: anyone can log in with them at any time, no locking or exclusivity.

| Role     | Email                  | Password     |
|----------|-------------------------|--------------|
| Admin    | admin@leaflore.com      | admin123     |
| Customer | student@leaflore.com    | student123   |

These are reseeded on every restart, local or deployed — they exist to make the demo instantly explorable, not to protect anything sensitive, since nothing here persists anyway.

---

## API Endpoints

### Public
| Method | URL              | Description              |
|--------|------------------|--------------------------|
| GET    | `/`              | Home page                |
| GET    | `/books`         | Browse books (with search)|
| GET    | `/books/{id}`    | Book details             |
| GET    | `/login`         | Login page (lists demo accounts) |

### Authenticated (Customer)
| Method | URL                      | Description          |
|--------|--------------------------|----------------------|
| GET    | `/cart`                  | View cart            |
| POST   | `/cart/add/{bookId}`     | Add to cart          |
| POST   | `/cart/update/{itemId}`  | Update quantity      |
| POST   | `/cart/remove/{itemId}`  | Remove from cart     |
| GET    | `/checkout`              | Checkout page        |
| POST   | `/checkout/place`        | Place order          |
| GET    | `/orders`                | Order history        |
| GET    | `/orders/{id}`           | Order details        |
| GET    | `/profile`               | View profile         |
| POST   | `/profile/update`        | Update profile       |

### Admin Only
| Method | URL                              | Description            |
|--------|----------------------------------|------------------------|
| GET    | `/admin/dashboard`               | Admin dashboard        |
| GET    | `/admin/books`                   | Manage books           |
| GET    | `/admin/books/add`               | Add book form          |
| POST   | `/admin/books/save`              | Save new book          |
| GET    | `/admin/books/edit/{id}`         | Edit book form         |
| POST   | `/admin/books/update`            | Update book            |
| GET    | `/admin/books/delete/{id}`       | Delete book            |
| GET    | `/admin/customers`               | View customers         |
| GET    | `/admin/orders`                  | View all orders        |
| POST   | `/admin/orders/update-status`    | Update order status    |

---

## Notable Design Choices

- **Shared demo accounts, not self-registration.** As a public portfolio demo, open sign-up invites throwaway accounts and clutter. Two fixed accounts (admin + customer), listed and autofillable right on the login page, keep the experience simple for anyone trying it out.
- **In-memory H2 database.** The schema is rebuilt and reseeded from scratch on every application restart. Combined with Render's free-tier sleep/wake cycle, the live demo effectively resets itself with zero maintenance — nothing a visitor does ever persists beyond the current run.
- **Custom UI, not stock Bootstrap.** The frontend layers a warm, book-themed design system (Playfair Display + Inter typography, a forest-green/gold palette) on top of Bootstrap 5 via CSS variable overrides, so every page — including the admin panel — picks up the theme automatically.

## Optional Features

- **Auto-seeded data** — Admin, customer, and 10 books inserted fresh on every startup
- **Order status management** — Full lifecycle: PENDING → CONFIRMED → SHIPPED → DELIVERED → CANCELLED
- **Stock management** — Auto-decremented on order; insufficient stock prevents ordering
- **Responsive UI** — Custom Bootstrap 5.3 theme with auto-dismiss alerts, breadcrumbs, and loading spinners
- **Global exception handling** — `@ControllerAdvice` for validations, not-found, and generic errors
- **Payment transaction IDs** — Unique simulated IDs (e.g., `TXN8A3F9C2E1B0D`)

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

<div align="center">
  <p>Built by <strong>Bhavini Awasthi</strong></p>
  <p>
    <a href="https://www.linkedin.com/in/bhaviniawasthi/">LinkedIn</a>
  </p>
</div>
