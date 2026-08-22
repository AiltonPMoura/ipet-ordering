package br.com.ipet.ordering.domain.model.booking.stay;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class StayBookingCannotBeRequestedException extends DomainException {

    private StayBookingCannotBeRequestedException(String message, String value) {
        super(message, value);
    }

    public static StayBookingCannotBeRequestedException noStays(String value) {
        return new StayBookingCannotBeRequestedException(MessageCode.Booking.ERROR_CANNOT_BE_REQUEST_HAS_NO_APPOINTMENTS, value);
    }

    public static StayBookingCannotBeRequestedException noPaymentMethod(String value) {
        return new StayBookingCannotBeRequestedException(MessageCode.Booking.ERROR_CANNOT_BE_REQUEST_HAS_NO_PAYMENT_METHOD, value);
    }

    public static StayBookingCannotBeRequestedException noBilling(String value) {
        return new StayBookingCannotBeRequestedException(MessageCode.Booking.ERROR_CANNOT_BE_PLACE_HAS_NO_BILLING, value);
    }

}
