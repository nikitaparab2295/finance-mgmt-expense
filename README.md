# Expense Service

The **Expense Service** manages expenses and contacts the **Budget Service** to check  
budget usage. It also notifies the **Notification Service** when thresholds (80%, 100%) are crossed.

---

## 🚀 Tech Stack
- Spring Boot 3.5.8
- Java + Gradle
- Spring Data JPA + H2
- Feign Client (Budget + Notification services)
- Springdoc OpenAPI (Swagger)
- Docker / Docker Compose

---

## ▶️ Run Locally

```bash
cd expense
./gradlew bootRun

Runs on:
👉 http://localhost:8082

📘 Swagger Docs
👉 http://localhost:8082/swagger-ui.html
👉 http://localhost:8082/v3/api-docs
👉 http://localhost:8082/v3/api-docs.yaml

📮 Endpoints
POST /expenses
GET /expenses/{id}

Budget check happens automatically:
If expense causes > 80% usage → warning
If expense exceeds 100% → alert

🐳 Docker Instructions
docker build -t expense .
docker-compose up

🗃️ H2 Database
URL: http://localhost:8082/h2-console

JDBC: jdbc:h2:mem:expensedb

✔️ Tests
Run tests:
./gradlew test

