package br.com.ipet.ordering.infrastructure.persistence.customer;

import br.com.ipet.ordering.application.customer.query.CustomerAddressOutput;
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

import java.util.List;
import java.util.UUID;

import static br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceSpecification.emailLike;
import static br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceSpecification.firstNameLike;
import static br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceSpecification.lastNameLike;

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
        return repository.findAll(toSpecification(filter), pageable)
                .map(customerPersistenceEntity -> mapper.convert(customerPersistenceEntity, CustomerSummaryOutput.class));
    }

    @Override
    public List<CustomerAddressOutput> findAddressesByCustomerId(UUID customerId) {
        return repository.findAddressesByCustomerId(customerId);
    }

    private Specification<CustomerPersistenceEntity> toSpecification(CustomerFilter filter) {
        return firstNameLike(filter.getFirstName())
                .or(lastNameLike(filter.getLastName()))
                .or(emailLike(filter.getEmail()));
    }
}
