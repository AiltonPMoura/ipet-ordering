package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.Booking;
import br.com.ipet.ordering.domain.model.agenda.AgendaId;
import br.com.ipet.ordering.domain.model.Repository;

import java.util.Set;

public interface Bookings extends Repository<Booking, BookingId> {

    Set<Booking> ofAgendaIdGreaterThanNow(AgendaId agendaId);

}
