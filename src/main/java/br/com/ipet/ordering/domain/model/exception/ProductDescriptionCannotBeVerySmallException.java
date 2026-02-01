package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class ProductDescriptionCannotBeVerySmallException extends DomainException {

    public ProductDescriptionCannotBeVerySmallException() {
        super(MessageCode.ERROR_PRODUCT_DESCRIPTION_CANNOT_BE_VERY_SMALL);
    }
}
