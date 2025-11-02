package br.com.ipet.ordering.domain.exception;

import br.com.ipet.ordering.domain.exception.message.MessageCode;

public class ProductDescriptionCannotBeVerySmall extends DomainException {

    public ProductDescriptionCannotBeVerySmall() {
        super(MessageCode.ERROR_PRODUCT_DESCRIPTION_CANNOT_BE_VERY_SMALL);
    }
}
