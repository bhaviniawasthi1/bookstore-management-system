# <img src="https://img.icons8.com/?size=30&id=12458&format=png"/> LeafLore

A complete web-based bookstore management application built with **Spring Boot**, **MySQL**, and **Thymeleaf**. LeafLore manages books, customers, orders, and payments with a clean, layered architecture and a responsive Bootstrap frontend.

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
- Cart persistence per user

### Payment Simulation
- Multiple payment methods: UPI, Credit Card, Debit Card, Cash on Delivery
- Simulated success/failure responses
- Unique transaction ID generation
- Order confirmation on successful payment

### Authentication & Authorization
- User registration with BCrypt password encryption
- Login / Logout functionality
- Two roles: **Admin** and **Customer**
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
| MySQL             | 8.0+        |
| Thymeleaf         | 3           |
| Bootstrap         | 5.3         |
| JavaScript        | (Vanilla)   |
| Maven             | 3.8+        |
| Lombok            | (Optional)  |

---

## Project Structure

```
bookstore-management-system/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/bookstore/
        │       ├── BookstoreApplication.java          # Entry point
        │       ├── config/
        │       │   ├── DataInitializer.java           # Seeds admin & sample data
        │       │   └── SecurityConfig.java            # Spring Security config
        │       ├── controller/
        │       │   ├── AdminController.java            # Admin panel routes
        │       │   ├── AuthController.java             # Login/Register routes
        │       │   ├── BookController.java             # Book listing & detail
        │       │   ├── CartController.java             # Cart operations
        │       │   ├── CheckoutController.java         # Checkout & payment flow
        │       │   ├── HomeController.java             # Home page
        │       │   ├── OrderController.java            # Order history & detail
        │       │   └── ProfileController.java          # User profile
        │       ├── dto/
        │       │   ├── BookDto.java
        │       │   ├── CartItemDto.java
        │       │   ├── OrderDto.java
        │       │   └── UserRegistrationDto.java
        │       ├── entity/
        │       │   ├── Book.java
        │       │   ├── Cart.java
        │       │   ├── CartItem.java
        │       │   ├── Order.java
        │       │   ├── OrderItem.java
        │       │   ├── OrderStatus.java               # Enum
        │       │   ├── Payment.java
        │       │   ├── PaymentMethod.java             # Enum
        │       │   ├── PaymentStatus.java             # Enum
        │       │   ├── Role.java                      # Enum
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
            │   └── js/main.js
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
                ├── profile.html
                └── register.html
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
- **MySQL 8.0+** — [Download](https://dev.mysql.com/downloads/installer/)
- **Maven 3.8+** — Included with IntelliJ IDEA or [Download](https://maven.apache.org/download.cgi)

### Step-by-Step Setup

#### 1. Install MySQL

- Download and run the MySQL Installer
- During setup, select **MySQL Server** and note the **root password** you set
- MySQL will run as a background service automatically

#### 2. Verify MySQL is Running

Open **Command Prompt** and run:

```bash
mysql -u root -p
```

Enter your root password. If you see the `mysql>` prompt, it's working. Type `exit` to quit.

> **Note:** If `mysql` is not recognized, add `C:\Program Files\MySQL\MySQL Server 8.0\bin` to your system PATH.

#### 3. Configure Database Credentials

Open `src/main/resources/application.properties` and change the password:

```properties
spring.datasource.password=your_mysql_password
```

#### 4. Build and Run

Open a terminal in the project directory and run:

```bash
mvn spring-boot:run
```

Wait until you see:
```
Started BookstoreApplication in X seconds
```

#### 5. Open the Application

Go to **http://localhost:8080** in your browser.

> The database `bookstore_db`, all tables, and sample data are created **automatically** on first run.

### Default Accounts

| Role     | Email               | Password      |
|----------|---------------------|---------------|
| Admin    | admin@bookstore.com | admin123      |
| Customer | john@example.com    | password123   |

---

## API Endpoints

### Public
| Method | URL              | Description              |
|--------|------------------|--------------------------|
| GET    | `/`              | Home page                |
| GET    | `/books`         | Browse books (with search)|
| GET    | `/books/{id}`    | Book details             |
| GET    | `/login`         | Login page               |
| GET    | `/register`      | Registration page        |
| POST   | `/register`      | Register new user        |

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

## Optional Features

- **Auto-seeded data** — Admin, sample customer, and 10 books inserted on first run
- **Order status management** — Full lifecycle: PENDING → CONFIRMED → SHIPPED → DELIVERED → CANCELLED
- **Stock management** — Auto-decremented on order; insufficient stock prevents ordering
- **Responsive UI** — Bootstrap 5.3 with auto-dismiss alerts, breadcrumbs, and loading spinners
- **Global exception handling** — `@ControllerAdvice` for validations, not-found, and generic errors
- **Payment transaction IDs** — Unique simulated IDs (e.g., `TXN8A3F9C2E1B0D`)
