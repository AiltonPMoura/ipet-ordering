package br.com.ipet.ordering.application.customer.management;

import br.com.ipet.ordering.application.commons.AddressData;
import br.com.ipet.ordering.application.commons.AddressMapper;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.document.DocumentFactory;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;
import br.com.ipet.ordering.domain.model.commons.valueobject.Phone;
import br.com.ipet.ordering.domain.model.customer.BirthDate;
import br.com.ipet.ordering.domain.model.customer.Customer;
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
    private final AddressMapper addressMapper;

    public UUID create(CustomerInput input) {
        FieldValidator.requiresNonNull("input", input);

        var customer = customerRegistrationService.register(
                new FullName(input.getFirstName(), input.getLastName()),
                new Email(input.getEmail()),
                new Phone(input.getPhone()),
                DocumentFactory.from(input.getDocument()),
                new BirthDate(input.getBirthDate()),
                addressMapper.toAddress(input.getAddress())
        );

        customers.add(customer);

        return customer.id().value();
    }

    public UUID addAddress(UUID customerId, AddressData address) {
        FieldValidator.requiresNonNull("customerId", customerId);
        FieldValidator.requiresNonNull("address", address);

        var customer = this.findById(customerId);

        var customerAddressId = customer.addAddress(addressMapper.toAddress(address));

        customers.add(customer);

        return customerAddressId.value();
    }

    public void removeAddress(UUID customerId, UUID addressId) {
        FieldValidator.requiresNonNull("customerId", customerId);
        FieldValidator.requiresNonNull("addressId", addressId);

        var customer = this.findById(customerId);

        customer.removeAddress(new CustomerAddressId(addressId));

        customers.add(customer);
    }

    public void update(UUID customerId, CustomerUpdateInput input) {
        FieldValidator.requiresNonNull("customerId", customerId);
        FieldValidator.requiresNonNull("input", input);

        var customer = this.findById(customerId);

        customer.changeName(new FullName(input.getFirstName(), input.getLastName()));
        customer.changeDocument(DocumentFactory.from(input.getDocument()));
        customer.changePhone(new Phone(input.getPhone()));
        customer.changeBirthDate(new BirthDate(input.getBirthDate()));

        customers.add(customer);
    }

    public void changeEmail(String newEmail, UUID customerId) {
        FieldValidator.requiresNonNull("email", newEmail);
        FieldValidator.requiresNonNull("customerId", customerId);

        var customer = this.findById(customerId);

        customerRegistrationService.changeEmail(customer, new Email(newEmail));

        customers.add(customer);
    }

    public void changeAddress(UUID customerId, UUID addressId, AddressData address) {
        FieldValidator.requiresNonNull("customerId", customerId);
        FieldValidator.requiresNonNull("addressId", addressId);
        FieldValidator.requiresNonNull("address", address);

        var customer = this.findById(customerId);

        customer.changeAddress(new CustomerAddressId(addressId), addressMapper.toAddress(address));

        customers.add(customer);
    }

    public void changePrincipalAddress(UUID customerId, UUID addressId) {
        FieldValidator.requiresNonNull("customerId", customerId);
        FieldValidator.requiresNonNull("addressId", addressId);

        var customer = this.findById(customerId);

        customer.changePrincipalAddress(new CustomerAddressId(addressId));

        customers.add(customer);
    }

    private Customer findById(UUID customerId) {
        return customers.ofId(new CustomerId(customerId))
                .orElseThrow(() -> new CustomerNotFoundException(""));
    }

}
