package br.com.ipet.ordering.infrastructure.persistence.customer;

import br.com.ipet.ordering.domain.model.customer.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomerPersistenceMapper {

    public CustomerPersistenceEntity toPersistence(Customer customer) {
        return CustomerPersistenceEntity.builder()
                .id(customer.id().value())
                .build();
    }

}
