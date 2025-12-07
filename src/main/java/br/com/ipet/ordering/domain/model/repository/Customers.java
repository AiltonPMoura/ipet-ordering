package br.com.ipet.ordering.domain.model.repository;

import br.com.ipet.ordering.domain.model.entity.Customer;
import br.com.ipet.ordering.domain.model.valueobject.CustumerId;

public interface Customers extends Repository<Customer, CustumerId> {
}
