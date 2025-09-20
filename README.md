# Task Manager Spring Boot Application

A RESTful Task Management API built with **Spring Boot**, supporting CRUD operations, filtering, pagination, and statistics. Uses **H2 in-memory database** for storage.

---

## Features

- Create, read, update, and delete tasks.
- Patch task status.
- Pagination and filtering by status, priority, and due date.
- Task statistics:
  - Not overdue count
  - Overdue count
  - Detailed status & priority counts
- Global exception handling with custom error response.
- Validation for task fields.

---

## Tech Stack

- **Backend:** Spring Boot, Java 21
- **Database:** H2 in-memory
- **Build Tool:** Maven
- **API Docs:** Swagger/OpenAPI

---

## Getting Started

### 1. Clone the Repository

```bash
git clone [https://github.com/yourusername/task-manager.git](https://github.com/aadityaagrawal/TaskManager.git)
cd TaskManager
```

### 2. Run the Application

```bash
mvn spring-boot:run
```

The application will start at: `http://localhost:8080`

---

## H2 Database Setup

The application uses **H2 in-memory database** for simplicity and auto-initialization.

### Configuration (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:taskdb
    driverClassName: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true
    properties:
      hibernate.format_sql: true
  h2:
    console:
      enabled: true
      path: /h2-console
  sql:
    init:
      mode: always
```

### SQL Scripts

- **`src/main/resources/schema.sql`** → contains table definitions.
- **`src/main/resources/data.sql`** → contains sample data.

These scripts **auto-run on service startup**, initializing the H2 database.

### Access H2 Console

URL: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:taskdb`
- Username: `sa`
- Password: (leave blank)

---

## API Endpoints

### Tasks

| Method | Endpoint                 | Description                        |
|--------|-------------------------|------------------------------------|
| GET    | /api/tasks              | Get all tasks with optional filters|
| GET    | /api/tasks/{id}         | Get task by ID                     |
| POST   | /api/tasks              | Add a new task                     |
| PUT    | /api/tasks/{id}         | Update an existing task            |
| PATCH  | /api/tasks/{id}/status  | Update task status                 |
| DELETE | /api/tasks/{id}         | Delete a task                      |
| GET    | /api/tasks/stats        | Get task statistics                |

---

## Validation Rules

- `title` → Not blank, max 100 characters.
- `description` → Max 500 characters.
- `status` → Must be one of [PENDING, IN_PROGRESS, COMPLETED].
- `priority` → Must be one of [LOW, MEDIUM, HIGH].
- `dueDate` → Cannot be null or in the past.

---

## Error Handling

The application uses a **global exception handler** to return structured errors:

```json
{
  "timestamp": "2025-09-21T22:45:00",
  "status": 400,
  "error": "Validation failed for one or more fields",
  "fieldErrors": {
    "title": "must not be blank",
    "status": "must not be null"
  }
}
```

---

## Swagger UI

- Swagger UI available at: `http://localhost:8080/swagger-ui.html`

---

## Notes

- The database is **in-memory**, so all data will be lost after shutting down the application.
- Use `schema.sql` and `data.sql` to pre-populate tables on startup.
- Enums `Status` and `Priority` are validated automatically using `@NotNull` and custom exception handling.

