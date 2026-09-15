package org.example.service;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.jboss.logging.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class UserService {

    private static final Logger LOG = Logger.getLogger(UserService.class);

    @Inject
    UserRepository userRepository;

    private final AtomicLong idSequence = new AtomicLong(1000);

    @Transactional
    void initSampleData(@Observes StartupEvent evt) {
        if (userRepository.count() == 0) {
            LOG.info("Initializing database user repository with seed data...");
            addUser(new User(null, "Alice Johnson", "123 Maple Street, Seattle, WA 98101", "alice.johnson@example.com", "+1 (206) 555-0143"));
            addUser(new User(null, "Bob Smith", "456 Oak Avenue, Austin, TX 78701", "bob.smith@example.com", "+1 (512) 555-0189"));
            addUser(new User(null, "Charlie Brown", "789 Pine Road, Denver, CO 80201", "charlie.brown@example.com", "+1 (303) 555-0122"));
            addUser(new User(null, "Diana Prince", "101 Gateway Blvd, San Francisco, CA 94105", "diana.prince@example.com", "+1 (415) 555-0199"));
            addUser(new User(null, "Evan Wright", "202 Elm Street, Portland, OR 97201", "evan.wright@example.com", "+1 (503) 555-0134"));
            addUser(new User(null, "Fiona Gallagher", "303 Michigan Avenue, Chicago, IL 60601", "fiona.gallagher@example.com", "+1 (312) 555-0176"));
            addUser(new User(null, "George Clark", "404 Peachtree Street, Atlanta, GA 30303", "george.clark@example.com", "+1 (404) 555-0155"));
            addUser(new User(null, "Hannah Abbott", "505 Beacon Street, Boston, MA 02116", "hannah.abbott@example.com", "+1 (617) 555-0182"));
            addUser(new User(null, "Ian Malcolm", "606 Broadway, New York, NY 10012", "ian.malcolm@example.com", "+1 (212) 555-0167"));
            addUser(new User(null, "Julia Roberts", "707 Ocean Drive, Miami, FL 33139", "julia.roberts@example.com", "+1 (305) 555-0148"));
            addUser(new User(null, "Kevin Bacon", "808 Market Street, Philadelphia, PA 19107", "kevin.bacon@example.com", "+1 (215) 555-0191"));
            addUser(new User(null, "Laura Croft", "909 Sunset Strip, Los Angeles, CA 90069", "laura.croft@example.com", "+1 (310) 555-0114"));
            addUser(new User(null, "Michael Scott", "1725 Slough Avenue, Scranton, PA 18503", "michael.scott@example.com", "+1 (570) 555-0165"));
            addUser(new User(null, "Nora Jones", "111 Music Row, Nashville, TN 37203", "nora.jones@example.com", "+1 (615) 555-0128"));
            addUser(new User(null, "Oliver Queen", "222 Starling Ave, Star City, WA 98004", "oliver.queen@example.com", "+1 (206) 555-0173"));
            addUser(new User(null, "Patricia Arquette", "333 Desert Inn Rd, Las Vegas, NV 89109", "patricia.arquette@example.com", "+1 (702) 555-0139"));
            addUser(new User(null, "Quentin Tarantino", "444 Hollywood Blvd, Los Angeles, CA 90028", "quentin.tarantino@example.com", "+1 (323) 555-0186"));
            addUser(new User(null, "Rachel Green", "555 Bedford Street, New York, NY 10014", "rachel.green@example.com", "+1 (212) 555-0195"));
            addUser(new User(null, "Steve Rogers", "666 Brooklyn Heights Blvd, Brooklyn, NY 11201", "steve.rogers@example.com", "+1 (718) 555-0150"));
            addUser(new User(null, "Tina Turner", "777 Beale Street, Memphis, TN 38103", "tina.turner@example.com", "+1 (901) 555-0162"));
            LOG.infof("Initialized database repository with %d seed users", userRepository.count());
        }
    }

    public List<User> getAllUsers() {
        return userRepository.listAll().stream()
                .sorted(Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public Optional<User> getUserById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return userRepository.findByIdOptional(id);
    }

    @Transactional
    public User addUser(User user) {
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId("usr-" + idSequence.incrementAndGet());
        }
        userRepository.persist(user);
        LOG.debugf("Stored user: id=%s", user.getId());
        return user;
    }

    @Transactional
    public Optional<User> updateUser(String id, User updatedUser) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        Optional<User> existingOpt = userRepository.findByIdOptional(id);
        if (existingOpt.isEmpty()) {
            return Optional.empty();
        }
        User existing = existingOpt.get();
        existing.setName(updatedUser.getName());
        existing.setAddress(updatedUser.getAddress());
        existing.setEmail(updatedUser.getEmail());
        existing.setTelephone(updatedUser.getTelephone());
        LOG.debugf("Updated store entry for user: id=%s", id);
        return Optional.of(existing);
    }

    @Transactional
    public boolean deleteUser(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        boolean removed = userRepository.deleteById(id);
        if (removed) {
            LOG.debugf("Removed store entry for user: id=%s", id);
        }
        return removed;
    }

    @Transactional
    public void clear() {
        userRepository.deleteAll();
        LOG.debug("In-memory store cleared");
    }

    public int count() {
        return (int) userRepository.count();
    }
}
