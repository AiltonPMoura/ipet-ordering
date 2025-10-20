package br.com.ipet.ordering.domain.exception;

import br.com.ipet.ordering.domain.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class OrderItemNotFoundException extends DomainException {

    private final String[] params;

    public OrderItemNotFoundException(String... params) {
        super(MessageCode.ERROR_FIELD_CANNOT_BE_EMPTY);
        this.params = params;
    }
}
