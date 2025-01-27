package challenge.backend.util;

import challenge.backend.model.Deposit;
import challenge.backend.model.DepositType;
import challenge.backend.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JsonFileWriterTest {

    @Autowired
    private JsonFileWriter jsonFileWriter;

    @Value("${data.fake.write.file}")
    private String jsonFakeWriteFilePath;

    @AfterEach
    void cleanUp() {
        File writtenFile = new File(jsonFakeWriteFilePath);
        if (writtenFile.exists()) {
            writtenFile.delete();
        }
    }

    @Test
    @DisplayName("Should write users to JSON file correctly")
    void should_WriteUsersToJsonFileCorrectly_When_FileExists() {
        // Given
        List<User> users = List.of(
                User.builder()
                        .id(UUID.fromString("7626e4a6-fdfd-44e4-b602-320a4c8aa6fb"))
                        .name("John Doe")
                        .deposits(List.of(
                                Deposit.create(
                                        UUID.fromString("da2dc808-186f-4e59-adfb-d753c9d56a82"),
                                        DepositType.GIFT,
                                        BigDecimal.valueOf(100.0),
                                        LocalDate.of(2025, 1, 1)
                                )
                        ))
                        .build()
        );

        // When
        jsonFileWriter.writeListToFile(jsonFakeWriteFilePath, users);

        // Then
        File writtenFile = new File(jsonFakeWriteFilePath);
        assertTrue(writtenFile.exists());
        assertTrue(writtenFile.length() > 0);
    }
}
