package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBooking;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookings;
import br.com.ipet.ordering.domain.model.customer.Customer;
import br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AppointmentBookingProvider implements AppointmentBookings {

    private final AppointmentBookingPersistenceRepository appointmentBookingRepository;
    private final AppointmentBookingMapper appointmentBookingMapper;
    private final AppointmentBookingPersistenceMapper appointmentBookingPersistenceMapper;

    @Override
    public Optional<AppointmentBooking> ofId(BookingId appointmentBookingId) {
        return appointmentBookingRepository.findById(appointmentBookingId.value()).map(appointmentBookingMapper::toDomain);
    }

    @Override
    public boolean exists(BookingId appointmentBookingId) {
        return appointmentBookingRepository.existsById(appointmentBookingId.value());
    }

    @Override
    public void add(AppointmentBooking appointmentBooking) {
        appointmentBookingRepository.findById(appointmentBooking.id().value())
                .ifPresentOrElse(
                        appointmentBookingPersistence -> this.update(appointmentBookingPersistence, appointmentBooking),
                        () -> this.insert(appointmentBooking)
                );

        appointmentBooking.clearDomainEvents();
    }

    @Override
    public long count() {
        return appointmentBookingRepository.count();
    }

    private void insert(AppointmentBooking appointmentBooking) {
        var appointmentBookingPersistence = appointmentBookingPersistenceMapper.fromDomain(appointmentBooking);
        appointmentBookingRepository.saveAndFlush(appointmentBookingPersistence);
    }

    private void update(AppointmentBookingPersistenceEntity appointmentBookingPersistence, AppointmentBooking appointmentBooking) {
        appointmentBookingPersistence = appointmentBookingPersistenceMapper.merge(appointmentBookingPersistence, appointmentBooking);
        appointmentBookingRepository.saveAndFlush(appointmentBookingPersistence);
    }
}
