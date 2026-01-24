package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class ProductNotFoundException extends DomainException {

    private final String value;

    public ProductNotFoundException(String value) {
        super(MessageCode.ERROR_PRODUCT_NOT_FOUND);
        this.value = value;
    }

}
