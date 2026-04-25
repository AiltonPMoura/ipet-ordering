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

    private final CustomerPersistenceRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CustomerPersistenceMapper customerPersistenceMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> ofId(CustomerId customerId) {
        return customerRepository.findById(customerId.value()).map(customerMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(CustomerId customerId) {
        return customerRepository.existsById(customerId.value());
    }

    @Override
    @Transactional
    public void add(Customer customer) {
        customerRepository.findById(customer.id().value())
                .ifPresentOrElse(
                        customerPersistence -> this.update(customerPersistence, customer),
                        () -> this.insert(customer)
                );
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return customerRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailUnique(Email email, CustomerId customerId) {
        return !customerRepository.existsByEmailAndIdNot(email.value(), customerId.value());
    }

    private void insert(Customer customer) {
        var customerPersistence = customerPersistenceMapper.fromDomain(customer);
        customerRepository.saveAndFlush(customerPersistence);
    }

    private void update(CustomerPersistenceEntity customerPersistence, Customer customer) {
        customerPersistence = customerPersistenceMapper.merge(customerPersistence, customer);
        customerRepository.saveAndFlush(customerPersistence);
    }

}
