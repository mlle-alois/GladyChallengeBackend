package challenge.backend.controller;

import challenge.backend.exception.IllegalDepositAmountException;
import challenge.backend.model.DepositType;
import challenge.backend.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CompanyController companyController;

    @Mock
    private CompanyService companyService;

    private UUID companyId;
    private String depositDtoJson;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();
        companyId = UUID.randomUUID();
        depositDtoJson = """
                {
                    "userId": "%s",
                    "depositType": "%s",
                    "amount": %s,
                    "distributionDate": "%s"
                }
                """.formatted(
                UUID.randomUUID(),
                DepositType.GIFT,
                BigDecimal.valueOf(500.0),
                LocalDate.now());

    }

    @Test
    @DisplayName("Should return Ok when deposit is distributed successfully")
    void should_ReturnOk_When_DepositDistributedSuccessfully() throws Exception {
        // Given
        doNothing().when(companyService).distributeDeposit(eq(companyId), any());

        // When
        mockMvc.perform(post("/company/{companyId}/distribute", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositDtoJson))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value("Deposit distributed successfully."));
    }

    @Test
    @DisplayName("Should return BadRequest when deposit fails")
    void should_ReturnBadRequest_When_DepositFails() throws Exception {
        // Given
        doThrow(new IllegalDepositAmountException("Insufficient balance for the deposit.")).when(companyService).distributeDeposit(eq(companyId), any());

        // When
        mockMvc.perform(post("/company/{companyId}/distribute", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositDtoJson))
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Insufficient balance for the deposit."));
    }
}
