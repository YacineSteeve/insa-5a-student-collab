INSA Student Collaboration Platform — Architecture and Service Report

Overview
- Purpose: A microservices-based platform for peer help between students (post/help requests, recommendations, reviews, profiles).
- Tech stack: Spring Boot (per service), Spring Cloud (Gateway, Eureka Discovery, Config Server), JPA/Hibernate with MySQL, JWT for authentication, WebClient for internal calls.
- Services:
  - discovery-service: Eureka server for service registry.
  - config-service: Spring Cloud Config Server serving centralized configuration stored in Git.
  - api-gateway: Single entry point, request routing, JWT verification, and user context propagation.
  - student-service: User management (signup/login), student profiles, and reviews.
  - help-request-service: Help request lifecycle (create, list, update, assign, delete).
  - recommendation-service: Suggests students for a given help request based on simple scoring.

Configuration Management (config-service)
- App name: config-service, runs on port 8888.
- Stores client configurations in a Git repo (see application.properties):
  - spring.cloud.config.server.git.uri=https://github.com/YacineSteeve/insa-5a-student-collab
  - spring.cloud.config.server.git.search-paths=config-service/src/main/resources/clients
- Notable client configs (files within config-service/src/main/resources/clients):
  - discovery-service.properties
    - server.port=8761
    - Eureka standalone mode (register-with-eureka=false, fetch-registry=false).
  - api-gateway.properties
    - server.port=8080
    - Eureka defaultZone
    - Shared JWT: security.jwt.secret-key (base64) and security.jwt.expiration-time=3600000 (1h)
  - student-service.properties
    - server.port=8081, MySQL connection (jdbc:mysql://localhost:3306/projet_gei_062), user/pwd, MySQL8 dialect, ddl-auto=update
    - security.jwt.secret-key and expiration-time (same as gateway)
  - help-request-service.properties
    - server.port=8082, same MySQL config and JPA settings
  - recommendation-service.properties
    - server.port=8083, Eureka defaultZone
- Each service imports the config server via: spring.config.import=optional:configserver:${SPRING_CLOUD_CONFIG_URI:http://localhost:8888}

Service Discovery (discovery-service)
- App name: discovery-service.
- Eureka server on port 8761.
- Central registry allowing clients to register and discover each other (lb:// URIs).

API Gateway (api-gateway)
- App name: api-gateway, runs on port 8080.
- Routes (application.properties):
  - /student-service/** → lb://student-service (StripPrefix=1)
  - /help-request-service/** → lb://help-request-service (StripPrefix=1)
  - /recommendation-service/** → lb://recommendation-service (StripPrefix=1)
- Global JWT filter: JwtAuthGlobalFilter
  - Exempts the following from authentication: 
    - /student-service/auth/** (signup/login)
    - /student-service/students (GET list)
    - /help-request-service/help-requests (GET list)
    - OpenAPI docs endpoints for each service (v3/api-docs)
  - Validates Authorization: Bearer <token>
  - On success, forwards headers:
    - X-User-Email: JWT subject
    - X-User-Id: claims["id"] as string (supports several numeric types)
  - On failure/expiration: responds 401.
- JwtService (gateway) uses the shared base64 secret (security.jwt.secret-key) to parse and validate tokens.

Student Service (student-service)
- Purpose: Account management, profile CRUD (self), listing students, and managing reviews.
- Security: SecurityConfig permits all requests (authorization handled at gateway); stateless session.
- Persistence (JPA/Hibernate to MySQL):
  - Entity Student (table student):
    - id (PK), createdAt, updatedAt (timestamps)
    - lastName, firstName, email (unique), password
    - establishment, major (enum), skills (collection table student_skills), availabilities (collection table student_availabilities)
    - OneToMany reviews (mapped by Review.student)
    - Transient getAverageRating() computes average of related reviews
  - Entity Review (table review):
    - id (PK), createdAt, updatedAt
    - student (ManyToOne), rating [0..5], comment (<=1000), helpRequestId
- Authentication API (AuthenticationController):
  - POST /auth/signup
    - Body: RegistrationDTO (fields not shown here, typical: email, password, profile)
    - Creates Student via AuthenticationService.signup; returns StudentDTO
  - POST /auth/login
    - Body: LoginDTO (email/password)
    - Authenticates via AuthenticationService.authenticate
    - Generates JWT via JwtService.generateToken(claims={"id": student.id}, subject=email), returns { token, expiresInMs }
  - JwtService (student-service) signs tokens using shared key and configured expiration-time.
- Student API (StudentController):
  - GET /students
    - Public (whitelisted at gateway). Accepts @ModelAttribute StudentsFilters for search (e.g., skills/majors, see codebase).
  - GET /students/{id}
    - Returns StudentDTO by id.
  - GET /students/me
    - Requires auth; uses X-User-Id header from gateway.
  - PATCH /students/me
    - Requires auth; updates own profile from StudentUpdateDTO.
  - DELETE /students/me
    - Requires auth; delete own account.
- Review API (ReviewController):
  - GET /reviews?helpRequestId={optional}
    - Requires auth; returns reviews for the authenticated student; optionally filter by helpRequestId.
  - POST /reviews
    - Requires auth; body: ReviewCreationDTO { rating, comment, helpRequestId }
    - Validates via HelpRequestService (feign/http client in student-service) that the caller is the author of the referenced help request; then creates a Review for the assignee of that request.
- Core review logic (ReviewService):
  - createReview(studentId, ReviewCreationDTO) → persists Review linked to Student and helpRequestId.
  - getAllForStudent(studentId, helpRequestId?) → returns student’s reviews (optionally filtered).

Help Request Service (help-request-service)
- Purpose: CRUD and workflow for help requests.
- Persistence (JPA/Hibernate to MySQL):
  - Entity HelpRequest (table help_request):
    - id (PK), timestamps
    - title, description, authorId, assigneeId (nullable)
    - status: WAITING, IN_PROGRESS, DONE, ABANDONED, CLOSED
    - desiredDate (Instant)
    - keywords (collection table help_request_keywords)
- Controller (HelpRequestController):
  - GET /help-requests
    - Public (whitelisted at gateway); supports filters (HelpRequestsFilters: keywords, statuses, desiredDateFrom/To).
  - GET /help-requests/{id}
    - Public endpoint to fetch a specific help request.
  - GET /help-requests/by-me
    - Requires auth; lists help requests authored by the caller (X-User-Id).
  - GET /help-requests/for-me
    - Requires auth; lists help requests assigned to the caller.
  - POST /help-requests
    - Requires auth; creates a help request authored by the caller.
  - PATCH /help-requests/{id}
    - Requires auth; partial update by author (title, description, keywords, desiredDate, status per service rules).
  - PATCH /help-requests/{id}/assignee?assigneeId=...
    - Requires auth; change assignee (typically author action).
  - DELETE /help-requests/{id}
    - Requires auth; delete by author.

Recommendation Service (recommendation-service)
- Purpose: Given a help request, recommend up to 10 students ordered by match score.
- Data sources:
  - Calls student-service GET /students to retrieve all students via WebClient with lb://student-service.
  - Calls help-request-service to fetch the referenced help request (via a dedicated HelpRequestService class; not shown here in full, but used in RecommendationService).
- Controller:
  - GET /recommendations?helpRequestId=ID
    - Requires auth; only the author of the help request can request recommendations for it.
- Scoring (RecommendationService):
  - Skill match: overlap of help request keywords vs student skills (weighted up to 40 points).
  - Availability: +20 if student has any availability listed.
  - Reputation: student average rating (0–5) multiplied by 8 (max +40).
  - Score capped at 100; provides a human-readable reason string.

Authentication and Authorization Flow
1) Registration and Login (student-service):
   - POST /auth/signup creates a Student.
   - POST /auth/login verifies credentials and issues a JWT:
     - Subject: student email
     - Claims: { id: studentId }
     - Signature: HS key from security.jwt.secret-key (shared with gateway)
     - Expiration: security.jwt.expiration-time (ms)
2) Gateway verification (api-gateway):
   - Global filter validates Bearer token on all non-exempt routes.
   - On success, forwards X-User-Id and X-User-Email to downstream services.
3) Downstream authorization:
   - Controllers check X-User-Id and enforce resource ownership explicitly (e.g., Review creation requires caller to be the author of the help request; Recommendation endpoint verifies the same).
   - Services themselves trust gateway for authentication; they return 401 when X-User-Id is missing.

Database and Persistence Choices
- Single MySQL schema (jdbc:mysql://localhost:3306/projet_gei_062) shared by student-service and help-request-service in current configuration.
  - Pros: simplicity for a student project; fewer schemas to manage.
  - Cons: tighter coupling and potential cross-service interference; for production, separate schemas are recommended.
- Hibernate ddl-auto=update for rapid iteration; dialect: MySQL8.
- Collections mapped via @ElementCollection into dedicated tables:
  - student_skills, student_availabilities
  - help_request_keywords

Available Actions Summary (per service)
- student-service
  - POST /auth/signup, POST /auth/login
  - GET /students (public), GET /students/{id}
  - GET /students/me, PATCH /students/me, DELETE /students/me
  - GET /reviews[?helpRequestId], POST /reviews
- help-request-service
  - GET /help-requests (public), GET /help-requests/{id}
  - GET /help-requests/by-me, GET /help-requests/for-me
  - POST /help-requests, PATCH /help-requests/{id}, PATCH /help-requests/{id}/assignee, DELETE /help-requests/{id}
- recommendation-service
  - GET /recommendations?helpRequestId=...

Error Handling and Status Codes
- Common patterns:
  - 401 when missing/invalid JWT (or missing X-User-Id at controller level).
  - 403 when the user lacks ownership (e.g., requesting recommendations for a help request they did not author).
  - 404 when resource not found (custom NotFoundException mapped to 404 in controllers).
  - 400 for bad input (e.g., non-numeric X-User-Id), 500 for unexpected errors.

Operational Notes
- Launch order (local):
  1) discovery-service (Eureka)
  2) config-service (Git-backed config)
  3) student-service, help-request-service, recommendation-service (they will register with Eureka and import config)
  4) api-gateway (exposes unified routes on :8080)
- API base paths through gateway:
  - http://localhost:8080/student-service/...
  - http://localhost:8080/help-request-service/...
  - http://localhost:8080/recommendation-service/...
- OpenAPI docs are exposed per service under /v3/api-docs and are bypassed by the JWT filter.

Security Considerations and Future Improvements
- Current student-service SecurityConfig permits all; rely on gateway for authentication. For defense-in-depth, consider per-service JWT verification as well.
- Shared DB across services is a trade-off; prefer schema-per-service in production.
- Password storage (in AuthenticationService) should use a strong encoder (e.g., BCryptPasswordEncoder) — ensure it is configured.
- Rotate JWT secrets and support refresh tokens for long-lived sessions.
- Consider rate limiting at the gateway.

Appendix: Key Classes and Responsibilities
- api-gateway
  - JwtAuthGlobalFilter: validates JWT, sets X-User-Id/X-User-Email headers, manages exemptions.
  - JwtService (gateway): parsing and verification utilities for JWT using the shared secret.
- student-service
  - AuthenticationController: signup/login issuing tokens.
  - StudentController: profile operations and listing.
  - ReviewController/ReviewService: review CRUD entry points (GET, POST basic) and transformations.
  - JwtService (student): token building and signing.
  - Entities: Student, Review (JPA mappings including collection tables and relationships).
- help-request-service
  - HelpRequestController: CRUD endpoints and filters.
  - Entity: HelpRequest with keywords, status, author/assignee, desiredDate.
- recommendation-service
  - RecommendationController/RecommendationService: scoring and authorization checks.
  - StudentService (client): fetch students via lb://student-service.
  - HelpRequestService (client): fetch a help request for validation and inputs.
