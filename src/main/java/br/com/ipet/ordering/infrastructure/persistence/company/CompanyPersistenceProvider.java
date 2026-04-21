package br.com.ipet.ordering.infrastructure.persistence.company;

import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CompanyPersistenceProvider implements Companies {

    private final CompanyPersistenceRepository repository;
    private final CompanyMapper companyMapper;
    private final CompanyPersistenceMapper companyPersistenceMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> ofId(CompanyId companyId) {
        return repository.findById(companyId.value()).map(companyMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(CompanyId companyId) {
        return repository.existsById(companyId.value());
    }

    @Override
    @Transactional
    public void add(Company company) {
        repository.findById(company.id().value())
                .ifPresentOrElse(companyPersistence ->
                        update(companyPersistence, company),
                        () -> insert(company));
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailUnique(Email email, CompanyId companyId) {
        return !repository.existsByEmailAndIdNot(email.value(), companyId.value());
    }

    private void insert(Company company) {
        var companyPersistence = companyPersistenceMapper.fromDomain(company);
        repository.saveAndFlush(companyPersistence);
    }

    private void update(CompanyPersistenceEntity companyPersistence, Company company) {
        companyPersistence = companyPersistenceMapper.merge(companyPersistence, company);
        repository.saveAndFlush(companyPersistence);
    }
}
