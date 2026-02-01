package br.com.ipet.ordering;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.agenda.AgendaId;
import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import lombok.Builder;

import java.time.LocalDate;
import java.time.OffsetTime;

public class Booking implements AggregateRoot<BookingId> {
    private BookingId id;
    private AgendaId agendaId;
    private CustomerId customerId;
    private LocalDate date;
    private OffsetTime startTime;
    private OffsetTime endTime;

    @Builder(builderClassName = "CreateNewBookingBuilder", builderMethodName = "createNew")
    private static Booking create(AgendaId agendaId, CustomerId customerId, LocalDate date,
                                  OffsetTime startTime, OffsetTime endTime) {
        return new Booking(new BookingId(), agendaId, customerId, date, startTime, endTime);
    }

    @Builder(builderClassName = "ExistingBookingBuilder", builderMethodName = "existing")
    private Booking(BookingId id, AgendaId agendaId, CustomerId customerId, LocalDate date,
                   OffsetTime startTime, OffsetTime endTime) {
        this.setId(id);
        this.setAgendaId(agendaId);
        this.setCustomerId(customerId);
        this.setDate(date);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
    }

    public BookingId id() {
        return id;
    }

    public AgendaId agendaId() {
        return agendaId;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public LocalDate date() {
        return date;
    }

    public OffsetTime startTime() {
        return startTime;
    }

    public OffsetTime endTime() {
        return endTime;
    }

    private void setId(BookingId id) {
        this.id = id;
    }

    private void setAgendaId(AgendaId agendaId) {
        this.agendaId = agendaId;
    }

    private void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    private void setDate(LocalDate date) {
        this.date = date;
    }

    private void setStartTime(OffsetTime startTime) {
        this.startTime = startTime;
    }

    private void setEndTime(OffsetTime endTime) {
        this.endTime = endTime;
    }
}
