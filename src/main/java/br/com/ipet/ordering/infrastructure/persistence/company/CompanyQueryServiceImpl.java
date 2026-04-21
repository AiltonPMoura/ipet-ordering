package br.com.ipet.ordering.infrastructure.persistence.company;

import br.com.ipet.ordering.application.company.query.CompanyDetailOutput;
import br.com.ipet.ordering.application.company.query.CompanyFilter;
import br.com.ipet.ordering.application.company.query.CompanyQueryService;
import br.com.ipet.ordering.application.company.query.CompanySummaryOutput;
import br.com.ipet.ordering.application.util.Mapper;
import br.com.ipet.ordering.domain.model.company.CompanyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static br.com.ipet.ordering.infrastructure.persistence.company.CompanyPersistenceSpecification.email;
import static br.com.ipet.ordering.infrastructure.persistence.company.CompanyPersistenceSpecification.name;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyQueryServiceImpl implements CompanyQueryService {

    private final CompanyPersistenceRepository repository;
    private final Mapper mapper;

    @Override
    public CompanyDetailOutput findById(UUID companyId) {
        var company = repository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException(""));
        return mapper.convert(company, CompanyDetailOutput.class);
    }

    @Override
    public Page<CompanySummaryOutput> filter(CompanyFilter filter, Pageable pageable) {
        return repository.findAll(toSpecification(filter), pageable)
                .map(company -> mapper.convert(company, CompanySummaryOutput.class));
    }

    private Specification<CompanyPersistenceEntity> toSpecification(CompanyFilter filter) {
        return name(filter.getCompanyName()).or(email(filter.getEmail()));
    }
}
