package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class OrderCannotBePlacedException extends DomainException {

    private OrderCannotBePlacedException(String message, String value) {
        super(message, value);
    }

    public static OrderCannotBePlacedException noItems(String value) {
        return new OrderCannotBePlacedException(MessageCode.Order.ERROR_CANNOT_BE_PLACE_HAS_NO_ITEMS, value);
    }

    public static OrderCannotBePlacedException noPaymentMethod(String value) {
        return new OrderCannotBePlacedException(MessageCode.Order.ERROR_CANNOT_BE_PLACE_HAS_NO_PAYMENT_METHOD, value);
    }

    public static OrderCannotBePlacedException noShipping(String value) {
        return new OrderCannotBePlacedException(MessageCode.Order.ERROR_CANNOT_BE_PLACE_HAS_NO_SHIPPING, value);
    }

    public static OrderCannotBePlacedException noBilling(String value) {
        return new OrderCannotBePlacedException(MessageCode.Order.ERROR_CANNOT_BE_PLACE_HAS_NO_BILLING, value);
    }

    public static OrderCannotBePlacedException noDeliveryCompany(String value) {
        return new OrderCannotBePlacedException(MessageCode.Order.ERROR_CANNOT_BE_PLACE_HAS_NO_DELIVERY_COMPANY, value);
    }

    public static OrderCannotBePlacedException invalidShippingCost(String value) {
        return new OrderCannotBePlacedException(MessageCode.Order.ERROR_CANNOT_BE_PLACE_INVALID_SHIPPING_COST, value);
    }

    public static OrderCannotBePlacedException invalidExpectedDeliveryDate(String value) {
        return new OrderCannotBePlacedException(MessageCode.Order.ERROR_CANNOT_BE_PLACE_INVALID_DELIBERY_DATE, value);
    }

}
