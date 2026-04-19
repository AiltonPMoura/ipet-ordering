package br.com.ipet.ordering.domain.model.company;

import br.com.ipet.ordering.domain.model.DomainException;

public class CompanyNotFoundException extends DomainException {
    public CompanyNotFoundException(String message) {
        super(message);
    }
}
