# ---- Step 1: Build the JAR ----
FROM gradle:8.7-jdk17 AS build
WORKDIR /app

COPY . .
RUN gradle clean build -x test

# ---- Step 2: Run the application ----
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
