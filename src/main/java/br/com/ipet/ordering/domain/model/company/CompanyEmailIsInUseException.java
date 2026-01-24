package br.com.ipet.ordering.domain.model.company;

import br.com.ipet.ordering.domain.model.exception.DomainException;

public class CompanyEmailIsInUseException extends DomainException {

    public CompanyEmailIsInUseException() {
        super("message");
    }

}
