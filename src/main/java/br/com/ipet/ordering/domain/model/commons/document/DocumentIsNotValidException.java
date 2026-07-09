package br.com.ipet.ordering.domain.model.commons.document;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class DocumentIsNotValidException extends DomainException {
    public DocumentIsNotValidException() {
        super(MessageCode.INVALID_DOCUMENT);
    }

    public DocumentIsNotValidException(String value) {
        super(MessageCode.INVALID_FORMAT_DOCUMENT, value);
    }
}
