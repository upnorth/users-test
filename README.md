# User Management REST API & Vue 3 Frontend

A lightweight Java/Quarkus REST API with in-memory storage, OpenAPI compatibility, health endpoints, Docker support, and a Vue 3 frontend with full CRUD operations.

## Features

- **REST API**:
  - `GET /digg/user`: Lists all users in the system.
  - `GET /digg/user/{id}`: Retrieves a user by ID.
  - `POST /digg/user`: Creates a new user with validation.
  - `PUT /digg/user/{id}`: Updates an existing user.
  - `DELETE /digg/user/{id}`: Deletes a user.
- **User Model & Constraints**:
  - `id` (String): Unique identifier (e.g., `usr-1001`).
  - `name` (String): Required, 2-100 characters.
  - `address` (String): Required, 3-255 characters.
  - `email` (String): Required, valid email format (e.g. `user@example.com`).
  - `telephone` (String): Required, valid phone number format (6-25 characters).
- **In-Memory Storage**: Thread-safe storage with seeded sample data for instant exploration.
- **Health Checks**:
  - `GET /health`: Direct health status JSON endpoint.
  - `GET /q/health`: SmallRye Health endpoint with liveness and readiness probes.
- **OpenAPI & Swagger UI**:
  - `GET /q/openapi`: OpenAPI 3.0 specification.
  - `GET /q/swagger-ui`: Interactive Swagger UI documentation.
- **Vue 3 Frontend**:
  - Served directly from `http://localhost:8080/`.
  - Responsive, modern UI with search/filter, user card/table view, create/edit modal dialogs, and deletion confirmations.

## Getting Started

### Prerequisites
- Java 17+
- Docker (optional)

### Running Locally with Maven
```bash
# Run in development mode with live reload
./mvnw quarkus:dev

# Run tests
./mvnw test

# Package fast-jar
./mvnw package

# Run packaged application
java -jar target/quarkus-app/quarkus-run.jar
```

Access the frontend at `http://localhost:8080/` and Swagger UI at `http://localhost:8080/q/swagger-ui`.

### Running with Docker

#### Using Multi-Stage Dockerfile:
```bash
docker build -t users-api .
docker run -p 8080:8080 users-api
```

#### Using Docker Compose:
```bash
docker compose up --build
```
