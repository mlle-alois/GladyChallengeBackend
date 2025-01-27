package challenge.backend.service;

import challenge.backend.dto.DepositDto;
import challenge.backend.model.Company;
import challenge.backend.model.Deposit;
import challenge.backend.model.User;
import challenge.backend.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final UserService userService;

    @Override
    public void save(Company company) {
        companyRepository.save(company);
    }

    @Override
    public void distributeDeposit(UUID companyId, DepositDto depositDto) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        User user = userService.getUserById(depositDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Deposit deposit = Deposit.create(
                company.getId(),
                depositDto.type(),
                depositDto.amount(),
                depositDto.distributionDate()
        );

        company.distributeDeposit(deposit);
        user.addDeposit(deposit);

        this.save(company);
        userService.save(user);
    }
}
