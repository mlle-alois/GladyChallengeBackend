package challenge.backend.service;

import challenge.backend.dto.DepositDto;
import challenge.backend.model.Company;
import challenge.backend.model.Deposit;
import challenge.backend.model.User;
import challenge.backend.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static challenge.backend.model.DepositType.GIFT;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {
    @InjectMocks
    private CompanyServiceImpl companyService;

    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private UserService userService;

    private UUID companyId;
    private UUID userId;
    private Company company;
    private User user;

    @BeforeEach
    void setUp() {
        companyId = UUID.randomUUID();
        userId = UUID.randomUUID();
        company = Company.builder()
                .id(companyId)
                .name("Test Company")
                .balance(BigDecimal.valueOf(1000.0))
                .build();
        user = User.builder()
                .id(userId)
                .name("John Doe")
                .deposits(new ArrayList<>())
                .build();
    }

    @Nested
    @DisplayName("Tests for save method")
    class SaveTests {
        @Test
        @DisplayName("Should save company")
        void should_SaveCompany() {
            // given
            doNothing().when(companyRepository).save(company);

            // when
            companyService.save(company);

            // then
            verify(companyRepository, times(1)).save(company);
        }
    }

    @Nested
    @DisplayName("Tests for distributeDeposit method")
    class DistributeDepositTests {
        @Test
        @DisplayName("Should distribute deposit to user correctly")
        void should_DistributeDepositCorrectly() {
            // given
            BigDecimal amount = BigDecimal.valueOf(100);
            DepositDto depositDto = DepositDto.builder()
                    .userId(userId)
                    .type(GIFT)
                    .amount(amount)
                    .distributionDate(LocalDate.now())
                    .build();
            Deposit deposit = Deposit.create(companyId, GIFT, amount, LocalDate.now());

            int expectedSize = 1;
            BigDecimal expectedBalance = BigDecimal.valueOf(900.0);

            when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
            when(userService.getUserById(userId)).thenReturn(Optional.of(user));

            // when
            companyService.distributeDeposit(companyId, depositDto);

            // then
            assertAll(
                    () -> verify(companyRepository, times(1)).findById(companyId),
                    () -> verify(userService, times(1)).getUserById(userId),
                    () -> verify(companyRepository, times(1)).save(company),
                    () -> verify(userService, times(1)).save(user),
                    () -> assertEquals(expectedBalance, company.getBalance()),
                    () -> assertEquals(expectedSize, user.getDeposits().size()),
                    () -> {
                        Deposit actualDeposit = user.getDeposits().getFirst();
                        assertEquals(deposit.getDepositType(), actualDeposit.getDepositType());
                        assertEquals(deposit.getAmount(), actualDeposit.getAmount());
                        assertEquals(deposit.getDistributionDate(), actualDeposit.getDistributionDate());
                    }
            );
        }

        @Test
        @DisplayName("Should throw exception when company not found")
        void should_ThrowException_When_CompanyNotFound() {
            // given
            DepositDto depositDto = DepositDto.builder()
                    .userId(userId)
                    .type(GIFT)
                    .amount(BigDecimal.valueOf(100))
                    .distributionDate(LocalDate.now())
                    .build();

            when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

            // when
            Executable executable = () -> companyService.distributeDeposit(companyId, depositDto);

            // then
            assertThrows(IllegalArgumentException.class, executable);
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void should_ThrowException_When_UserNotFound() {
            // given
            DepositDto depositDto = DepositDto.builder()
                    .userId(userId)
                    .type(GIFT)
                    .amount(BigDecimal.valueOf(100))
                    .distributionDate(LocalDate.now())
                    .build();

            when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
            when(userService.getUserById(userId)).thenReturn(Optional.empty());

            // when
            Executable executable = () -> companyService.distributeDeposit(companyId, depositDto);

            // then
            assertThrows(IllegalArgumentException.class, executable);
        }
    }
}
