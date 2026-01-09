package br.com.ipet.ordering.infrastructure.persistence.mapper;

import br.com.ipet.ordering.Booking;
import br.com.ipet.ordering.domain.model.valueobject.AgendaId;
import br.com.ipet.ordering.domain.model.valueobject.BookingId;
import br.com.ipet.ordering.domain.model.valueobject.CustomerId;
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
