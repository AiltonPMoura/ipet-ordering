package br.com.ipet.ordering.domain.model.repository;

import br.com.ipet.ordering.Booking;
import br.com.ipet.ordering.domain.model.valueobject.AgendaId;
import br.com.ipet.ordering.domain.model.valueobject.BookingId;

import java.util.Set;

public interface Bookings extends Repository<Booking, BookingId> {

    Set<Booking> ofAgendaId(AgendaId agendaId);

}
