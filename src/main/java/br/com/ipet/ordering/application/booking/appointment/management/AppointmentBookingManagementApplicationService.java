package br.com.ipet.ordering.application.booking.appointment.management;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.booking.Pet;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingService;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookings;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.pet.PetId;
import br.com.ipet.ordering.domain.model.pet.Pets;
import br.com.ipet.ordering.domain.model.schedule.ScheduleId;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentSchedules;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceOfferingCatalogService;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceOfferingId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentBookingManagementApplicationService {

    private final AppointmentBookings appointmentBookings;
    private final AppointmentBookingService appointmentBookingService;
    private final AppointmentSchedules appointmentSchedules;
    private final Pets pets;
    private final ServiceOfferingCatalogService serviceOfferingCatalogService;

    public UUID create(AppointmentBookingInput input) {
        var appointmentSchedule = appointmentSchedules.ofId(new ScheduleId(input.appointmentScheduleId()))
                .orElseThrow(() -> new DomainException("Agenda de agendamento não encontrada."));

        var petIds = input.petAppointments().stream()
                .map(petAppointmentInput -> new PetId(petAppointmentInput.petId()))
                .collect(Collectors.toSet());

        var appointmentService = serviceOfferingCatalogService.ofId(new ServiceOfferingId(input.petAppointments().getFirst().serviceOfferingId()), new CompanyId(input.companyId()))
                .orElseThrow(() -> new DomainException("Serviço não encontrado para o agendamento."));

        var customerPets = pets.ofCustomerAndIds(new CustomerId(input.customerId()), petIds).stream()
                .map(AppointmentBookingManagementApplicationService::buildBookingPet)
                .collect(Collectors.toSet());

        var appointmentBooking = appointmentBookingService.book(
                new CustomerId(input.customerId()),
                new CompanyId(input.companyId()),
                appointmentSchedule,
                appointmentService,
                input.scheduleStartTime(),
                customerPets
        );

        appointmentBookings.add(appointmentBooking);

        return appointmentBooking.id().value();
    }

    private static Pet buildBookingPet(br.com.ipet.ordering.domain.model.pet.Pet pet) {
        return Pet.builder()
                .id(pet.id().value())
                .name(pet.name().value())
                .type(pet.type().name())
                .breed(pet.breed().name())
                .gender(pet.gender().name())
                .size(pet.size().name())
                .weight(pet.weight().value())
                .age(pet.age().value())
                .build();
    }

}
