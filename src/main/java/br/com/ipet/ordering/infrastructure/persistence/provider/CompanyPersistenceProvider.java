package br.com.ipet.ordering.infrastructure.persistence.provider;

import br.com.ipet.ordering.domain.model.commons.Email;
import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.commons.CompanyId;
import br.com.ipet.ordering.infrastructure.persistence.mapper.CompanyMapper;
import br.com.ipet.ordering.infrastructure.persistence.repository.CompanyPersistenceRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CompanyPersistenceProvider implements Companies {

    private final CompanyPersistenceRepository persistenceRepository;
    private final CompanyMapper mapper;

    @Override
    public Optional<Company> ofId(CompanyId companyId) {
        var companyPersistenceEntity = persistenceRepository.findById(companyId.value()).orElseThrow();
        return Optional.ofNullable(mapper.toDomainEntity(companyPersistenceEntity));
    }

    @Override
    public boolean exists(CompanyId companyId) {
        return false;
    }

    @Override
    public void add(Company aggregateRoot) {

    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public boolean isEmailUnique(Email email, CompanyId excepedCompanyId) {
        return !persistenceRepository.existsByEmailAndIdNot(email.value(), excepedCompanyId.value());
    }
}
