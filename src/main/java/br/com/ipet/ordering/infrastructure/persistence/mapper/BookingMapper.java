package br.com.ipet.ordering.infrastructure.persistence.mapper;

import br.com.ipet.ordering.Booking;
import br.com.ipet.ordering.domain.model.agenda.AgendaId;
import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.infrastructure.persistence.entity.BookingPersistenceEntity;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public Booking toDomainEntity(BookingPersistenceEntity bookingPersistenceEntity) {
        return Booking.existing()
                .id(new BookingId(bookingPersistenceEntity.getId()))
                .agendaId(new AgendaId(bookingPersistenceEntity.getAgendaId()))
                .customerId(new CustomerId(bookingPersistenceEntity.getCustomerId()))
                .date(bookingPersistenceEntity.getDate())
                .startTime(bookingPersistenceEntity.getStartTime())
                .endTime(bookingPersistenceEntity.getEndTime())
                .build();
    }

}
