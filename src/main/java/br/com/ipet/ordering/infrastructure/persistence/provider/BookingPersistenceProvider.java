package br.com.ipet.ordering.infrastructure.persistence.provider;

import br.com.ipet.ordering.Booking;
import br.com.ipet.ordering.domain.model.booking.Bookings;
import br.com.ipet.ordering.domain.model.schedule.ScheduleId;
import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.infrastructure.persistence.mapper.BookingMapper;
import br.com.ipet.ordering.infrastructure.persistence.repository.BookingPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class BookingPersistenceProvider implements Bookings {

    private final BookingPersistenceEntityRepository repository;
    private final BookingMapper bookingMapper;

    @Override
    public Optional<Booking> ofId(BookingId id) {
        return Optional.empty();
    }

    @Override
    public boolean exists(BookingId id) {
        return false;
    }

    @Override
    public void add(Booking aggregateRoot) {

    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public Set<Booking> ofAgendaIdGreaterThanNow(ScheduleId scheduleId) {
        /*var booknigs = repository.findByAgendaIdAndDateGreaterThan(agendaId.value(), LocalDate.now());

        return booknigs.stream().map(bookingMapper::toDomainEntity)
                .collect(Collectors.toUnmodifiableSet());*/
        return Set.of();
    }
}
