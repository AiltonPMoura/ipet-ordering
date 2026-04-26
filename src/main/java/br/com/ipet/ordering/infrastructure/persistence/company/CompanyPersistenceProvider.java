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

    private final CompanyPersistenceRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final CompanyPersistenceMapper companyPersistenceMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> ofId(CompanyId companyId) {
        return companyRepository.findById(companyId.value()).map(companyMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(CompanyId companyId) {
        return companyRepository.existsById(companyId.value());
    }

    @Override
    @Transactional
    public void add(Company company) {
        companyRepository.findById(company.id().value())
                .ifPresentOrElse(companyPersistence ->
                        this.update(companyPersistence, company),
                        () -> this.insert(company));
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return companyRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailUnique(Email email, CompanyId companyId) {
        return !companyRepository.existsByEmailAndIdNot(email.value(), companyId.value());
    }

    private void insert(Company company) {
        var companyPersistence = companyPersistenceMapper.fromDomain(company);
        companyRepository.saveAndFlush(companyPersistence);
    }

    private void update(CompanyPersistenceEntity companyPersistence, Company company) {
        companyPersistence = companyPersistenceMapper.merge(companyPersistence, company);
        companyRepository.saveAndFlush(companyPersistence);
    }
}
