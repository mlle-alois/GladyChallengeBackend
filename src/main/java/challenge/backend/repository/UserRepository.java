package challenge.backend.repository;

import challenge.backend.model.User;
import challenge.backend.util.JsonFileReader;
import challenge.backend.util.JsonFileWriter;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class UserRepository {
    private final JsonFileReader jsonFileReader;
    private final JsonFileWriter jsonFileWriter;

    @Value("${data.user.json.file}")
    private String jsonFilePath;

    public Optional<User> findById(UUID userId) {
        TypeReference<List<User>> userType = new TypeReference<>() {};
        Optional<List<User>> foundUsers = jsonFileReader.readJsonFile(jsonFilePath, userType);

        return foundUsers.flatMap(users -> users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst());
    }

    public void save(User toSaveUser) {
        Optional<List<User>> foundUsers = jsonFileReader.readJsonFile(jsonFilePath, new TypeReference<>() {});

        List<User> users = foundUsers.orElseGet(ArrayList::new);
        users.removeIf(user -> user.getId().equals(toSaveUser.getId()));
        users.add(toSaveUser);

        jsonFileWriter.writeListToFile(jsonFilePath, users);
    }
}
