package br.com.ipet.ordering.application.booking.appointment.management;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.booking.Pet;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingService;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookings;
import br.com.ipet.ordering.domain.model.booking.appointment.PetServicePair;
import br.com.ipet.ordering.domain.model.commons.PaymentMethod;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.pet.PetId;
import br.com.ipet.ordering.domain.model.pet.Pets;
import br.com.ipet.ordering.domain.model.schedule.ScheduleId;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentSchedules;
import br.com.ipet.ordering.domain.model.serviceoffering.AppointmentService;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceOfferingCatalogService;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentBookingManagementApplicationService {

    private final AppointmentBookingService appointmentBookingService;
    private final AppointmentBookings appointmentBookings;
    private final AppointmentSchedules appointmentSchedules;
    private final Pets pets;
    private final ServiceOfferingCatalogService serviceOfferingCatalogService;

    public UUID create(AppointmentBookingInput input) {
        var appointmentSchedule = appointmentSchedules.ofId(new ScheduleId(input.appointmentScheduleId()))
                .orElseThrow(() -> new DomainException("Agenda não encontrada."));

        var petsById = this.findCustomerPetsMappedById(input);
        var appointmentServicesById = this.findAppointmentServicesMappedById(input);
        var petServicePairs = this.buildPetServicePairs(input, petsById, appointmentServicesById);

        var appointmentBooking = appointmentBookingService.book(
                new CustomerId(input.customerId()),
                appointmentSchedule,
                input.requestedStart(),
                petServicePairs,
                PaymentMethod.valueOf(input.paymentMethod())
        );

        appointmentBookings.add(appointmentBooking);

        return appointmentBooking.id().value();
    }

    private Map<ServiceId, AppointmentService> findAppointmentServicesMappedById(AppointmentBookingInput input) {
        var serviceIds = input.appointments().stream()
                .map(appointmentInput -> new ServiceId(appointmentInput.serviceId()))
                .collect(Collectors.toSet());

        return serviceOfferingCatalogService.ofIdsAndCompanyId(serviceIds, new CompanyId(input.companyId())).stream()
                .collect(Collectors.toMap(AppointmentService::id, service -> service));
    }

    private Map<PetId, Pet> findCustomerPetsMappedById(AppointmentBookingInput input) {
        var petIds = input.appointments().stream()
                .map(appointmentInput -> new PetId(appointmentInput.petId()))
                .collect(Collectors.toSet());

        return pets.ofCustomerAndIds(new CustomerId(input.customerId()), petIds).stream()
                .map(this::buildBookingPet)
                .collect(Collectors.toMap(Pet::id, pet -> pet));
    }

    private Set<PetServicePair> buildPetServicePairs(AppointmentBookingInput input,
                                                     Map<PetId, Pet> petsById,
                                                     Map<ServiceId, AppointmentService> appointmentServicesById) {
        return input.appointments().stream()
                .map(appointmentInput -> {
                    var pet = Optional.ofNullable(petsById.get(new PetId(appointmentInput.petId())))
                            .orElseThrow(() -> new DomainException("Pet não encontrado: " + appointmentInput.petId()));

                    var service = Optional.ofNullable(appointmentServicesById.get(new ServiceId(appointmentInput.serviceId())))
                            .orElseThrow(() -> new DomainException("Serviço não encontrado: " + appointmentInput.serviceId()));

                    return new PetServicePair(pet, service);
                })
                .collect(Collectors.toSet());
    }

    private Pet buildBookingPet(br.com.ipet.ordering.domain.model.pet.Pet pet) {
        return Pet.builder()
                .id(pet.id())
                .type(pet.type())
                .size(pet.size())
                .name(pet.name().value())
                .breed(pet.breed().name())
                .gender(pet.gender().name())
                .weight(pet.weight().value())
                .age(pet.age().value())
                .build();
    }

}
