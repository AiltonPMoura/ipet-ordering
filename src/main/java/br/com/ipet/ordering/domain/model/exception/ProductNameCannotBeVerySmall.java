package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class ProductNameCannotBeVerySmall extends DomainException {

    public ProductNameCannotBeVerySmall() {
        super(MessageCode.ERROR_PRODUCT_NAME_CANNOT_BE_VERY_SMALL);
    }
}
