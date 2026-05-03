package br.com.ipet.ordering.domain.model.commons.document;

import br.com.ipet.ordering.domain.model.DomainException;

public class DocumentIsNotValidException extends DomainException {
    public DocumentIsNotValidException(String message) {
        super(message);
    }
}
