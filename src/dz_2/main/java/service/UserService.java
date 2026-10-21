package service;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final Map<Integer, User> users = new ConcurrentHashMap<>();

    static {
        users.put(1, new User(1, "Ivan", "ivan@test.com", "active"));
        users.put(2, new User(2, "Anna", "anna@test.com", "active"));
    }

    public User getById(int id) {
        return users.get(id);
    }

    public boolean update(int id, String name, String email) {
        User user = getById(id);

        if (user == null) {
            return false;
        }

        user.setName(name);
        user.setEmail(email);

        log.info("User {} updated: name={}, email={}", id, name, email);
        return true;
    }

    public boolean updateStatus(int id, String status) {
        User user = users.get(id);
        if (user == null) {
            return false;
        }
        user.setStatus(status);
        log.info("User {} status changed to {}", id, status);
        return true;
    }

    public int count() {
        return users.size();
    }
}
