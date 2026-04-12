package br.com.ipet.ordering.application.customer.management;

import br.com.ipet.ordering.application.commons.AddressData;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.document.DocumentFactory;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Phone;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;
import br.com.ipet.ordering.domain.model.commons.valueobject.ZipCode;
import br.com.ipet.ordering.domain.model.customer.CustomerAddressId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.customer.CustomerNotFoundException;
import br.com.ipet.ordering.domain.model.customer.CustomerRegistrationService;
import br.com.ipet.ordering.domain.model.customer.Customers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerManagementApplicationService {

    private final Customers customers;
    private final CustomerRegistrationService customerRegistrationService;
    private final DocumentFactory documentFactory;

    public UUID create(CustomerInput input) {
        FieldValidator.requiresNonNull("input", input);

        var address = input.getAddressData();

        var customer = customerRegistrationService.register(
                new FullName(input.getFirstName(), input.getLastName()),
                new Email(input.getEmail()),
                new Phone(input.getPhone()),
                documentFactory.from(input.getDocument()),
                this.toAddress(address)
        );

        customers.add(customer);

        return customer.id().value();
    }

    public UUID addAddress(UUID customerId, AddressData address, boolean isDeliveryAddress) {
        FieldValidator.requiresNonNull("customerId", customerId);
        FieldValidator.requiresNonNull("address", address);

        var customer = customers.ofId(new CustomerId(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        var customerAddressId = customer.addAddress(this.toAddress(address), isDeliveryAddress);

        customers.add(customer);

        return customerAddressId.value();
    }

    public void removeAddress(UUID customerId, UUID addressId) {
        FieldValidator.requiresNonNull("customerId", customerId);
        FieldValidator.requiresNonNull("addressId", addressId);

        var customer = customers.ofId(new CustomerId(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        customer.removeAddress(new CustomerAddressId(addressId));

        customers.add(customer);
    }

    public void update(UUID customerId, CustomerUpdateInput input) {
        FieldValidator.requiresNonNull("customerId", customerId);
        FieldValidator.requiresNonNull("input", input);

        var customer = customers.ofId(new CustomerId(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        customer.changeName(new FullName(input.getFirstName(), input.getLastName()));
        customer.changeDocument(documentFactory.from(input.getDocument()));
        customer.changePhone(new Phone(input.getPhone()));

        customers.add(customer);
    }

    public void changeEmail(String newEmail, UUID customerId) {
        FieldValidator.requiresNonNull("email", newEmail);
        FieldValidator.requiresNonNull("customerId", customerId);

        var customer = customers.ofId(new CustomerId(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        customerRegistrationService.changeEmail(customer, new Email(newEmail));

        customers.add(customer);
    }

    public void changeAddress(UUID customerId, UUID addressId, AddressData address, boolean isDeliveryAddress) {
        FieldValidator.requiresNonNull("customerId", customerId);
        FieldValidator.requiresNonNull("addressId", addressId);
        FieldValidator.requiresNonNull("address", address);
        FieldValidator.requiresNonNull("isDeliveryAddress", isDeliveryAddress);

        var customer = customers.ofId(new CustomerId(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        customer.changeAddress(new CustomerAddressId(addressId), this.toAddress(address), isDeliveryAddress);

        customers.add(customer);
    }

    private Address toAddress(AddressData address) {
        return Address.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .complement(address.getComplement())
                .zipCode(new ZipCode(address.getZipCode()))
                .build();
    }

}
