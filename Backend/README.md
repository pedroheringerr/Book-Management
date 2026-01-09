# 📚 Book Management API

A RESTful backend application for managing books and users, built with **Spring Boot**, **Spring Security**, **JWT authentication**, and **PostgreSQL**.

The system supports **role-based access control**, secure authentication, pagination, validation, and clean layered architecture.

---

## Features

### Authentication & Security
- JWT-based authentication
- Stateless session management
- Role-based authorization (`USER`, `ADMIN`)
- Secure password hashing
- Custom authentication filter

### User Management
- User registration & login
- Role assignment (`ADMIN` only)
- List users with pagination
- Delete users (`ADMIN` only)

### Book Management
- Create, update, delete books (authenticated users)
- Public read access (GET)
- Filter books by:
  - Genre
  - Author
  - Year published
- Pagination support
- Input validation (ISBN, year range, etc.)

### Architecture
- Layered architecture:
  - Controller
  - Service
  - Repository
  - Model
  - DTO
- Global exception handling
- DTO-based API responses

---

## Tech Stack

- **Java 21**
- **Spring Boot**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **Spring Data JPA**
- **PostgreSQL**
- **Hibernate**
- **Maven**
- **Jakarta Validation**

---

## Project Structure

com.pedro.bookManagement
├── config # Security & application configuration
├── controller # REST controllers
├── dto # Request & response DTOs
├── exception # Global & custom exceptions
├── model # JPA entities
├── repo # Spring Data repositories
├── security # JWT & security filters
└── service # Business logic

---

## 🔑 Authentication Flow (JWT)

1. User logs in via `/api/auth/signin`
2. Server returns a **JWT token**
3. Token must be sent on protected requests:
    Authorization: Bearer <JWT_TOKEN>
4. `AuthTokenFilter`:
   - Extracts token
   - Validates token
   - Loads user details
   - Sets authentication in `SecurityContext`

---

## Roles & Authorization

### Roles
- `USER`
- `ADMIN`

### Role Mapping
Stored using a **Many-to-Many** relationship:
    users ↔ user_role ↔ roles


### Access Rules

| Endpoint | Access |
|--------|--------|
| `/api/auth/**` | Public |
| `GET /api/books/**` | Public |
| `POST /api/books` | USER |
| `PUT /api/books/**` | USER |
| `DELETE /api/books/**` | USER |
| `/api/user/**` | ADMIN |

---

## API Endpoints

### Authentication

#### Login

POST /api/auth/signin
EX:
    {
        "email": "admin@example.com",
        "password": "password123"
    }
RESPONSE:
JWT_TOKEN_STRING

#### Register

POST /api/auth/signup

EX:
    {
        "email": "admin@example.com",
        "password": "password123",
        "firstName": "Admin",
        "lastName": "Nimda",
        "roleId": 2
    }

### Books

#### Get Books with filters

GET
/api/books?author=John&page=0&size=5

#### Create Books(USER Only)

POST /api/books
Authorization: Bearer <TOKEN>
EX:
    {
      "isbn": "9781234567890",
      "title": "Clean Architecture",
      "author": "Robert C. Martin",
      "yearPublished": 2017,
      "genre": "Programming"
    }

#### Update Books(USER Only)

PUT /api/books/{id}
EX:
    {
      "isbn": "9781234567890",
      "title": "Clean Architecture",
      "author": "Robert C. Martin",
      "yearPublished": 2017,
      "genre": "Programming"
    }

#### Delete Books(USER Only)

DELETE /api/books/{id} 

### Users(ADMIN only)

#### List Users

GET
/api/user?page=0&size=5

#### Update User Roles

PUT /api/user/{id}
EX:
    {
      "roles": ["ADMIN", "USER"]
    }

#### Delete User

DELETE /api/user/{id}

---

## Configuration

### application.properties

spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=${JWT_SECRET}
jwt.expiration=3600000

---

## Validation and Error Handling

* Input validation using Jakarta Validation
* Centralized exception handling with `GlobalExceptionHandler`
* Custom Exceptions:
    * ResourceNotFoundException
    * DuplicateResourceException

---

## Notes

For more security the Api rellies on a pre existing user with both ADMIN and USER role,
so it can create the other users and give roles.

---

## Running The Application

### Prerequisites

* Java 21+
* PostgreSQL
* Maven

### Steps

#### Create the Data Base and a User

And change the ${...} with it's real values in the application.properties

spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

#### Generate a JWT Secret HS256

Then change the ${JWT_SECRET} with it



#### Running the Application

```
mvn clean install
mvn spring-boot:run
```

Application will be running on:
`http://localhost:8080`

---

## Author
Pedro Heringer

