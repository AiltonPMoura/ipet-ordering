package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import br.com.ipet.ordering.domain.model.booking.Pet;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBooking;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentService;
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

    public AppointmentBookingPersistenceEntity merge(AppointmentBookingPersistenceEntity appointmentBookingPersistence, AppointmentBooking appointmentBooking) {
        appointmentBookingPersistence.setId(appointmentBooking.id().value());
        appointmentBookingPersistence.setCustomer(customerPersistenceRepository.getReferenceById(appointmentBooking.customerId().value()));
        appointmentBookingPersistence.setCompany(companyPersistenceRepository.getReferenceById(appointmentBooking.companyId().value()));
        appointmentBookingPersistence.setSchedule(schedulePersistenceRepository.getReferenceById(appointmentBooking.scheduleId().value()));
        appointmentBookingPersistence.setTotalPets(appointmentBooking.totalPets().value());
        appointmentBookingPersistence.setTotalAmount(appointmentBooking.totalAmount().value());
        appointmentBookingPersistence.setPaymentMethod(appointmentBooking.paymentMethod().name());
        appointmentBookingPersistence.setPaymentStatus(appointmentBooking.paymentStatus().name());
        appointmentBookingPersistence.setBilling(this.toBillingEmbeddable(appointmentBooking.billing()));
        appointmentBookingPersistence.setRequestedAt(appointmentBooking.requestedAt());
        appointmentBookingPersistence.setPaidAt(appointmentBooking.paidAt());
        appointmentBookingPersistence.setScheduledAt(appointmentBooking.scheduledAt());
        appointmentBookingPersistence.setCompletedAt(appointmentBooking.completedAt());
        appointmentBookingPersistence.setCanceledAt(appointmentBooking.canceledAt());
        appointmentBookingPersistence.setRefundedAt(appointmentBooking.refundedAt());
        appointmentBookingPersistence.setCancelationReason(appointmentBooking.cancelationReason());
        appointmentBookingPersistence.setScheduledStart(appointmentBooking.scheduledStart());
        appointmentBookingPersistence.setScheduledEnd(appointmentBooking.scheduledEnd());
        appointmentBookingPersistence.setStatus(appointmentBooking.appointmentStatus().name());
        appointmentBookingPersistence.setPetTransport(this.toPetTransportEmbeddable(appointmentBooking));
        appointmentBookingPersistence.setInProgressAt(appointmentBooking.inProgressAt());
        appointmentBookingPersistence.setReadyAt(appointmentBooking.readyAt());
        appointmentBookingPersistence.setReturnedAt(appointmentBooking.returnedAt());
        appointmentBookingPersistence.setPets(this.mergePets(appointmentBookingPersistence, appointmentBooking));
        return appointmentBookingPersistence;
    }

    private Set<PetAppointmentPersistenceEntity> mergePets(AppointmentBookingPersistenceEntity appointmentBookingPersistence, AppointmentBooking appointmentBooking) {
        var persistencePets = appointmentBookingPersistence.getPets();
        var persistencePetsById = persistencePets.stream()
                .collect(Collectors.toMap(PetAppointmentPersistenceEntity::getId, item -> item));

        return appointmentBooking.appointments().stream()
                .map(petAppointment -> {
                    var petPersistence = persistencePetsById.getOrDefault(petAppointment.id().value(), new PetAppointmentPersistenceEntity());
                    return this.mergePet(petPersistence, petAppointment, appointmentBookingPersistence);
                }).collect(Collectors.toSet());
    }

    private PetAppointmentPersistenceEntity mergePet(PetAppointmentPersistenceEntity petPersistence, PetAppointment petAppointment, AppointmentBookingPersistenceEntity appointmentBookingPersistence) {
        petPersistence.setId(petAppointment.id().value());
        petPersistence.setAppointmentBooking(appointmentBookingPersistence);
        petPersistence.setPet(this.toPetEmbeddable(petAppointment.pet()));
        petPersistence.setService(this.toServiceEmbeddable(petAppointment.service()));
        return petPersistence;
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
                .serviceId(service.serviceId())
                .type(service.type())
                .category(service.category())
                .description(service.description())
                .duration(service.duration())
                .price(service.price())
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
