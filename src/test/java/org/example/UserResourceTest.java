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
                .body("$", hasSize(greaterThanOrEqualTo(20)))
                .body("name", hasItem("Alice Johnson"))
                .body("name", hasItem("Tina Turner"));
    }

    @Test
    @Order(2)
    public void testCreateUserSuccess() {
        User newUser = new User(null, "Test User", "100 Innovation Way, Boston, MA 02110", "test.user@example.com", "+1 (617) 555-0100");

        given()
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
                .body("address", equalTo("100 Innovation Way, Boston, MA 02110"));
    }

    @Test
    @Order(3)
    public void testGetUserByIdSuccess() {
        User user = new User(null, "Lookup User", "123 Lookup Road, Chicago, IL 60601", "lookup@example.com", "+1 (312) 555-0199");
        String id = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when().post("/digg/user")
                .then()
                .statusCode(201)
                .extract().path("id");

        given()
                .when().get("/digg/user/" + id)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(id))
                .body("name", equalTo("Lookup User"))
                .body("address", equalTo("123 Lookup Road, Chicago, IL 60601"))
                .body("email", equalTo("lookup@example.com"))
                .body("telephone", equalTo("+1 (312) 555-0199"));
    }

    @Test
    @Order(4)
    public void testCreateUserValidationErrors() {
        // Invalid email
        given().contentType(ContentType.JSON)
                .body(new User(null, "Valid Name", "Valid Address", "not-an-email", "+1 (555) 000-0000"))
                .when().post("/digg/user")
                .then().statusCode(400);

        // Blank name
        given().contentType(ContentType.JSON)
                .body(new User(null, "   ", "Valid Address", "valid@example.com", "+1 (555) 000-0000"))
                .when().post("/digg/user")
                .then().statusCode(400);

        // Invalid telephone
        given().contentType(ContentType.JSON)
                .body(new User(null, "Valid Name", "Valid Address", "valid@example.com", "123"))
                .when().post("/digg/user")
                .then().statusCode(400);

        // Blank address
        given().contentType(ContentType.JSON)
                .body(new User(null, "Valid Name", "   ", "valid@example.com", "+1 (555) 000-0000"))
                .when().post("/digg/user")
                .then().statusCode(400);
    }

    @Test
    @Order(5)
    public void testGetUserNotFound() {
        given()
                .when().get("/digg/user/non-existent-user-id-9999")
                .then()
                .statusCode(404)
                .body("error", notNullValue());
    }

    @Test
    @Order(6)
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
    @Order(7)
    public void testUpdateUserValidationErrors() {
        User initial = new User(null, "Pre Validation Update", "Address 1", "pre.val@example.com", "+1 (555) 111-2233");
        String id = given()
                .contentType(ContentType.JSON)
                .body(initial)
                .when().post("/digg/user")
                .then()
                .statusCode(201)
                .extract().path("id");

        // Blank name on update
        given().contentType(ContentType.JSON)
                .body(new User(null, "", "Address 1", "pre.val@example.com", "+1 (555) 111-2233"))
                .when().put("/digg/user/" + id)
                .then().statusCode(400);
    }

    @Test
    @Order(8)
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
    @Order(9)
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
