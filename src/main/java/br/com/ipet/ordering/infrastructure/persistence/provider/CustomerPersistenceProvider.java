package br.com.ipet.ordering.infrastructure.persistence.provider;

import br.com.ipet.ordering.domain.model.customer.Customer;
import br.com.ipet.ordering.domain.model.customer.Customers;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.infrastructure.persistence.mapper.CustomerMapper;
import br.com.ipet.ordering.infrastructure.persistence.mapper.CustomerPersistenceMapper;
import br.com.ipet.ordering.infrastructure.persistence.repository.CustomerPersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerPersistenceProvider implements Customers {

    private final CustomerPersistenceRepository persistenceRepository;
    private final CustomerMapper customerMapper;
    private final CustomerPersistenceMapper customerPersistenceMapper;

    @Override
    public Optional<Customer> ofId(CustomerId customerId) {
        return persistenceRepository.findById(customerId.value())
                .map(customerMapper::toDomainEntity);
    }

    @Override
    public boolean exists(CustomerId customerId) {
        return persistenceRepository.existsById(customerId.value());
    }

    @Override
    public void add(Customer customer) {
        var customerPersistence = customerPersistenceMapper.toPersistence(customer);
        persistenceRepository.saveAndFlush(customerPersistence);
    }

    @Override
    public int count() {
        return 0;
    }
}
