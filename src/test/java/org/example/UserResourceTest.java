package org.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.example.model.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {

    @Test
    @Order(1)
    public void testListUsersInitial() {
        given()
                .when().get("/digg/user")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(20))
                .body("name", hasItem("Alice Johnson"))
                .body("name", hasItem("Tina Turner"));
    }

    @Test
    @Order(2)
    public void testCreateUserSuccess() {
        User newUser = new User(null, "Test User", "100 Innovation Way, Boston, MA 02110", "test.user@example.com", "+1 (617) 555-0100");

        String userId = given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when().post("/digg/user")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .header("Location", containsString("/digg/user/"))
                .body("id", notNullValue())
                .body("name", equalTo("Test User"))
                .body("email", equalTo("test.user@example.com"))
                .body("telephone", equalTo("+1 (617) 555-0100"))
                .body("address", equalTo("100 Innovation Way, Boston, MA 02110"))
                .extract().path("id");

        // Verify retrieval
        given()
                .when().get("/digg/user/" + userId)
                .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", equalTo("Test User"));
    }

    @Test
    @Order(3)
    public void testCreateUserValidationErrors() {
        // Test invalid email
        User invalidEmailUser = new User(null, "Invalid Email", "Valid Address", "not-an-email", "+1 (555) 000-0000");
        given()
                .contentType(ContentType.JSON)
                .body(invalidEmailUser)
                .when().post("/digg/user")
                .then()
                .statusCode(400);

        // Test blank name
        User blankNameUser = new User(null, "", "Valid Address", "valid@example.com", "+1 (555) 000-0000");
        given()
                .contentType(ContentType.JSON)
                .body(blankNameUser)
                .when().post("/digg/user")
                .then()
                .statusCode(400);

        // Test invalid phone number
        User invalidPhoneUser = new User(null, "Invalid Phone", "Valid Address", "valid@example.com", "abc");
        given()
                .contentType(ContentType.JSON)
                .body(invalidPhoneUser)
                .when().post("/digg/user")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(4)
    public void testGetUserNotFound() {
        given()
                .when().get("/digg/user/non-existent-user-id-9999")
                .then()
                .statusCode(404)
                .body("error", notNullValue());
    }

    @Test
    @Order(5)
    public void testUpdateUserSuccess() {
        // Create user first
        User initial = new User(null, "Original Name", "Original Address", "original@example.com", "+1 (555) 111-2233");
        String id = given()
                .contentType(ContentType.JSON)
                .body(initial)
                .when().post("/digg/user")
                .then()
                .statusCode(201)
                .extract().path("id");

        // Update user
        User updated = new User(null, "Updated Name", "Updated Address 99", "updated@example.com", "+1 (555) 999-8877");
        given()
                .contentType(ContentType.JSON)
                .body(updated)
                .when().put("/digg/user/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo("Updated Name"))
                .body("email", equalTo("updated@example.com"))
                .body("address", equalTo("Updated Address 99"))
                .body("telephone", equalTo("+1 (555) 999-8877"));
    }

    @Test
    @Order(6)
    public void testUpdateUserNotFound() {
        User updated = new User(null, "Updated Name", "Updated Address", "updated@example.com", "+1 (555) 999-8877");
        given()
                .contentType(ContentType.JSON)
                .body(updated)
                .when().put("/digg/user/non-existent-user-id-9999")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(7)
    public void testDeleteUserFlow() {
        // Create user
        User toDelete = new User(null, "To Be Deleted", "Delete Address 1", "delete.me@example.com", "+1 (555) 000-1111");
        String id = given()
                .contentType(ContentType.JSON)
                .body(toDelete)
                .when().post("/digg/user")
                .then()
                .statusCode(201)
                .extract().path("id");

        // Delete user
        given()
                .when().delete("/digg/user/" + id)
                .then()
                .statusCode(204);

        // Verify user is gone
        given()
                .when().get("/digg/user/" + id)
                .then()
                .statusCode(404);

        // Attempting to delete again returns 404
        given()
                .when().delete("/digg/user/" + id)
                .then()
                .statusCode(404);
    }
}
