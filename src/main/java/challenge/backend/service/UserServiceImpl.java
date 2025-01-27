package challenge.backend.service;

import challenge.backend.model.User;
import challenge.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public BigDecimal calculateUserBalance(UUID userId) {
        User user = this.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return user.calculateBalance();
    }
}
