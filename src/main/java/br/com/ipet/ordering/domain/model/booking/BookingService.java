package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.Booking;
import br.com.ipet.ordering.domain.model.schedule.Schedule;
import br.com.ipet.ordering.domain.model.schedule.ScheduleService;
import br.com.ipet.ordering.domain.model.customer.Customer;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetTime;

@RequiredArgsConstructor
public class BookingService {

    private final ScheduleService scheduleService;

    public void book(Customer customer, Schedule schedule,
                     LocalDate date, OffsetTime startTime, OffsetTime endTime) {

        var booking = Booking.createNew()
                .customerId(customer.id())
                .agendaId(schedule.id())
                .startTime(startTime)
                .endTime(endTime)
                .build();

    }

}
