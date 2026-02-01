package br.com.ipet.ordering.domain.model.company;

import br.com.ipet.ordering.domain.model.DomainException;

public class CompanyCnpjIsInUseException extends DomainException {
    public CompanyCnpjIsInUseException() {
        super("message");
    }
}
