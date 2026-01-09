# Book Management System

A full-stack book management application built with **Spring Boot**, **Angular**, **PostgreSQL**, and **Docker**.

This project was developed as a portfolio challenge to demonstrate:
- Clean backend architecture
- Modern Angular frontend
- Dockerized full-stack setup
- Environment-based configuration

---

## Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL

### Frontend
- Angular
- Bootstrap
- SCSS

### Infrastructure
- Docker
- Docker Compose
- Nginx

---

## Features

- User authentication (JWT)
- Role-based authorization
- Book CRUD operations
- User management
- Dark / Light theme
- Responsive UI
- Dockerized backend, frontend and database

---

## Running with Docker (Recommended)

### Clone the repository

```bash
git clone https://github.com/pedroheringerr/book-management.git
cd book-management
```

---

### Create environment file

```bash
cp .env.example .env
```

Edit the `.env` file and set your values.

---

### Build and run the project

```bash
docker compose up --build
```

---

### Access the application
* Frontend: <http://localhost:4200>
* Backend: <http://localhost:8080>

---

## Project Structure

```text
.
├── Backend
├── Frontend
├── docker-compose.yml
├── .env.example
└── README.md
```
