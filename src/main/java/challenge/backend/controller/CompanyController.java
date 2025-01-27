package challenge.backend.controller;

import challenge.backend.dto.DepositDto;
import challenge.backend.exception.IllegalDepositAmountException;
import challenge.backend.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/{companyId}/distribute")
    public ResponseEntity<String> distributeDeposit(@PathVariable UUID companyId, @RequestBody DepositDto depositDto) {
        companyService.distributeDeposit(companyId, depositDto);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("Deposit distributed successfully.");
    }

    @ExceptionHandler(IllegalDepositAmountException.class)
    public ResponseEntity<String> handleInvalidDepositAmount(IllegalDepositAmountException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
