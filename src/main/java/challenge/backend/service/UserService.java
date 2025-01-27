package challenge.backend.service;

import challenge.backend.model.User;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> getUserById(UUID id);
    void save(User user);
    BigDecimal calculateUserBalance(UUID id);
}
