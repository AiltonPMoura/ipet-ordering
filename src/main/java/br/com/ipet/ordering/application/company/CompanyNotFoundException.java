package br.com.ipet.ordering.application.company;

import br.com.ipet.ordering.domain.model.DomainException;

public class CompanyNotFoundException extends DomainException {
    public CompanyNotFoundException() {
        super("message");
    }
}
