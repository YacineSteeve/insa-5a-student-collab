Student Collab Platform — Microservices (Spring Boot + Spring Cloud)

Overview
This repository contains a microservices-based platform that helps students collaborate: create help requests, manage profiles, and get recommendations. The system uses Spring Boot and Spring Cloud (Config, Eureka Discovery, API Gateway) with a shared MySQL database.

Services and default ports
- Config Service: 8888
- Discovery Service (Eureka): 8761
- API Gateway: 8080
- Student Service: 8081
- Help Request Service: 8082
- Recommendation Service: 8083

High-level architecture
- All services (except Config) load their configuration from the Config Server (via SPRING_CLOUD_CONFIG_URI or default http://localhost:8888).
- Services register to Eureka Discovery for dynamic routing by logical names.
- Clients call the API through the API Gateway only. The gateway routes:
  - /student-service/** → student-service
  - /help-request-service/** → help-request-service
  - /recommendation-service/** → recommendation-service
- Security: JWT issued by student-service. Gateway validates JWT and forwards user context in headers (X-User-Id, X-User-Email if present).

Prerequisites
- JDK 17+
- Maven 3.9+
- MySQL 8.x available locally
- Git and internet access (for Maven dependencies)

Database setup (local dev)
By default, all business services use a single MySQL database with the following settings (from Config Server):
- URL: jdbc:mysql://localhost:3306/projet_gei_062
- Username: projet_gei_062
- Password: Ohbee2de

Steps to prepare MySQL locally:
1) Create database and user (adapt if you already have them):
   - CREATE DATABASE projet_gei_062 CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
   - CREATE USER 'projet_gei_062'@'%' IDENTIFIED BY 'Ohbee2de';
   - GRANT ALL PRIVILEGES ON projet_gei_062.* TO 'projet_gei_062'@'%';
   - FLUSH PRIVILEGES;
2) Ensure MySQL is listening on localhost:3306 and accessible by the created user.

Configuration source
- Config Server reads client configurations from this repo (see config-service/src/main/resources/clients):
  - api-gateway.properties → server.port=8080, JWT shared secret
  - discovery-service.properties → server.port=8761
  - student-service.properties → server.port=8081, datasource, JPA, JWT
  - help-request-service.properties → server.port=8082, datasource, JPA
  - recommendation-service.properties → server.port=8083

Environment variables
- SPRING_CLOUD_CONFIG_URI (optional): set to your Config Server URL if not using default http://localhost:8888
  Example: export SPRING_CLOUD_CONFIG_URI=http://localhost:8888

How to run locally (dev)
Run each service in a dedicated terminal. Start in the following order:
1) Config Service (port 8888)
   - cd config-service
   - mvn spring-boot:run

2) Discovery Service (port 8761)
   - In a new terminal
   - cd discovery-service
   - mvn spring-boot:run
   - Eureka UI: http://localhost:8761

3) Student Service (port 8081)
   - New terminal
   - cd student-service
   - mvn spring-boot:run

4) Help Request Service (port 8082)
   - New terminal
   - cd help-request-service
   - mvn spring-boot:run

5) Recommendation Service (port 8083)
   - New terminal
   - cd recommendation-service
   - mvn spring-boot:run

6) API Gateway (port 8080)
   - New terminal
   - cd api-gateway
   - mvn spring-boot:run

Verifying the setup
- Check Eureka registry: http://localhost:8761 should list api-gateway, student-service, help-request-service, recommendation-service once they are started.
- Check basic health endpoints (example via gateway or directly):
  - Gateway (actuator may be limited if not enabled), but you can call service OpenAPI UIs directly:
  - Student Service Swagger UI: http://localhost:8081/swagger-ui/index.html
  - Help Request Service Swagger UI: http://localhost:8082/swagger-ui/index.html
  - Recommendation Service Swagger UI: http://localhost:8083/swagger-ui/index.html
- Through the gateway, typical base paths are:
  - http://localhost:8080/student-service/...
  - http://localhost:8080/help-request-service/...
  - http://localhost:8080/recommendation-service/...

Security model (JWT via gateway)
1) A user signs up and logs in on student-service to obtain a JWT.
2) Client calls other endpoints through the gateway with Authorization: Bearer <token>.
3) The gateway validates the token and adds X-User-Id to downstream requests.

Quick start: Example API usage
All examples below go through the Gateway (recommended for real usage). You can also hit services directly on their ports for debugging.

1) Register a student
POST http://localhost:8080/student-service/auth/signup
Content-Type: application/json
Body example:
{
  "firstname": "Ada",
  "lastname": "Lovelace",
  "email": "ada@example.com",
  "password": "StrongPassw0rd!",
  "school": "INSA",
  "major": "CS"
}

2) Login to get a JWT
POST http://localhost:8080/student-service/auth/login
Content-Type: application/json
Body example:
{
  "email": "ada@example.com",
  "password": "StrongPassw0rd!"
}
Successful response example:
{
  "token": "<JWT>",
  "expiresIn": 3600000
}
Save the token as an environment variable for convenience:
export TOKEN="<JWT>"

3) Create a help request (authenticated)
POST http://localhost:8080/help-request-service/help-requests
Headers:
- Authorization: Bearer $TOKEN
- Content-Type: application/json
Body example:
{
  "title": "Need help with Graph Algorithms",
  "description": "Looking for someone to review Dijkstra and A* with me.",
  "keywords": ["graphs", "algorithms", "dijkstra"],
  "desiredDate": "2025-12-10T10:00:00Z"
}

4) List my help requests (authenticated)
GET http://localhost:8080/help-request-service/help-requests/by-me
Headers:
- Authorization: Bearer $TOKEN

5) List all help requests (public)
GET http://localhost:8080/help-request-service/help-requests

6) Explore APIs (OpenAPI/Swagger)
- Student Service UI: http://localhost:8081/swagger-ui/index.html
- Help Request Service UI: http://localhost:8082/swagger-ui/index.html
- Recommendation Service UI: http://localhost:8083/swagger-ui/index.html
Note: When calling via Gateway, Springdoc UIs are typically not proxied by default. Prefer opening the UIs directly on the service ports for docs, but call the APIs through the gateway for real flows.

Recommendation service
- This service aggregates data from student-service and help-request-service and exposes recommendation endpoints (see its Swagger UI for the exact paths if available). It does not require a database.

Troubleshooting
- 404/No instances available: Ensure all services have registered in Eureka (http://localhost:8761) and that you started Config Service first so clients could load their properties.
- Cannot connect to Config Server: Set SPRING_CLOUD_CONFIG_URI to http://localhost:8888 (or your host/port) and restart the service.
- Database connection errors: Verify MySQL is running, the database and user exist, and credentials in config match your local setup.
- JWT validation errors at gateway: Ensure you use the token from student-service /auth/login and pass it as Authorization: Bearer <token> when calling protected endpoints through the gateway.

Development tips
- Use mvn -DskipTests spring-boot:run if you want to start faster without running tests.
- To rebuild quickly after code changes, stop and rerun the affected service only.

Project layout
- api-gateway: Spring Cloud Gateway, JWT validation + routing
- config-service: Spring Cloud Config Server (loads client configs from this repo)
- discovery-service: Eureka server
- student-service: identity, profiles, authentication (JWT issuance)
- help-request-service: CRUD of help requests and workflow
- recommendation-service: computes recommended helpers using data from other services

License / ownership
This codebase is for academic purposes. See REPORT.md for a detailed architecture report and context.
