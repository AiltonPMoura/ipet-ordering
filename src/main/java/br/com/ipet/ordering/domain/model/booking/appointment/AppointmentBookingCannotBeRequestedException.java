package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class AppointmentBookingCannotBeRequestedException extends DomainException {

    private AppointmentBookingCannotBeRequestedException(String message, String value) {
        super(message, value);
    }

    public static AppointmentBookingCannotBeRequestedException noAppointments(String value) {
        return new AppointmentBookingCannotBeRequestedException(MessageCode.Booking.ERROR_CANNOT_BE_REQUEST_HAS_NO_APPOINTMENTS, value);
    }

    public static AppointmentBookingCannotBeRequestedException noPaymentMethod(String value) {
        return new AppointmentBookingCannotBeRequestedException(MessageCode.Booking.ERROR_CANNOT_BE_REQUEST_HAS_NO_PAYMENT_METHOD, value);
    }

    public static AppointmentBookingCannotBeRequestedException noBilling(String value) {
        return new AppointmentBookingCannotBeRequestedException(MessageCode.Booking.ERROR_CANNOT_BE_PLACE_HAS_NO_BILLING, value);
    }

    public static AppointmentBookingCannotBeRequestedException noPetTransport(String value) {
        return new AppointmentBookingCannotBeRequestedException(MessageCode.Booking.ERROR_CANNOT_BE_REQUEST_HAS_NO_PET_TRANSPORT, value);
    }

}
