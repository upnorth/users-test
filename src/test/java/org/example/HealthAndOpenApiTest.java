package org.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class HealthAndOpenApiTest {

    @Test
    public void testHealthEndpoint() {
        given()
                .when().get("/health")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("status", equalTo("UP"))
                .body("service", equalTo("user-api"));
    }

    @Test
    public void testMicroProfileHealthEndpoint() {
        given()
                .when().get("/q/health")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("status", equalTo("UP"))
                .body("checks.name", hasItem("user-api-liveness"))
                .body("checks.name", hasItem("user-api-readiness"));
    }

    @Test
    public void testOpenApiEndpoint() {
        given()
                .when().get("/q/openapi")
                .then()
                .statusCode(200)
                .body(containsString("/digg/user"))
                .body(containsString("User API"));
    }

    @Test
    public void testFrontendServed() {
        given()
                .when().get("/")
                .then()
                .statusCode(200)
                .contentType(ContentType.HTML)
                .body(containsString("UserHub"))
                .body(containsString("Vue 3"));
    }
}
