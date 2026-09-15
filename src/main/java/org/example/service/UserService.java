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
            createUser(new User(null, "Evan Wright", "202 Elm Street, Portland, OR 97201", "evan.wright@example.com", "+1 (503) 555-0134"));
            createUser(new User(null, "Fiona Gallagher", "303 Michigan Avenue, Chicago, IL 60601", "fiona.gallagher@example.com", "+1 (312) 555-0176"));
            createUser(new User(null, "George Clark", "404 Peachtree Street, Atlanta, GA 30303", "george.clark@example.com", "+1 (404) 555-0155"));
            createUser(new User(null, "Hannah Abbott", "505 Beacon Street, Boston, MA 02116", "hannah.abbott@example.com", "+1 (617) 555-0182"));
            createUser(new User(null, "Ian Malcolm", "606 Broadway, New York, NY 10012", "ian.malcolm@example.com", "+1 (212) 555-0167"));
            createUser(new User(null, "Julia Roberts", "707 Ocean Drive, Miami, FL 33139", "julia.roberts@example.com", "+1 (305) 555-0148"));
            createUser(new User(null, "Kevin Bacon", "808 Market Street, Philadelphia, PA 19107", "kevin.bacon@example.com", "+1 (215) 555-0191"));
            createUser(new User(null, "Laura Croft", "909 Sunset Strip, Los Angeles, CA 90069", "laura.croft@example.com", "+1 (310) 555-0114"));
            createUser(new User(null, "Michael Scott", "1725 Slough Avenue, Scranton, PA 18503", "michael.scott@example.com", "+1 (570) 555-0165"));
            createUser(new User(null, "Nora Jones", "111 Music Row, Nashville, TN 37203", "nora.jones@example.com", "+1 (615) 555-0128"));
            createUser(new User(null, "Oliver Queen", "222 Starling Ave, Star City, WA 98004", "oliver.queen@example.com", "+1 (206) 555-0173"));
            createUser(new User(null, "Patricia Arquette", "333 Desert Inn Rd, Las Vegas, NV 89109", "patricia.arquette@example.com", "+1 (702) 555-0139"));
            createUser(new User(null, "Quentin Tarantino", "444 Hollywood Blvd, Los Angeles, CA 90028", "quentin.tarantino@example.com", "+1 (323) 555-0186"));
            createUser(new User(null, "Rachel Green", "555 Bedford Street, New York, NY 10014", "rachel.green@example.com", "+1 (212) 555-0195"));
            createUser(new User(null, "Steve Rogers", "666 Brooklyn Heights Blvd, Brooklyn, NY 11201", "steve.rogers@example.com", "+1 (718) 555-0150"));
            createUser(new User(null, "Tina Turner", "777 Beale Street, Memphis, TN 38103", "tina.turner@example.com", "+1 (901) 555-0162"));
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
