package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.commons.Document;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.CelPhone;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final Customers customers;

    public Customer register(FullName fullName, Email email,
                             CelPhone celPhone, Document document, Address address) {

        var customer = Customer.createNew()
                .fullName(fullName)
                .email(email)
                .document(document)
                .celPhone(celPhone)
                .address(address)
                .build();

        verifyIfEmailIsUnique(customer.email(), customer.id());

        return customer;
    }

    public void changeEmail(Customer customer, Email newEmail) {
        verifyIfEmailIsUnique(newEmail, customer.id());
        customer.changeEmail(newEmail);
    }

    private void verifyIfEmailIsUnique(Email email, CustomerId customerId) {
        if (!customers.isEmailUnique(email, customerId))
            throw new CustomerEmailInUseException();
    }
}
