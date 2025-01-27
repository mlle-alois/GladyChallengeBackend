package challenge.backend.repository;

import challenge.backend.model.Company;
import challenge.backend.util.JsonFileReader;
import challenge.backend.util.JsonFileWriter;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class CompanyRepository {
    private final JsonFileReader jsonFileReader;
    private final JsonFileWriter jsonFileWriter;

    @Value("${data.company.json.file}")
    private String jsonFilePath;

    public Optional<Company> findById(UUID companyId) {
        TypeReference<List<Company>> companyType = new TypeReference<>() {};

        Optional<List<Company>> foundCompanies = jsonFileReader.readJsonFile(jsonFilePath, companyType);

        return foundCompanies.flatMap(companies -> companies.stream()
                .filter(company -> company.getId().equals(companyId))
                .findFirst());
    }

    public void save(Company toSaveCompany) {
        Optional<List<Company>> foundCompanies = jsonFileReader.readJsonFile(jsonFilePath, new TypeReference<>() {});

        List<Company> companies = foundCompanies.orElseGet(List::of);
        companies.removeIf(company -> company.getId().equals(toSaveCompany.getId()));
        companies.add(toSaveCompany);

        jsonFileWriter.writeListToFile(jsonFilePath, companies);
    }
}

