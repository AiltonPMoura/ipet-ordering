package br.com.ipet.ordering.domain.exception.message;

public class MessageCode {
    private MessageCode(){}

    public static final String ERROR_FIELD_CANNOT_BE_EMPTY = "error.attribute.cannot.be.empty";
    public static final String ERROR_EMAIL_NOT_VALID = "error.email.not.valid";
    public static final String ERROR_NUMBER_CANNOT_BE_NEGATIVE = "error.no.items";
    public static final String ERROR_QUANTITY_GREATER_THAN_ZERO = "error.quantity.greater.than.zero";
    public static final String ERROR_NO_ITEMS = "error.no.items";
    public static final String ERROR_NO_PAYMENT_METHOD = "error.no.payment.method";
    public static final String ERROR_CANNOT_BE_CHANGE_STATUS = "error.cannot.be.change.status";

}
