package br.com.ipet.ordering.infrastructure.persistence.company;

import br.com.ipet.ordering.domain.model.customer.Cnpj;
import br.com.ipet.ordering.domain.model.commons.Email;
import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.customer.CompanyId;
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
        return false;
    }

    @Override
    public void add(Company company) {
        var companyPersistence = companyPersistenceMapper.toPersistence(company);
        companyPersistenceRepository.saveAndFlush(companyPersistence);
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public boolean isEmailUnique(Email email, CompanyId excepedCompanyId) {
        return !companyPersistenceRepository.existsByEmailAndIdNot(email.value(), excepedCompanyId.value());
    }

    @Override
    public boolean isCnpjUnique(Cnpj cnpj, CompanyId excepedCompanyId) {
        return !companyPersistenceRepository.existsByCnpjAndIdNot(cnpj.value(), excepedCompanyId.value());
    }
}
