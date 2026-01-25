package br.com.ipet.ordering.application.model.company;

import br.com.ipet.ordering.domain.model.exception.DomainException;

public class CompanyNotFoundException extends DomainException {
    public CompanyNotFoundException() {
        super("message");
    }
}
