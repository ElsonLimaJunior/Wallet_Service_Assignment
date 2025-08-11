# Wallet Service

## 1. Overview

Wallet Service is a mission-critical microservice designed to manage user funds. It provides a robust, secure, and auditable platform for creating wallets, handling deposits, withdrawals, and transfers between users.

The entire system is built following **Clean Architecture** principles to ensure high maintainability, testability, and technology independence. A key feature of this service is its **immutable ledger-based data model**, which guarantees full traceability of all operations and allows for querying a wallet's balance at any specific point in the past.

For a detailed explanation of the architectural decisions and patterns used, please refer to the [DESIGN_CHOICES.md](./DESIGN_CHOICES.md) file.

## 2. Technology Stack

* **Language:** Java 21 (LTS)
* **Framework:** Spring Boot 3.3.3
* **Data Persistence:** Spring Data JPA / Hibernate
* **Database:** PostgreSQL
* **Security:** Spring Security 6 / JSON Web Tokens (JWT)
* **Testing:** JUnit 5, Mockito, AssertJ
* **High-Fidelity Integration Testing:** Testcontainers
* **Containerization:** Docker & Docker Compose
* **Build Tool:** Apache Maven

## 3. Prerequisites

To run or develop this project, you will need:

* JDK 21 or higher
* Apache Maven 3.8+
* Docker and Docker Compose (for the production-like environment)

## 4. How to Run the Service

You have two main options to run the service.

### Option 1: Simple Run (In-Memory H2 Database)

This method is ideal for quick development and testing of specific functionalities without needing Docker. It uses an in-memory H2 database that is reset every time the application stops.

1.  **Clone the repository:**
    ```bash
    git clone <your-repo-url>
    cd wallet-service
    ```

2.  **Run the application using the Maven wrapper:**
    ```bash
    ./mvnw spring-boot:run
    ```

The service will start and be available at `http://localhost:8080`.

### Option 2: Production-like Environment (Docker & PostgreSQL)

This is the **recommended** way to run the service as it mirrors a real production setup, using a dedicated PostgreSQL database.

1.  **Ensure Docker Desktop is running.**

2.  **Create a `docker-compose.yml` file** in the root of the project with the following content:

    ```yaml
    version: '3.8'
    services:
      db:
        image: postgres:16-alpine
        container_name: wallet-db
        environment:
          - POSTGRES_USER=admin
          - POSTGRES_PASSWORD=admin
          - POSTGRES_DB=walletdb
        ports:
          - "5432:5432"
        volumes:
          - postgres_data:/var/lib/postgresql/data

      app:
        image: wallet-service:latest # This image will be built in the next step
        container_name: wallet-app
        depends_on:
          - db
        ports:
          - "8080:8080"
        environment:
          - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/walletdb
          - SPRING_DATASOURCE_USERNAME=admin
          - SPRING_DATASOURCE_PASSWORD=admin
          - SPRING_JPA_HIBERNATE_DDL_AUTO=update
    
    volumes:
      postgres_data:
    ```

3.  **Build the application's Docker image** using the built-in Spring Boot plugin. This command creates a highly optimized container image.
    ```bash
    ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=wallet-service
    ```

4.  **Start all services** using Docker Compose in detached mode.
    ```bash
    docker-compose up -d
    ```

The service will be available at `http://localhost:8080` and will be connected to the PostgreSQL database running inside another container.

* To view the logs: `docker-compose logs -f`
* To stop all services: `docker-compose down`

## 5. How to Run Tests

The project includes a comprehensive test suite with both unit and integration tests.

The integration tests are configured with **Testcontainers**. This means they automatically start a dedicated, ephemeral PostgreSQL database inside a Docker container just for the test run. You **do not need** to have a separate database installed or configured on your machine to run the tests.

1.  **Execute all tests** using the Maven wrapper. The `verify` command ensures both unit and integration tests are run.
    ```bash
    ./mvnw clean verify
    ```

A test report will be generated in the `target/surefire-reports` directory.

## 6. API Endpoints Overview

All endpoints under `/api/wallets/` are protected and require a `Bearer <JWT>` token in the `Authorization` header.

| Method | Endpoint                                         | Description                                    | Protected |
| :----- | :----------------------------------------------- | :--------------------------------------------- | :-------: |
| `POST` | `/api/auth/register`                             | Creates a new user and their associated wallet.|    No     |
| `POST` | `/api/auth/login`                                | Authenticates a user and returns a JWT.        |    No     |
| `GET`  | `/api/wallets/me/balance`                        | Retrieves the current balance of your wallet.  |    Yes    |
| `GET`  | `/api/wallets/me/balance/historical?at_time=...` | Retrieves balance at a specific ISO timestamp. |    Yes    |
| `POST` | `/api/wallets/me/deposit`                        | Deposits funds into your wallet.               |    Yes    |
| `POST` | `/api/wallets/me/withdraw`                       | Withdraws funds from your wallet.              |    Yes    |
| `POST` | `/api/wallets/me/transfer`                       | Transfers funds from your wallet to another.   |    Yes    |
