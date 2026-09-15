package org.example;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class UserServiceTest {

    @Inject
    UserService userService;

    @Test
    public void testGetAllUsers() {
        List<User> users = userService.getAllUsers();
        assertThat(users, notNullValue());
        assertThat(users, hasSize(greaterThanOrEqualTo(20)));
    }

    @Test
    @Transactional
    public void testAddUser() {
        User user = new User(null, "Service Add User", "Service Address", "add@example.com", "+1 (555) 123-4567");
        User created = userService.addUser(user);
        assertThat(created.getId(), notNullValue());
        assertThat(created.getId(), startsWith("usr-"));
        assertThat(created.getName(), equalTo("Service Add User"));
    }

    @Test
    @Transactional
    public void testGetUserById() {
        User user = new User(null, "Service Get User", "Service Address", "get@example.com", "+1 (555) 123-4567");
        User created = userService.addUser(user);
        Optional<User> fetched = userService.getUserById(created.getId());
        assertThat(fetched.isPresent(), is(true));
        assertThat(fetched.get().getName(), equalTo("Service Get User"));
        assertThat(fetched.get().getEmail(), equalTo("get@example.com"));
    }

    @Test
    @Transactional
    public void testUpdateUser() {
        User user = new User(null, "Service Before Update", "Old Address", "old@example.com", "+1 (555) 123-4567");
        User created = userService.addUser(user);

        User updatePayload = new User(null, "Service After Update", "New Address", "new@example.com", "+1 (555) 987-6543");
        Optional<User> updated = userService.updateUser(created.getId(), updatePayload);
        assertThat(updated.isPresent(), is(true));
        assertThat(updated.get().getName(), equalTo("Service After Update"));
        assertThat(updated.get().getAddress(), equalTo("New Address"));
        assertThat(updated.get().getEmail(), equalTo("new@example.com"));
        assertThat(updated.get().getTelephone(), equalTo("+1 (555) 987-6543"));
    }

    @Test
    @Transactional
    public void testDeleteUser() {
        User user = new User(null, "Service Delete User", "Service Address", "delete@example.com", "+1 (555) 123-4567");
        User created = userService.addUser(user);
        assertThat(userService.deleteUser(created.getId()), is(true));
        assertThat(userService.getUserById(created.getId()).isEmpty(), is(true));
    }

    @Test
    public void testGetUserNotFound() {
        assertThat(userService.getUserById("non-existent-id").isEmpty(), is(true));
    }
}
