package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import br.com.ipet.ordering.domain.model.booking.Pet;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBooking;
import br.com.ipet.ordering.domain.model.serviceoffering.AppointmentService;
import br.com.ipet.ordering.domain.model.booking.appointment.PetAppointment;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Billing;
import br.com.ipet.ordering.infrastructure.persistence.company.CompanyPersistenceRepository;
import br.com.ipet.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceRepository;
import br.com.ipet.ordering.infrastructure.persistence.order.BillingEmbeddable;
import br.com.ipet.ordering.infrastructure.persistence.schedule.appointment.AppointmentSchedulePersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AppointmentBookingPersistenceMapper {

    private final CustomerPersistenceRepository customerPersistenceRepository;
    private final CompanyPersistenceRepository companyPersistenceRepository;
    private final AppointmentSchedulePersistenceRepository schedulePersistenceRepository;

    public AppointmentBookingPersistenceEntity fromDomain(AppointmentBooking appointmentBooking) {
        return this.merge(new AppointmentBookingPersistenceEntity(), appointmentBooking);
    }

    public AppointmentBookingPersistenceEntity merge(AppointmentBookingPersistenceEntity appointmentBookingPersistence,
                                                     AppointmentBooking appointmentBooking) {
        appointmentBookingPersistence.setId(appointmentBooking.id().value());
        appointmentBookingPersistence.setSchedule(schedulePersistenceRepository.getReferenceById(appointmentBooking.scheduleId().value()));
        appointmentBookingPersistence.setCompany(companyPersistenceRepository.getReferenceById(appointmentBooking.companyId().value()));
        appointmentBookingPersistence.setCustomer(customerPersistenceRepository.getReferenceById(appointmentBooking.customerId().value()));
        appointmentBookingPersistence.setScheduledStart(appointmentBooking.scheduledStart());
        appointmentBookingPersistence.setScheduledEnd(appointmentBooking.scheduledEnd());
        appointmentBookingPersistence.setTotalPets(appointmentBooking.totalPets().value());
        appointmentBookingPersistence.setTotalAmount(appointmentBooking.totalAmount().value());
        appointmentBookingPersistence.setStatus(appointmentBooking.appointmentStatus().name());
        appointmentBookingPersistence.setPaymentMethod(appointmentBooking.paymentMethod().name());
        appointmentBookingPersistence.setPaymentStatus(appointmentBooking.paymentStatus().name());
        appointmentBookingPersistence.setBilling(this.toBillingEmbeddable(appointmentBooking.billing()));
        appointmentBookingPersistence.setPetTransport(this.toPetTransportEmbeddable(appointmentBooking));
        appointmentBookingPersistence.setPetAppointments(this.mergePetAppointments(appointmentBookingPersistence, appointmentBooking));
        appointmentBookingPersistence.setRequestedAt(appointmentBooking.requestedAt());
        appointmentBookingPersistence.setScheduledAt(appointmentBooking.scheduledAt());
        appointmentBookingPersistence.setPaidAt(appointmentBooking.paidAt());
        appointmentBookingPersistence.setReadyAt(appointmentBooking.readyAt());
        appointmentBookingPersistence.setInProgressAt(appointmentBooking.inProgressAt());
        appointmentBookingPersistence.setReturnedAt(appointmentBooking.returnedAt());
        appointmentBookingPersistence.setCompletedAt(appointmentBooking.completedAt());
        appointmentBookingPersistence.setRefundedAt(appointmentBooking.refundedAt());
        appointmentBookingPersistence.setCanceledAt(appointmentBooking.canceledAt());
        appointmentBookingPersistence.setCancelationReason(appointmentBooking.cancelationReason());
        return appointmentBookingPersistence;
    }

    private Set<PetAppointmentPersistenceEntity> mergePetAppointments(AppointmentBookingPersistenceEntity appointmentBookingPersistence,
                                                                      AppointmentBooking appointmentBooking) {
        var petAppointmentsPersistence = appointmentBookingPersistence.getPetAppointments();
        var petAppointments = appointmentBooking.petAppointments();

        if (petAppointmentsPersistence.isEmpty())
            return petAppointments.stream().map(this::fromDomainPetAppointment).collect(Collectors.toSet());

        var petAppointmentsPersistenceMap = petAppointmentsPersistence.stream()
                .collect(Collectors.toMap(PetAppointmentPersistenceEntity::getId, item -> item));

        return petAppointments.stream().map(petAppointment -> {
            var petAppointmentPersistence = petAppointmentsPersistenceMap.getOrDefault(petAppointment.id().value(), new PetAppointmentPersistenceEntity());
            return this.mergePet(petAppointmentPersistence, petAppointment);
        }).collect(Collectors.toSet());
    }

    private PetAppointmentPersistenceEntity fromDomainPetAppointment(PetAppointment petAppointment) {
        return this.mergePet(new PetAppointmentPersistenceEntity(), petAppointment);
    }

    private PetAppointmentPersistenceEntity mergePet(PetAppointmentPersistenceEntity petAppointmentPersistence, PetAppointment petAppointment) {
        petAppointmentPersistence.setId(petAppointment.id().value());
        petAppointmentPersistence.setPet(this.toPetEmbeddable(petAppointment.pet()));
        petAppointmentPersistence.setService(this.toServiceEmbeddable(petAppointment.service()));
        return petAppointmentPersistence;
    }

    private PetEmbeddable toPetEmbeddable(Pet pet) {
        return PetEmbeddable.builder()
                .petId(pet.id())
                .name(pet.name())
                .petType(pet.type())
                .breed(pet.breed())
                .gender(pet.gender())
                .size(pet.size())
                .weight(pet.weight())
                .age(pet.age())
                .build();
    }

    private ServiceEmbeddable toServiceEmbeddable(AppointmentService service) {
        return ServiceEmbeddable.builder()
                .serviceId(service.id().value())
                .type(service.type())
                .category(service.category())
                .description(service.description())
                .duration(service.duration())
                .price(service.price().value())
                .petSize(service.petSize())
                .build();
    }

    private PetTransportEmbeddable toPetTransportEmbeddable(AppointmentBooking appointmentBooking) {
        return PetTransportEmbeddable.builder()
                .cost(appointmentBooking.transport().cost().value())
                .build();
    }

    private BillingEmbeddable toBillingEmbeddable(Billing billing) {
        return BillingEmbeddable.builder()
                .firstName(billing.fullName().firstName())
                .lastName(billing.fullName().lastName())
                .document(billing.document().value())
                .phone(billing.phone().value())
                .email(billing.email().value())
                .address(this.toAddressEmbeddable(billing.address()))
                .build();
    }

    private AddressEmbeddable toAddressEmbeddable(Address address) {
        return AddressEmbeddable.builder()
                .street(address.street())
                .number(address.number())
                .neighborhood(address.neighborhood())
                .complement(address.complement())
                .city(address.city())
                .state(address.state())
                .zipCode(address.zipCode().value())
                .build();
    }
}
