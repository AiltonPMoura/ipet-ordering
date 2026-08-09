package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class AppointmentBookingIsNotDraftToChangeException extends DomainException {

    private final String appointmentBookingId;

    public AppointmentBookingIsNotDraftToChangeException(String appointmentBookingId) {
        super(MessageCode.Booking.ERROR_APPOINTMENT_BOOKING_IS_NOT_DRAFT_TO_CHANGE);
        this.appointmentBookingId = appointmentBookingId;
    }
}
