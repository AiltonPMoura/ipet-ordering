package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.exception.message.MessageCode;

public class ProductDescriptionCannotBeVerySmallException extends DomainException {

    public ProductDescriptionCannotBeVerySmallException() {
        super(MessageCode.ERROR_PRODUCT_DESCRIPTION_CANNOT_BE_VERY_SMALL);
    }
}
