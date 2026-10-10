package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import br.com.ipet.ordering.application.booking.appointment.AppointmentBookingBlockOutput;
import br.com.ipet.ordering.application.booking.appointment.AppointmentBookingQueryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class AppointmentBookingQueryServiceImpl implements AppointmentBookingQueryService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AppointmentBookingBlockOutput> findBlockedBookings(
            UUID scheduleId, OffsetDateTime windowStart, OffsetDateTime windowEnd, OffsetDateTime gracePeriod) {

        String jpql = """
            SELECT new br.com.ipet.ordering.application.booking.appointment.AppointmentBookingBlockOutput(
                appointment.scheduledStart,
                appointment.scheduledEnd
            )
            FROM AppointmentBookingPersistenceEntity appointment
            WHERE appointment.schedule.id = :scheduleId
            AND appointment.scheduledStart >= :windowStart
            AND appointment.scheduledStart <= :windowEnd
            AND (
                appointment.status IN (
                    'PENDING_APPROVAL', 'SCHEDULED', 'PICKING_UP',
                    'IN_PROGRESS', 'READY', 'DROPPING_OFF', 'RETURNED', 'COMPLETED'
                )
                OR (appointment.status = 'REQUESTED' AND appointment.scheduledStart >= :gracePeriod)
            )
        """;

        return entityManager.createQuery(jpql, AppointmentBookingBlockOutput.class)
                .setParameter("scheduleId", scheduleId)
                .setParameter("windowStart", windowStart)
                .setParameter("windowEnd", windowEnd)
                .setParameter("gracePeriod", gracePeriod)
                .getResultList();
    }
}
