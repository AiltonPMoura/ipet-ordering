package br.com.ipet.ordering.domain.model;

public class MessageCode {
    private MessageCode(){}

    public static class Order {
        private Order(){}
        public static final String ERROR_CANNOT_BE_PLACE_HAS_NO_ITEMS = "error.order.cannot.be.placed.has.no.items";
        public static final String ERROR_CANNOT_BE_PLACE_HAS_NO_PAYMENT_METHOD = "error.order.cannot.be.placed.has.no.payment.method";
        public static final String ERROR_CANNOT_BE_PLACE_HAS_NO_SHIPPING = "error.order.cannot.be.placed.has.no.shipping";
        public static final String ERROR_CANNOT_BE_PLACE_HAS_NO_BILLING = "error.order.cannot.be.placed.has.no.billing";
        public static final String ERROR_CANNOT_BE_PLACE_HAS_NO_DELIVERY_COMPANY = "error.order.cannot.be.placed.has.no.delivery.company";
        public static final String ERROR_CANNOT_BE_PLACE_INVALID_SHIPPING_COST = "error.order.cannot.be.placed.invalid.shipping.cost";
        public static final String ERROR_CANNOT_BE_PLACE_INVALID_DELIVERY_DATE = "error.order.cannot.be.placed.invalid.delivery.date";
    }

    public static class Customer {
        private Customer(){}
        public static final String NOT_FOUND = "customer.not.found";
        public static final String EMAIL_IN_USE = "customer.email.in.use";
    }

    public static class Pet {
        private Pet(){}
        public static final String NOT_FOUND = "pet.not.found";
    }

    public static class Booking {
        private Booking(){}
        public static final String ERROR_CANNOT_BE_REQUEST_HAS_NO_APPOINTMENTS = "error.appointment.cannot.be.request.has.no.appointments";
        public static final String ERROR_CANNOT_BE_REQUEST_HAS_NO_PAYMENT_METHOD = "error.appointment.cannot.be.request.has.no.payment.method";
        public static final String ERROR_CANNOT_BE_PLACE_HAS_NO_BILLING = "error.appointment.cannot.be.placed.has.no.billing";
        public static final String ERROR_CANNOT_BE_REQUEST_HAS_NO_PET_TRANSPORT = "error.appointment.cannot.be.request.has.no.pet.trasnport";
        public static final String ERROR_APPOINTMENT_BOOKING_IS_NOT_DRAFT_TO_CHANGE = "error.appointment.is.not.draft.to.change";
    }

    public static final String ERROR_FIELD_CANNOT_BE_EMPTY = "error.field.cannot.be.empty";
    public static final String ERROR_EMAIL_NOT_VALID = "error.email.not.valid";
    public static final String INVALID_DOCUMENT = "error.invalid.document";
    public static final String INVALID_FORMAT_DOCUMENT = "error.invalid.format.document";

    public static final String ERROR_NUMBER_CANNOT_BE_NEGATIVE = "error.number.cannot.be.negative";
    public static final String ERROR_QUANTITY_NEEDS_GREATER_THAN_ZERO = "error.quantity.needs.greater.than.zero";

    public static final String ERROR_CANNOT_CHANGE_STATUS = "error.cannot.change.status";
    public static final String ERROR_ORDER_CANNOT_BE_EDITED = "error.order.cannot.be.edited";
    public static final String ERROR_AGENDA_IS_NOT_DRAFT_TO_CHANGE = "error.agenda.is.not.draft.to.change";
    public static final String ERROR_SCHEDULING_IS_NOT_DRAFT_TO_CHANE = "error.scheduling.is.not.draft.to.change";
    public static final String ERROR_ORDER_DOES_NOT_CONTAIN_ITEM = "error.order.does.not.contain.item";
    public static final String ERROR_SCHEDULING_PET_NOT_FOUND = "error.scheduling.pet.not.found";
    public static final String ERROR_WEIGHT_CANNOT_BE_ZERO_OR_NEGATIVE = "error.cannot.be.zero.or.negative";
    public static final String ERROR_SERVICE_NOT_FOUND = "error.service.not.found";
    public static final String ERROR_PRODUCT_NOT_FOUND = "error.product.not.found";
    public static final String ERROR_PRODUCT_NAME_CANNOT_BE_VERY_SMALL = "error.product.name.cannot.be.very.small";
    public static final String ERROR_PRODUCT_DESCRIPTION_CANNOT_BE_VERY_SMALL = "error.product.description.cannot.be.very.small";
    public static final String ERROR_SERVICE_NAME_CANNOT_BE_VERY_SMALL = "error.service.name.cannot.be.very.small";
    public static final String ERROR_SERVICE_DESCRIPTION_CANNOT_BE_VERY_SMALL = "error.service.description.cannot.be.very.small";
    public static final String ERROR_WORKING_HOURS_CANNOT_BE_LESS_THAN_ONE = "error.working.hours.cannot.be.less.than.one";
    public static final String ERROR_INVALID_INTERVAL_LOCKED_TIME = "error.invalid.interval.locked.time";
    public static final String ERROR_START_TIME_CANNOT_BE_GREATER_THAN_END_TIME = "start.time.cannot.be.greater.than.end.time";
    public static final String ERROR_START_DATE_CANNOT_BE_GREATER_THAN_END_DATE = "start.date.cannot.be.greater.than.end.date";
    public static final String ERROR_DATE_TIME_MUST_BE_LATTER_THAN_NOW = "error.date.time.must.be.latter.than.now";
    public static final String ERROR_DATE_MUST_BE_LATTER_THAN_NOW = "error.date.must.be.latter.than.now";
    public static final String ERROR_CANNOT_CHANGE_SCHEDULING_AT = "error.cannot.change.scheduling.at";
    public static final String ERROR_STOCK_CANNOT_BE_NEGATIVE = "error.stock.cannot.be.negative";

}
