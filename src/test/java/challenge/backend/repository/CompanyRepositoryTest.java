package challenge.backend.repository;

import challenge.backend.model.Company;
import challenge.backend.util.JsonFileReader;
import challenge.backend.util.JsonFileWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyRepositoryTest {
    @InjectMocks
    private CompanyRepository companyRepository;

    @Mock
    private JsonFileReader jsonFileReader;
    @Mock
    private JsonFileWriter jsonFileWriter;

    private UUID companyId;
    private Company company;

    @BeforeEach
    void setUp() {
        companyId = UUID.randomUUID();
        company = Company.builder()
                .id(companyId)
                .name("Test Company")
                .balance(BigDecimal.valueOf(1000.0))
                .build();
    }

    @Nested
    @DisplayName("Tests for findById method")
    class FindByIdTests {
        @Test
        @DisplayName("Should return company when it's found")
        void should_ReturnCompany_When_Found() {
            // Given
            when(jsonFileReader.readJsonFile(any(), any())).thenReturn(Optional.of(List.of(company)));

            // When
            Optional<Company> foundCompany = companyRepository.findById(companyId);

            // Then
            assertTrue(foundCompany.isPresent());
            assertEquals(companyId, foundCompany.get().getId());
        }

        @Test
        @DisplayName("Should return empty when company not found")
        void should_ReturnEmpty_When_NotFound() {
            // Given
            UUID notExistingId = UUID.randomUUID();

            // When
            Optional<Company> foundCompany = companyRepository.findById(notExistingId);

            // Then
            assertTrue(foundCompany.isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests for save method")
    class SaveTests {
        @Test
        void should_WriteToFile_When_CompanyIsSaved() {
            // Given
            Company company = Company.builder()
                    .id(UUID.randomUUID())
                    .name("Apple")
                    .balance(BigDecimal.valueOf(1000.0))
                    .build();

            when(jsonFileReader.readJsonFile(any(), any())).thenReturn(Optional.of(new ArrayList<>(List.of(company))));

            // When
            companyRepository.save(company);

            // Then
            verify(jsonFileWriter, times(1)).writeListToFile(any(), anyList());
        }
    }
}
