package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface AppointmentBookingPersistenceRepository
        extends JpaRepository<AppointmentBookingPersistenceEntity, UUID>,
        JpaSpecificationExecutor<AppointmentBookingPersistenceEntity> {
}
