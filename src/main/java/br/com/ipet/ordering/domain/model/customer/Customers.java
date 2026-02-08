package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.Repository;
import br.com.ipet.ordering.domain.model.commons.Email;

public interface Customers extends Repository<Customer, CustomerId> {
    boolean isEmailUnique(Email email, CustomerId customerId);
}
