package br.com.ipet.ordering.application.customer.management;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.Address;
import br.com.ipet.ordering.domain.model.commons.CelPhone;
import br.com.ipet.ordering.domain.model.commons.Email;
import br.com.ipet.ordering.domain.model.commons.FullName;
import br.com.ipet.ordering.domain.model.commons.ZipCode;
import br.com.ipet.ordering.domain.model.customer.Cpf;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.customer.CustomerNotFoundException;
import br.com.ipet.ordering.domain.model.customer.CustomerService;
import br.com.ipet.ordering.domain.model.customer.Customers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CustomerApplicationService {

    private CustomerService customerService;
    private Customers customers;

    public UUID create(CustomerInput input) {
        FieldValidator.requiresNonNull("customer input", input);

        var address = input.getAddressData();

        var customer = customerService.register(
                new FullName(input.getFirstName(), input.getLastName()),
                new Email(input.getEmail()),
                new CelPhone(input.getCelPhone()),
                new Cpf(input.getCpf()),
                Address.builder()
                        .street(address.getStreet())
                        .number(address.getNumber())
                        .neighborhood(address.getNeighborhood())
                        .city(address.getCity())
                        .state(address.getState())
                        .complement(address.getComplement())
                        .zipCode(new ZipCode(address.getZipCode()))
                        .build()
        );

        customers.add(customer);

        return customer.id().value();
    }

    public void update(UUID customerId, CustomerUpdateInput input) {
        FieldValidator.requiresNonNull("customerId", customerId);
        FieldValidator.requiresNonNull("customer update input", input);

        var customer = customers.ofId(new CustomerId(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        customer.changeName(new FullName(input.getFirstName(), input.getLastName()));
        customer.changeCpf(new Cpf(input.getCpf()));
        customer.changeCelPhone(new CelPhone(input.getCelPhone()));

        var address = input.getAddressData();
        customer.changeAddress(Address.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .complement(address.getComplement())
                .zipCode(new ZipCode(address.getZipCode()))
                .build());

        customers.add(customer);
    }

    public void changeEmail(String newEmail, UUID customerId) {
        FieldValidator.requiresNonNull("email", newEmail);
        FieldValidator.requiresNonNull("customerId", customerId);

        var customer = customers.ofId(new CustomerId(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        customerService.changeEmail(customer, new Email(newEmail));

        customers.add(customer);
    }

}
