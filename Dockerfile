# ============================
# 1. Build Stage (Java 8)
# ============================
FROM maven:3.8.6-openjdk-8 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests


# ============================
# 2. Runtime Stage (Java 8)
# ============================
FROM eclipse-temurin:8-jre

WORKDIR /app

COPY --from=build /app/target/ecommerce-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]