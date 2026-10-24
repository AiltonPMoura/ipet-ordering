package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.commons.PaymentMethod;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
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
                                   TransportDetail transportDetail,
                                   PaymentMethod paymentMethod) {

        var appointmentBooking = AppointmentBooking.create(
                customerId,
                appointmentSchedule.companyId(),
                appointmentSchedule,
                requestedStart
        );

        petServicePairs.forEach(petServicePair -> appointmentBooking.addAppointmentPetService(petServicePair.pet(), petServicePair.service()));

        var requestedEnd = appointmentBooking.scheduledEnd();

        appointmentSchedule.verifyOperatingHours(requestedStart, requestedEnd);
        this.verifyTimeSlotAvailability(appointmentSchedule, requestedStart, requestedEnd);

        appointmentBooking.changeTransportDetail(transportDetail);
        appointmentBooking.changePaymentMethod(paymentMethod);
        appointmentBooking.request(customerId, appointmentSchedule.companyId());

        return appointmentBooking;
    }

    private void verifyTimeSlotAvailability(AppointmentSchedule appointmentSchedule, LocalDateTime requestedStart, LocalDateTime requestedEnd) {
        if (appointmentBookings.hasTimeSlotConflict(appointmentSchedule.id(), requestedStart, requestedEnd))
            throw new RuntimeException("Horário já reservado.");
    }

}
