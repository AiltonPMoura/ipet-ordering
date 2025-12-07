package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class OrderCannotBePlacedException extends DomainException {

    private final String value;

    private OrderCannotBePlacedException(String message, String value) {
        super(message);
        this.value = value;
    }

    public static OrderCannotBePlacedException noItems(String value) {
        return new OrderCannotBePlacedException(MessageCode.ERROR_NO_ITEMS, value);
    }

    public static OrderCannotBePlacedException noPaymentMethod(String value) {
        return new OrderCannotBePlacedException(MessageCode.ERROR_NO_PAYMENT_METHOD, value);
    }

}
