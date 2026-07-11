/*
package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class SchedulingCannotBePlacedException extends DomainException {

    private final String value;

    private SchedulingCannotBePlacedException(String message, String value) {
        super(message);
        this.value = value;
    }

    public static SchedulingCannotBePlacedException noItems(String value) {
        return new SchedulingCannotBePlacedException(MessageCode.ERROR_SCHEDULING_CANNOT_BE_PLACE_HAS_NO_ITEMS, value);
    }

    public static SchedulingCannotBePlacedException noPaymentMethod(String value) {
        return new SchedulingCannotBePlacedException(MessageCode.ERROR_SCHEDULING_CANNOT_BE_PLACE_HAS_NO_PAYMENT_METHOD, value);
    }

    public static SchedulingCannotBePlacedException noDeliveryCompany(String value) {
        return new SchedulingCannotBePlacedException(MessageCode.ERROR_SCHEDULING_CANNOT_BE_PLACE_HAS_NO_DELIVERY_COMPANY, value);
    }

}

