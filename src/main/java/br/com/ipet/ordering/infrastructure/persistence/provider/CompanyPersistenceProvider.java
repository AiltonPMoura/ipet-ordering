package br.com.ipet.ordering.infrastructure.persistence.provider;

import br.com.ipet.ordering.domain.model.entity.Company;
import br.com.ipet.ordering.domain.model.repository.Companies;
import br.com.ipet.ordering.domain.model.valueobject.CompanyId;
import br.com.ipet.ordering.infrastructure.persistence.mapper.CompanyMapper;
import br.com.ipet.ordering.infrastructure.persistence.repository.CompanyPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CompanyPersistenceProvider implements Companies {

    private final CompanyPersistenceEntityRepository repository;
    private final CompanyMapper mapper;

    @Override
    public Optional<Company> ofId(CompanyId companyId) {
        var companyPersistenceEntity = repository.findById(companyId.value()).orElseThrow();
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
}
