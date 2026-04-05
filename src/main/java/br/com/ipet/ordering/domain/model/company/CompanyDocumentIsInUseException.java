package br.com.ipet.ordering.domain.model.company;

import br.com.ipet.ordering.domain.model.DomainException;

public class CompanyDocumentIsInUseException extends DomainException {
    public CompanyDocumentIsInUseException() {
        super("message");
    }
}
