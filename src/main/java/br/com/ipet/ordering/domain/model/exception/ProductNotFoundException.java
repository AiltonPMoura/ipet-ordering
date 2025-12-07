package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class ProductNotFoundException extends DomainException {

    private final String[] fields;

    public ProductNotFoundException(String... fields) {
        super(MessageCode.ERROR_PRODUCT_NOT_FOUND);
        this.fields = fields;
    }

}
