package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import br.com.ipet.ordering.application.booking.appointment.AppointmentBookingBlockOutput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentBookingPersistenceRepository
        extends JpaRepository<AppointmentBookingPersistenceEntity, UUID>,
        JpaSpecificationExecutor<AppointmentBookingPersistenceEntity> {

    @Query("""
        SELECT new br.com.ipet.ordering.application.booking.appointment.AppointmentBookingBlockOutput(
            booking.scheduledStart,
            booking.scheduledEnd
        )
        FROM AppointmentBookingPersistenceEntity booking
        WHERE booking.schedule.id = :scheduleId
        AND booking.scheduledStart >= :windowStart
        AND booking.scheduledStart <= :windowEnd
        AND (
            booking.status IN (
                'PENDING_APPROVAL', 'SCHEDULED', 'PICKING_UP',
                'IN_PROGRESS', 'READY', 'DROPPING_OFF', 'RETURNED', 'COMPLETED'
            )
            OR (appointment.status = 'REQUESTED' AND appointment.scheduledStart >= :gracePeriod)
        )
        """)
    List<AppointmentBookingBlockOutput> findBlockedBookings(
            UUID scheduleId,
            OffsetDateTime windowStart,
            OffsetDateTime windowEnd,
            OffsetDateTime gracePeriod
    );
}
