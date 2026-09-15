package org.example.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.example.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class UserService {

    private final Map<String, User> userStore = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1000);

    @PostConstruct
    void initSampleData() {
        if (userStore.isEmpty()) {
            createUser(new User(null, "Alice Johnson", "123 Maple Street, Seattle, WA 98101", "alice.johnson@example.com", "+1 (206) 555-0143"));
            createUser(new User(null, "Bob Smith", "456 Oak Avenue, Austin, TX 78701", "bob.smith@example.com", "+1 (512) 555-0189"));
            createUser(new User(null, "Charlie Brown", "789 Pine Road, Denver, CO 80201", "charlie.brown@example.com", "+1 (303) 555-0122"));
            createUser(new User(null, "Diana Prince", "101 Gateway Blvd, San Francisco, CA 94105", "diana.prince@example.com", "+1 (415) 555-0199"));
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>(userStore.values());
        list.sort(Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER));
        return list;
    }

    public Optional<User> getUserById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(userStore.get(id));
    }

    public User createUser(User user) {
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId("usr-" + idSequence.incrementAndGet());
        }
        userStore.put(user.getId(), user);
        return user;
    }

    public Optional<User> updateUser(String id, User updatedUser) {
        if (id == null || !userStore.containsKey(id)) {
            return Optional.empty();
        }
        updatedUser.setId(id);
        userStore.put(id, updatedUser);
        return Optional.of(updatedUser);
    }

    public boolean deleteUser(String id) {
        if (id == null) {
            return false;
        }
        return userStore.remove(id) != null;
    }

    public void clear() {
        userStore.clear();
    }

    public int count() {
        return userStore.size();
    }
}
