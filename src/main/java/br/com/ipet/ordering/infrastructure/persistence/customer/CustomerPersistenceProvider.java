package br.com.ipet.ordering.infrastructure.persistence.customer;

import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.customer.Customer;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.customer.Customers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerPersistenceProvider implements Customers {

    private final CustomerPersistenceRepository repository;
    private final CustomerMapper customerMapper;
    private final CustomerPersistenceMapper customerPersistenceMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> ofId(CustomerId customerId) {
        return repository.findById(customerId.value())
                .map(customerMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(CustomerId customerId) {
        return repository.existsById(customerId.value());
    }

    @Override
    @Transactional
    public void add(Customer customer) {
        repository.findById(customer.id().value())
                .ifPresentOrElse(customerPersistence ->
                        update(customerPersistence, customer),
                        () -> insert(customer));
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailUnique(Email email, CustomerId customerId) {
        return !repository.existsByEmailAndIdNot(email.value(), customerId.value());
    }

    private void insert(Customer customer) {
        var customerPersistence = customerPersistenceMapper.fromDomain(customer);
        repository.saveAndFlush(customerPersistence);
    }

    private void update(CustomerPersistenceEntity customerPersistence, Customer customer) {
        customerPersistence = customerPersistenceMapper.merge(customerPersistence, customer);
        repository.saveAndFlush(customerPersistence);
    }

}
