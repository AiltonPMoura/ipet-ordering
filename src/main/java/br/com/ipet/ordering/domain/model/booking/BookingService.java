package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.Booking;
import br.com.ipet.ordering.domain.model.agenda.Agenda;
import br.com.ipet.ordering.domain.model.agenda.AgendaService;
import br.com.ipet.ordering.domain.model.customer.Customer;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetTime;

@RequiredArgsConstructor
public class BookingService {

    private final AgendaService agendaService;

    public void book(Customer customer, Agenda agenda,
                     LocalDate date, OffsetTime startTime, OffsetTime endTime) {

        var booking = Booking.createNew()
                .customerId(customer.id())
                .agendaId(agenda.id())
                .startTime(startTime)
                .endTime(endTime)
                .build();

    }

}
