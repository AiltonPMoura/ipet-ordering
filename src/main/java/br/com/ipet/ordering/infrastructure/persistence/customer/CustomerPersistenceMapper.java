package br.com.ipet.ordering.infrastructure.persistence.customer;

import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.customer.Customer;
import br.com.ipet.ordering.domain.model.customer.CustomerAddress;
import br.com.ipet.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomerPersistenceMapper {

    public CustomerPersistenceEntity fromDomain(Customer customer) {
        return this.merge(new CustomerPersistenceEntity(), customer);
    }

    public CustomerPersistenceEntity merge(CustomerPersistenceEntity customerPersistence, Customer customer) {
        customerPersistence.setId(customer.id().value());
        customerPersistence.setFirstName(customer.fullName().firstName());
        customerPersistence.setLastName(customer.fullName().lastName());
        customerPersistence.setEmail(customer.email().value());
        customerPersistence.setDocument(customer.document().value());
        customerPersistence.setBirthDate(customer.birthDate().value());
        customerPersistence.setPhone(customer.phone().value());
        customerPersistence.setCustomerAddresses(this.mergeAddresses(customerPersistence, customer));
        customerPersistence.setRegisterAt(customer.registeredAt());
        customerPersistence.addEvents(customer.domainEvents());
        return customerPersistence;
    }

    private Set<CustomerAddressPersistenceEntity> mergeAddresses(CustomerPersistenceEntity customerPersistence, Customer customer) {
        var customerAddressesPersistence = customerPersistence.getCustomerAddresses();
        var customerAddresses = customer.customerAddresses();

        if (customerAddressesPersistence.isEmpty())
            return customerAddresses.stream().map(this::fromDomainCustomerAddress)
                    .collect(Collectors.toSet());

        var customerAddressPersistenceMap = customerAddressesPersistence.stream()
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
