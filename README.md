# User Management REST API & Vue 3 Frontend

A modern Java/Quarkus REST API with PostgreSQL persistence, OpenAPI compatibility, health endpoints, Docker support, and a responsive Vue 3 SPA frontend with full CRUD operations and client-side pagination.

## Features

- **REST API**:
  - `GET /digg/user`: Lists all users in the system sorted by name.
  - `GET /digg/user/{id}`: Retrieves a user by ID.
  - `POST /digg/user`: Creates a new user with Jakarta validation.
  - `PUT /digg/user/{id}`: Updates an existing user.
  - `DELETE /digg/user/{id}`: Deletes a user.
- **User Model & Constraints**:
  - `id` (String): Unique identifier (e.g., `usr-1001`).
  - `name` (String): Required, 2-100 characters.
  - `address` (String): Required, 3-255 characters.
  - `email` (String): Required, valid email format (e.g., `user@example.com`).
  - `telephone` (String): Required, valid phone number format (6-25 characters).
- **PostgreSQL Persistence & Dev Services**:
  - Backed by PostgreSQL using Hibernate ORM with Panache.
  - Quarkus Dev Services automatically provisions and manages an ephemeral PostgreSQL container during testing (`./mvnw test`) and local dev mode (`./mvnw quarkus:dev`).
  - Automatically seeded with 20 sample users on initial startup.
- **Health Checks & Observability**:
  - `GET /health`: Direct health status JSON endpoint returning service status and user count.
  - `GET /q/health`: SmallRye MicroProfile Health endpoint with liveness and readiness probes.
- **OpenAPI & Swagger UI**:
  - `GET /q/openapi`: OpenAPI 3.0 specification.
  - `GET /q/swagger-ui`: Interactive Swagger UI documentation.
- **Vue 3 SPA Frontend**:
  - Modular Single Page Application built with Vue 3 SFCs, Vite, and Tailwind CSS under `frontend/`.
  - Served directly from `http://localhost:8080/` when packaged.
  - Responsive UI with real-time search/filtering, client-side pagination (5, 10, 20 items per page), create/edit modal dialogs, deletion confirmations, and live API health status monitoring.

## Getting Started

### Prerequisites
- Java 21+
- Docker (required for Quarkus Dev Services during tests/dev mode and containerized execution)
- Node.js 20+ & npm (optional, only needed for standalone frontend development)

### Running Locally with Maven
```bash
# Run in development mode with live reload (auto-starts PostgreSQL Dev Services)
./mvnw quarkus:dev

# Run tests (auto-provisions PostgreSQL container via Testcontainers)
./mvnw test

# Package fast-jar and bundle frontend SPA
./mvnw package

# Run packaged application (requires accessible PostgreSQL or configured datasource)
java -jar target/quarkus-app/quarkus-run.jar
```

Access the frontend at `http://localhost:8080/` and Swagger UI at `http://localhost:8080/q/swagger-ui`.

### Standalone Frontend Development (Optional)
To develop the frontend independently with instant Hot Module Replacement (HMR):
```bash
cd frontend
npm install
npm run dev
```
The Vite dev server runs at `http://localhost:5173` and automatically proxies `/digg`, `/health`, and `/q` requests to the Quarkus backend on port `8080`.

### Running with Docker

#### Using Docker Compose (Recommended):
Starts both the PostgreSQL database and the Quarkus application:
```bash
docker compose up --build
```

#### Using Multi-Stage Dockerfile:
```bash
docker build -t users-api .
docker run -p 8080:8080 users-api
```
