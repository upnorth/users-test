# Stage 1: Build stage
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace

# Copy build descriptor and wrapper
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Download dependencies (offline cache layer)
RUN ./mvnw dependency:go-offline -B || true

# Copy source code and build package
COPY src src
RUN ./mvnw package -DskipTests

# Stage 2: Minimal Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

ENV LANGUAGE='en_US:en'

# Copy the fast-jar build output from the builder stage
COPY --from=build /workspace/target/quarkus-app/lib/ /app/lib/
COPY --from=build /workspace/target/quarkus-app/*.jar /app/
COPY --from=build /workspace/target/quarkus-app/app/ /app/app/
COPY --from=build /workspace/target/quarkus-app/quarkus/ /app/quarkus/

EXPOSE 8080
USER 1001

ENTRYPOINT ["java", "-Dquarkus.http.host=0.0.0.0", "-Djava.util.logging.manager=org.jboss.logmanager.LogManager", "-jar", "/app/quarkus-run.jar"]
