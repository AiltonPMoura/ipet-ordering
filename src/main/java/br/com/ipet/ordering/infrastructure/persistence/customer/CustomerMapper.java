package br.com.ipet.ordering.infrastructure.persistence.customer;

import br.com.ipet.ordering.domain.model.customer.Customer;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.CelPhone;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.customer.Cpf;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;
import br.com.ipet.ordering.domain.model.commons.valueobject.ZipCode;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toDomainEntity(CustomerPersistenceEntity persistenceEntity) {
        return Customer.existing()
                .id(new CustomerId(persistenceEntity.getId()))
                .fullName(this.toFullName(persistenceEntity))
                .email(new Email(persistenceEntity.getEmail()))
                .celPhone(new CelPhone(persistenceEntity.getCelPhone()))
                .document(new Cpf(persistenceEntity.getDocument()))
                .address(this.toAddress(persistenceEntity))
                .build();
    }

    private FullName toFullName(CustomerPersistenceEntity persistenceEntity) {
        return FullName.builder()
                .firstName(persistenceEntity.getFirstName())
                .lastName(persistenceEntity.getLastName())
                .build();
    }

    private Address toAddress(CustomerPersistenceEntity persistenceEntity) {
        var address = persistenceEntity.getAddress();
        return Address.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .complement(address.getComplement())
                .zipCode(new ZipCode(address.getZipCode()))
                .build();
    }

}
