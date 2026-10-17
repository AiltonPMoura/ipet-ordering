package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentSchedule;
import br.com.ipet.ordering.domain.model.serviceoffering.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppointmentBookingService {

    private final AppointmentBookings appointmentBookings;

    public AppointmentBooking book(CustomerId customerId,
                                   AppointmentSchedule appointmentSchedule,
                                   LocalDateTime requestedStart,
                                   Set<PetServicePair> petServicePairs) {

        var requestedEnd = this.calculateRequestedEnd(requestedStart, petServicePairs);

        appointmentSchedule.verifyOperatingHours(requestedStart, requestedEnd);

        this.verifyTimeSlotAvailability(appointmentSchedule, requestedStart, requestedEnd);

        var appointmentBooking = AppointmentBooking.create(
                customerId,
                appointmentSchedule.companyId(),
                appointmentSchedule,
                requestedStart,
                requestedEnd
        );

        petServicePairs.forEach(petServicePair -> appointmentBooking.addAppointmentPetService(petServicePair.pet(), petServicePair.service()));

        appointmentBooking.request(customerId, appointmentSchedule.companyId());

        return appointmentBooking;
    }

    private LocalDateTime calculateRequestedEnd(LocalDateTime requestedStart, Set<PetServicePair> petServicePairs) {
        return requestedStart.plusMinutes(petServicePairs.stream()
                .map(PetServicePair::service)
                .mapToInt(AppointmentService::duration)
                .sum());
    }

    private void verifyTimeSlotAvailability(AppointmentSchedule appointmentSchedule, LocalDateTime requestedStart, LocalDateTime requestedEnd) {
        if (appointmentBookings.hasTimeSlotConflict(appointmentSchedule.id(), requestedStart, requestedEnd))
            throw new RuntimeException("Horário já reservado.");
    }

}
