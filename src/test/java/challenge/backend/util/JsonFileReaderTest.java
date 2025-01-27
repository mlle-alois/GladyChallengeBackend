package challenge.backend.util;

import challenge.backend.model.Deposit;
import challenge.backend.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static challenge.backend.model.DepositType.GIFT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JsonFileReaderTest {
    @Autowired
    private JsonFileReader jsonFileReader;

    @Value("${data.user.json.file}")
    private String jsonUserFilePath;

    @Value("${data.fake.json.file}")
    private String jsonFakeFilePath;

    @Test
    @DisplayName("Should read users JSON file correctly")
    void should_ReadUsersJsonFileCorrectly_When_FileExists() {
        // Given
        TypeReference<List<User>> typeReference = new TypeReference<>() {
        };

        int expectedUsersCount = 2;

        // When
        Optional<List<User>> users = jsonFileReader.readJsonFile(jsonUserFilePath, typeReference);

        // Then
        assertTrue(users.isPresent());
        assertEquals(expectedUsersCount, users.get().size());

        User firstUser = users.get().getFirst();
        assertEquals(UUID.fromString("7626e4a6-fdfd-44e4-b602-320a4c8aa6fb"), firstUser.getId());
        assertEquals("John Doe", firstUser.getName());
        assertEquals(2, firstUser.getDeposits().size());

        Deposit firstDeposit = firstUser.getDeposits().getFirst();
        assertEquals(UUID.fromString("33902626-f539-4215-91f3-7fa65be39b24"), firstDeposit.getId());
        assertEquals(GIFT, firstDeposit.getDepositType());
        assertEquals(100.0, firstDeposit.getAmount().doubleValue());
    }

    @Test
    @DisplayName("Should throw exception when file does not exist")
    void should_ThrowException_When_FileDoesNotExist() {
        // Given
        TypeReference<List<User>> typeReference = new TypeReference<>() {
        };

        // When
        Executable executable = () -> jsonFileReader.readJsonFile(jsonFakeFilePath, typeReference);

        // Then
        assertThrows(RuntimeException.class, executable);
    }
}

