package challenge.backend.service;

import challenge.backend.dto.DepositDto;
import challenge.backend.model.Company;

import java.util.UUID;

public interface CompanyService {
    void save(Company company);
    void distributeDeposit(UUID companyId, DepositDto depositDto);
}
