# Enefit Home Assignment

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/) installed

## Additional Information
- A Postman collection is provided for testing the API endpoints.
- The database is seeded with two accounts:
- Username: john_doe, Password: password
- Username: jane_smith, Password: password
- The frontend has 1 test class AuthService.test.ts (Jest)
- The backend has also 1 test class ConsumptionServiceImplTest under the test directory


## Getting Started

## Docker compose variant

1. **Clone the repository**

2. **Build and start all services:**

    ```sh
    docker-compose up --build
    ```

   This will:
    - Build the backend (Spring Boot) and frontend (React) images
    - Start the backend, frontend, and Postgres database containers

3. **Access the application:**
    - Frontend: [http://localhost:3000](http://localhost:)
    - Backend API: [http://localhost:8080](http://localhost:8080)
    - Postgres: `localhost:5432`
        - User: `user`
        - Password: `password`
        - Database: `mydb`

4. **Stop the services (when done):**

    ```sh
    docker-compose down
    ```

## Notes

- The backend will automatically connect to the Postgres container using the environment variables set in `docker-compose.yml`.
- The frontend will proxy A
- PI requests to the backend at [http://localhost:8080](http://localhost:8080).

## Manual Run Guide

### Backend

1. **Set up the docker container**
   ```sh
   docker run --name mydb-postgres -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=mydb -p 5432:5432 -d postgres:15
   ```
2. **Set environment variables (or update `application.properties`):**
    ```env
    DB_URL=jdbc:postgresql://localhost:5432/mydb
    DB_USERNAME=user
    DB_PASSWORD=password
    ```

3. **In the backend directory, run:**
    ```sh
    ./gradlew bootRun
    ```

   The backend will be available at [http://localhost:8080](http://localhost:8080).

### Frontend

1. **In the frontend directory, install dependencies:**
    ```sh
    npm install
    ```

2. **Start the development server:**
    ```sh
    npm run dev
    ```

   The frontend will be available at [http://localhost:3000](http://localhost:3000).