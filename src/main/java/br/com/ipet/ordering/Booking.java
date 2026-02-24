package br.com.ipet.ordering;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.schedule.ScheduleId;
import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import lombok.Builder;

import java.time.LocalDate;
import java.time.OffsetTime;

public class Booking implements AggregateRoot<BookingId> {
    private BookingId id;
    private ScheduleId scheduleId;
    private CustomerId customerId;
    private LocalDate date;
    private OffsetTime startTime;
    private OffsetTime endTime;

    @Builder(builderClassName = "CreateNewBookingBuilder", builderMethodName = "createNew")
    private static Booking create(ScheduleId scheduleId, CustomerId customerId, LocalDate date,
                                  OffsetTime startTime, OffsetTime endTime) {
        return new Booking(new BookingId(), scheduleId, customerId, date, startTime, endTime);
    }

    @Builder(builderClassName = "ExistingBookingBuilder", builderMethodName = "existing")
    private Booking(BookingId id, ScheduleId scheduleId, CustomerId customerId, LocalDate date,
                    OffsetTime startTime, OffsetTime endTime) {
        this.setId(id);
        this.setScheduleId(scheduleId);
        this.setCustomerId(customerId);
        this.setDate(date);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
    }

    public BookingId id() {
        return id;
    }

    public ScheduleId agendaId() {
        return scheduleId;
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

    private void setScheduleId(ScheduleId scheduleId) {
        this.scheduleId = scheduleId;
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
