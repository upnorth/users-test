package org.example;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class UserServiceTest {

    @Inject
    UserService userService;

    @Test
    public void testGetAllUsers() {
        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    @Transactional
    public void testAddUser() {
        User user = new User(null, "Service Add User", "Service Address", "add@example.com", "+1 (555) 123-4567");
        User created = userService.addUser(user);
        assertNotNull(created.getId());
        assertTrue(created.getId().startsWith("usr-"));
        assertEquals("Service Add User", created.getName());
    }

    @Test
    @Transactional
    public void testGetUserById() {
        User user = new User(null, "Service Get User", "Service Address", "get@example.com", "+1 (555) 123-4567");
        User created = userService.addUser(user);
        Optional<User> fetched = userService.getUserById(created.getId());
        assertTrue(fetched.isPresent());
        assertEquals("Service Get User", fetched.get().getName());
        assertEquals("get@example.com", fetched.get().getEmail());
    }

    @Test
    @Transactional
    public void testUpdateUser() {
        User user = new User(null, "Service Before Update", "Old Address", "old@example.com", "+1 (555) 123-4567");
        User created = userService.addUser(user);

        User updatePayload = new User(null, "Service After Update", "New Address", "new@example.com", "+1 (555) 987-6543");
        Optional<User> updated = userService.updateUser(created.getId(), updatePayload);
        assertTrue(updated.isPresent());
        assertEquals("Service After Update", updated.get().getName());
        assertEquals("New Address", updated.get().getAddress());
        assertEquals("new@example.com", updated.get().getEmail());
        assertEquals("+1 (555) 987-6543", updated.get().getTelephone());
    }

    @Test
    @Transactional
    public void testDeleteUser() {
        User user = new User(null, "Service Delete User", "Service Address", "delete@example.com", "+1 (555) 123-4567");
        User created = userService.addUser(user);
        assertTrue(userService.deleteUser(created.getId()));
        assertTrue(userService.getUserById(created.getId()).isEmpty());
    }

    @Test
    public void testGetUserNotFound() {
        assertTrue(userService.getUserById("non-existent-id").isEmpty());
    }
}
