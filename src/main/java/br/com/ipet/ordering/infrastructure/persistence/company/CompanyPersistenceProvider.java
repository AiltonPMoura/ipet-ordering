package br.com.ipet.ordering.infrastructure.persistence.company;

import br.com.ipet.ordering.domain.model.commons.document.Document;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CompanyPersistenceProvider implements Companies {

    private final CompanyPersistenceRepository companyPersistenceRepository;
    private final CompanyMapper companyMapper;
    private final CompanyPersistenceMapper companyPersistenceMapper;

    @Override
    public Optional<Company> ofId(CompanyId companyId) {
        return companyPersistenceRepository.findById(companyId.value())
                .map(companyMapper::toDomain);
    }

    @Override
    public boolean exists(CompanyId companyId) {
        return companyPersistenceRepository.existsById(companyId.value());
    }

    @Override
    public void add(Company company) {
        var companyPersistence = companyPersistenceMapper.toPersistence(company);
        companyPersistenceRepository.saveAndFlush(companyPersistence);
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean isEmailUnique(Email email, CompanyId excepedCompanyId) {
        return !companyPersistenceRepository.existsByEmailAndIdNot(email.value(), excepedCompanyId.value());
    }

    @Override
    public boolean isDocumentUnique(Document document, CompanyId excepedCompanyId) {
        return !companyPersistenceRepository.existsByDocumentAndIdNot(document.value(), excepedCompanyId.value());
    }
}
