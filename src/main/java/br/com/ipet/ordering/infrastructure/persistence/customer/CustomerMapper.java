package br.com.ipet.ordering.infrastructure.persistence.customer;

import br.com.ipet.ordering.domain.model.customer.BirthDate;
import br.com.ipet.ordering.domain.model.customer.Customer;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Phone;
import br.com.ipet.ordering.domain.model.customer.CustomerAddress;
import br.com.ipet.ordering.domain.model.customer.CustomerAddressId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;
import br.com.ipet.ordering.domain.model.commons.valueobject.ZipCode;
import br.com.ipet.ordering.domain.model.commons.document.DocumentFactory;
import br.com.ipet.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerMapper {

    public Customer toDomain(CustomerPersistenceEntity persistenceEntity) {
        return Customer.existing()
                .id(new CustomerId(persistenceEntity.getId()))
                .fullName(this.toFullName(persistenceEntity))
                .email(new Email(persistenceEntity.getEmail()))
                .phone(new Phone(persistenceEntity.getPhone()))
                .document(DocumentFactory.from(persistenceEntity.getDocument()))
                .birthDate(new BirthDate(persistenceEntity.getBirthDate()))
                .address(this.toCustomerAddress(persistenceEntity.getCustomerAddress()))
                .registerAt(persistenceEntity.getRegisterAt())
                .build();
    }

    private FullName toFullName(CustomerPersistenceEntity persistenceEntity) {
        return FullName.builder()
                .firstName(persistenceEntity.getFirstName())
                .lastName(persistenceEntity.getLastName())
                .build();
    }

    private Set<CustomerAddress> toCustomerAddress(Set<CustomerAddressPersistenceEntity> customerAddressPersistence) {
        return customerAddressPersistence.stream()
                .map(customerAddress -> CustomerAddress.existing()
                        .id(new CustomerAddressId(customerAddress.getId()))
                        .customerId(new CustomerId(customerAddress.getCustomerId()))
                        .address(toAddress(customerAddress.getAddress()))
                        .isPrincipal(customerAddress.isPrincipal())
                        .build()
                ).collect(Collectors.toSet());
    }

    private Address toAddress(AddressEmbeddable addressEmbeddable) {
        return Address.builder()
                .street(addressEmbeddable.getStreet())
                .number(addressEmbeddable.getNumber())
                .neighborhood(addressEmbeddable.getNeighborhood())
                .city(addressEmbeddable.getCity())
                .state(addressEmbeddable.getState())
                .complement(addressEmbeddable.getComplement())
                .zipCode(new ZipCode(addressEmbeddable.getZipCode()))
                .build();
    }

}
