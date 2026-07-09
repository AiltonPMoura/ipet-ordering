package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class CustomerEmailIsInUseException extends DomainException {
    public CustomerEmailIsInUseException(String value) {
        super(MessageCode.Customer.EMAIL_IN_USE, value);
    }
}
