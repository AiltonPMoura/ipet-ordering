package br.com.ipet.ordering.infrastructure.persistence.customer;

import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.customer.Customer;
import br.com.ipet.ordering.domain.model.customer.CustomerAddress;
import br.com.ipet.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CustomerPersistenceMapper {

    public CustomerPersistenceEntity fromDomain(Customer customer) {
        return merge(new CustomerPersistenceEntity(), customer);
    }

    public CustomerPersistenceEntity merge(CustomerPersistenceEntity customerPersistence, Customer customer) {
        customerPersistence.setId(customer.id().value());
        customerPersistence.setFirstName(customer.fullName().firstName());
        customerPersistence.setLastName(customer.fullName().lastName());
        customerPersistence.setEmail(customer.email().value());
        customerPersistence.setDocument(customer.document().value());
        customerPersistence.setBirthDate(customer.birthDate().value());
        customerPersistence.setPhone(customer.phone().value());
        customerPersistence.setAddress(this.mergeAddress(customerPersistence, customer));
        customerPersistence.setRegisterAt(customer.registerAt());
        return customerPersistence;
    }

    private Set<CustomerAddressPersistenceEntity> mergeAddress(CustomerPersistenceEntity customerPersistence, Customer customer) {
        var customerAddressPersistence = customerPersistence.getCustomerAddress();
        var customerAddresses = customer.customerAddresses();

        if (customerAddressPersistence.isEmpty())
            return customerAddresses.stream().map(this::fromDomainCustomerAddress)
                    .collect(Collectors.toSet());

        var customerAddressPersistenceMap = customerAddressPersistence.stream()
                .collect(Collectors.toMap(CustomerAddressPersistenceEntity::getId, address -> address));

        return customerAddresses.stream().map(customerAddress -> {
            var addressPersistence = customerAddressPersistenceMap.getOrDefault(customerAddress.id().value(), new CustomerAddressPersistenceEntity());
            return mergeAddress(addressPersistence, customerAddress);
        }).collect(Collectors.toSet());
    }

    private CustomerAddressPersistenceEntity fromDomainCustomerAddress(CustomerAddress customerAddress) {
        return mergeAddress(new CustomerAddressPersistenceEntity(), customerAddress);
    }

    private CustomerAddressPersistenceEntity mergeAddress(CustomerAddressPersistenceEntity customerAddressPersistence,
                                                          CustomerAddress customerAddress) {
        customerAddressPersistence.setId(customerAddress.id().value());
        customerAddressPersistence.setAddress(this.toAddressEmbeddable(customerAddress.address()));
        customerAddressPersistence.setPrincipal(customerAddress.isPrincipal());
        return customerAddressPersistence;
    }

    private AddressEmbeddable toAddressEmbeddable(Address address) {
        return AddressEmbeddable.builder()
                .street(address.street())
                .number(address.number())
                .neighborhood(address.neighborhood())
                .city(address.city())
                .state(address.state())
                .complement(address.complement())
                .zipCode(address.zipCode().value())
                .build();
    }

}
