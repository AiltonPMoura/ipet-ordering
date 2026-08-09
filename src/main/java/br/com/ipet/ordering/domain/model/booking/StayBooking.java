package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.booking.appointment.PetAppointment;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;

import java.time.OffsetDateTime;
import java.util.Set;

import static br.com.ipet.ordering.domain.model.booking.TimeSlotBookingStatus.DRAFT;

public class StayBooking extends Booking
        implements AggregateRoot<BookingId> {

    private OffsetDateTime scheduledCheckIn;
    private OffsetDateTime scheduledCheckOut;
    private Set<PetStay> stays;
    private OffsetDateTime checkInAt;
    private OffsetDateTime checkOutAt;

    static Booking create(CustomerId customerId, CompanyId companyId, Set<PetAppointment> items) {
        return new Booking(new BookingId(), customerId, companyId, items,
                Quantity.ZERO, Money.ZERO, DRAFT,
                null,
                null, null,
                OffsetDateTime.now(), null, null,
                null, null

        );
    }

    //this.setStatus(status);
    this.setCheckIn(checkIn);
    this.setCheckOut(checkOut);
}
