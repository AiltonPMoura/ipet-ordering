package br.com.ipet.ordering.infrastructure.persistence.provider;

import br.com.ipet.ordering.domain.model.entity.Customer;
import br.com.ipet.ordering.domain.model.repository.Customers;
import br.com.ipet.ordering.domain.model.valueobject.CustumerId;
import br.com.ipet.ordering.infrastructure.persistence.mapper.CustomerMapper;
import br.com.ipet.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerPersistenceProvider implements Customers {

    private final CustomerPersistenceEntityRepository repository;
    private final CustomerMapper customerMapper;

    @Override
    public Optional<Customer> ofId(CustumerId custumerId) {
        var customerPersistenceEntity = repository.findById(custumerId.value()).orElseThrow();
        return customerMapper.toDomainEntity(customerPersistenceEntity);
    }

    @Override
    public boolean exists(CustumerId custumerId) {
        return false;
    }

    @Override
    public void add(Customer aggregateRoot) {

    }

    @Override
    public int count() {
        return 0;
    }
}
