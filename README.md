# Expense Service

The **Expense Service** manages expenses and contacts the **Budget Service** to check budget usage.
It also notifies the **Notification Service** when thresholds (80% and 100%) are crossed.

---

## Tech Stack

* Spring Boot 3.5.8
* Java + Gradle
* Spring Data JPA + H2
* Feign Client (Budget + Notification services)
* Springdoc OpenAPI (Swagger)
* Docker / Docker Compose

---

## Run Locally

```bash
cd finance-mgmt-expense
./gradlew bootRun
```

**Service runs at:**
[http://localhost:8082](http://localhost:8082)

---

## API Documentation (Swagger)

* [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
* [http://localhost:8082/v3/api-docs](http://localhost:8082/v3/api-docs)
* [http://localhost:8082/v3/api-docs.yaml](http://localhost:8082/v3/api-docs.yaml)

Swagger specifications: expense-openapi.yaml You can also import the provided expense-postman-collection.json into Postman or any API tool.
---

## Endpoints

* **POST** `/expenses`
* **GET** `/expenses/{id}`

### Budget Check Logic

* If an expense causes usage to exceed **80%** → a warning is triggered
* If an expense causes usage to exceed **100%** → an alert is triggered

---

## Docker

```bash
docker build -t expense .
docker-compose up
```

---

## H2 Database

URL: [http://localhost:8082/h2-console](http://localhost:8082/h2-console)
**JDBC URL:** `jdbc:h2:mem:expensedb`

---

## Tests

Run tests using:

```bash
./gradlew test
```
