# Stage 1: Build application
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace

COPY . .
RUN ./mvnw package -DskipTests

# Stage 2: Create runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /workspace/target/quarkus-app/ /app/

EXPOSE 8080

ENTRYPOINT ["java", "-Dquarkus.http.host=0.0.0.0", "-jar", "/app/quarkus-run.jar"]
