package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.booking.PetBookingTransport;
import br.com.ipet.ordering.domain.model.commons.enums.PaymentMethod;
import br.com.ipet.ordering.domain.model.commons.id.CustomerId;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentSchedule;
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
                                   Set<PetServicePair> petServicePairs,
                                   PetBookingTransport petBookingTransport,
                                   PaymentMethod paymentMethod) {

        var appointmentBooking = AppointmentBooking.create(
                customerId,
                appointmentSchedule.companyId(),
                appointmentSchedule,
                requestedStart
        );

        appointmentBooking.changeTransport(petBookingTransport);
        appointmentBooking.changePaymentMethod(paymentMethod);

        petServicePairs.forEach(petServicePair -> appointmentBooking.addAppointmentPetService(petServicePair.bookingPet(), petServicePair.service()));

        var scheduledEnd = appointmentBooking.scheduledEnd();

        appointmentSchedule.verifyOperatingHours(requestedStart, scheduledEnd);

        this.verifyNoTimeSlotConflict(appointmentSchedule, requestedStart, scheduledEnd);

        appointmentBooking.request(customerId, appointmentSchedule.companyId());

        return appointmentBooking;
    }

    private void verifyNoTimeSlotConflict(AppointmentSchedule appointmentSchedule, LocalDateTime requestedStart, LocalDateTime requestedEnd) {
        if (appointmentBookings.existsTimeSlotConflict(appointmentSchedule.id(), requestedStart, requestedEnd))
            throw new RuntimeException("Horário já reservado.");
    }

}
