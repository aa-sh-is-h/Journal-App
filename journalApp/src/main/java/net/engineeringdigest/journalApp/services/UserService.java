package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();

    Optional<User> findById(String id);

    void saveEntry(User user);

    void deleteById(String id);

    User findByUserName(String username);

    void deleteByUserName(String username);
}
