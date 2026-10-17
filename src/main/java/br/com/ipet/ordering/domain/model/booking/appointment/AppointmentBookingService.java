package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.booking.Pet;
import br.com.ipet.ordering.domain.model.company.CompanyId;
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
                                   CompanyId companyId,
                                   AppointmentSchedule appointmentSchedule,
                                   AppointmentService appointmentService,
                                   LocalDateTime requestedStart,
                                   Set<Pet> pets) {

        var requestedEnd = requestedStart.plusMinutes(appointmentService.duration());

        this.verifyTimeSlotAvailability(appointmentSchedule, requestedStart, requestedEnd);

        var appointmentBooking = AppointmentBooking.create(
                customerId,
                appointmentSchedule.companyId(),
                appointmentSchedule,
                requestedStart,
                requestedEnd
        );

        //Agrupar pets por service
        pets.forEach(pet -> appointmentBooking.addPetAppointment(pet, appointmentService));

        return appointmentBooking;
    }

    private void verifyTimeSlotAvailability(AppointmentSchedule appointmentSchedule, LocalDateTime requestedStart, LocalDateTime requestedEnd) {
        if (appointmentBookings.hasTimeSlotConflict(appointmentSchedule.id(), requestedStart, requestedEnd))
            throw new RuntimeException("Horário já reservado.");
    }

}
