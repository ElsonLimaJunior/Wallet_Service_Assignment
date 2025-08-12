# Wallet Service 

## About the project

This is my version of an wallet service. I did this as part of a assessment, the main goal was to make something close to what a production service would look like. 

There's two main non-negociable rules in this project
1.  **Full Traceability:** Being able to follow the trail of every single single transaction.
2.  **Historical Balance:** Being able to see the user's balance at any given moment.

Those rules guided the develepment of the whole application.

---

## About the architecture

* **The wallet must be very secure and trust worthy.** Every deposit, withdrawal, or transfer is a permanent and immutable record. I made the balance the sum of all of those records. This way everything is auditable and clean.

* **Clean Architecture** One of the main goals of the project like was to make something that could go to production, and for this I needed to follow the best practices in developing an API. Following this aproach I made the code easier to test and easier for future changes.

---

## About the tools 

* Java 21 with Spring Boot
* PostgreSQL as the database
* Spring Data JPA and Hibernate
* Spring Security with JWT 
* Docker and Docker Compose 
* JUnit 5, Mockito, AssertJ

---

## How to run the project

You'll need:
* Java 21 (JDK)
* Maven
* Docker (if you want to use Postgres)

**Option 1: In-Memory DB**

Use this option if you just want to make some calls and test de API. It uses an H2 in-memory database.

1.  Open your terminal.
2.  `cd wallet-service`
3.  And run it:
    ```bash
    ./mvnw spring-boot:run
    ```
   The API will be up at `http://localhost:8080`.

**Option 2: Docker with Postgres**

Use this option with you want to mimick a real environment.

1.  Make sure your Docker is running.
2.  The `docker-compose.yml` is already in the root. Just build the app's image:
    ```bash
    ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=wallet-service
    ```
3.  And to run everything together:
    ```bash
    docker-compose up -d
    ```

---

## Testing

A project that could go to production must have a very high covarage with tests.

* **Unit Tests:** Made with Mockito, just to make sure that the core rules of the application are working.
* **Integration Tests:** I used Testcontainers. This means that for the integration tests, the code automatically runs a real and temporary PostgreSQL database in a Docker container.

To run all the tests, just run:
```bash
./mvnw clean verify

## API Endpoints Overview

All endpoints under `/api/wallets/` are protected and require a `Bearer <JWT>` token in the `Authorization` header.

| Method | Endpoint                                         | Description                                    | Protected |
| :----- | :----------------------------------------------- | :--------------------------------------------- | :-------: |
| `POST` | `/api/auth/register`                             | Creates a new user and their associated wallet.|    No     |
| `POST` | `/api/auth/login`                                | Authenticates a user and returns a JWT.        |    No     |
| `GET`  | `/api/wallets/me/balance`                        | Retrieves the current balance of your wallet.  |    Yes    |
| `GET`  | `/api/wallets/me/balance/historical?at_time=...` | Retrieves balance at a specific timestamp. |    Yes    |
| `POST` | `/api/wallets/me/deposit`                        | Deposits funds into your wallet.               |    Yes    |
| `POST` | `/api/wallets/me/withdraw`                       | Withdraws funds from your wallet.              |    Yes    |
| `POST` | `/api/wallets/me/transfer`                       | Transfers funds from your wallet to another.   |    Yes    |
