package br.com.ipet.ordering.infrastructure.persistence.customer;

import br.com.ipet.ordering.application.customer.query.CustomerDetailOutput;
import br.com.ipet.ordering.application.customer.query.CustomerFilter;
import br.com.ipet.ordering.application.customer.query.CustomerQueryService;
import br.com.ipet.ordering.application.customer.query.CustomerSummaryOutput;
import br.com.ipet.ordering.application.util.Mapper;
import br.com.ipet.ordering.domain.model.customer.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceSpecification.email;
import static br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceSpecification.firstName;
import static br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceSpecification.lastName;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerQueryServiceImpl implements CustomerQueryService {

    private final CustomerPersistenceRepository repository;
    private final Mapper mapper;

    @Override
    public CustomerDetailOutput findById(UUID customerId) {
        var customer = repository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException(""));
        return mapper.convert(customer, CustomerDetailOutput.class);
    }

    @Override
    public Page<CustomerSummaryOutput> filter(CustomerFilter filter, Pageable pageable) {
        var customerPage = repository.findAll(toSpecification(filter), pageable);
        return customerPage.map(customer ->
                mapper.convert(customer, CustomerSummaryOutput.class));
    }

    private Specification<CustomerPersistenceEntity> toSpecification(CustomerFilter filter) {
        return firstName(filter.getFirstName())
                .or(lastName(filter.getLastName()))
                .or(email(filter.getEmail()));
    }
}
