package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.domain.model.booking.appointment.PetTransport;
import br.com.ipet.ordering.domain.model.commons.PaymentMethod;
import br.com.ipet.ordering.domain.model.commons.PaymentStatus;
import br.com.ipet.ordering.domain.model.booking.Pet;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBooking;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus;
import br.com.ipet.ordering.domain.model.pet.PetId;
import br.com.ipet.ordering.domain.model.pet.PetSize;
import br.com.ipet.ordering.domain.model.pet.PetType;
import br.com.ipet.ordering.domain.model.serviceoffering.AppointmentService;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentPetService;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentPetServiceId;
import br.com.ipet.ordering.domain.model.commons.document.DocumentFactory;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Billing;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Phone;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.commons.valueobject.ZipCode;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.schedule.ScheduleId;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceCategory;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceId;
import br.com.ipet.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import br.com.ipet.ordering.infrastructure.persistence.order.BillingEmbeddable;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AppointmentBookingMapper {

    public AppointmentBooking toDomain(AppointmentBookingPersistenceEntity appointmentBookingPersistence) {
        return AppointmentBooking.existing()
                .id(new BookingId(appointmentBookingPersistence.getId()))
                .customerId(new CustomerId(appointmentBookingPersistence.getCustomerId()))
                .companyId(new CompanyId(appointmentBookingPersistence.getCompanyId()))
                .scheduleId(new ScheduleId(appointmentBookingPersistence.getScheduleId()))
                .appointmentPetServices(this.toPetAppointments(appointmentBookingPersistence.getPetAppointments()))
                .totalPets(new Quantity(appointmentBookingPersistence.getTotalPets()))
                .totalAmount(new Money(appointmentBookingPersistence.getTotalAmount()))
                .paymentMethod(PaymentMethod.valueOf(appointmentBookingPersistence.getPaymentMethod()))
                .paymentStatus(PaymentStatus.valueOf(appointmentBookingPersistence.getPaymentStatus()))
                .billing(this.toBilling(appointmentBookingPersistence.getBilling()))
                .transport(new PetTransport(new Money(appointmentBookingPersistence.getPetTransport().getCost()), null, null))
                .appointmentStatus(AppointmentBookingStatus.valueOf(appointmentBookingPersistence.getStatus()))
                .scheduledStart(appointmentBookingPersistence.getScheduledStart())
                .scheduledEnd(appointmentBookingPersistence.getScheduledEnd())
                .requestedAt(appointmentBookingPersistence.getRequestedAt())
                .scheduledAt(appointmentBookingPersistence.getScheduledAt())
                .paidAt(appointmentBookingPersistence.getPaidAt())
                .inProgressAt(appointmentBookingPersistence.getInProgressAt())
                .readyAt(appointmentBookingPersistence.getReadyAt())
                .returnedAt(appointmentBookingPersistence.getReturnedAt())
                .completedAt(appointmentBookingPersistence.getCompletedAt())
                .cancelAt(appointmentBookingPersistence.getCanceledAt())
                .refundedAt(appointmentBookingPersistence.getRefundedAt())
                .cancelationReason(appointmentBookingPersistence.getCancelationReason())
                .build();
    }

    private Set<AppointmentPetService> toPetAppointments(Set<PetAppointmentPersistenceEntity> petAppointmentsPersistence) {
        return petAppointmentsPersistence.stream()
                .map(petAppointmentPersistence -> AppointmentPetService.existing()
                        .id(new AppointmentPetServiceId(petAppointmentPersistence.getId()))
                        .appointmentBookingId(new BookingId(petAppointmentPersistence.getAppointmentBookingId()))
                        .pet(this.toPet(petAppointmentPersistence.getPet()))
                        .service(this.toService(petAppointmentPersistence.getService()))
                        .build())
                .collect(Collectors.toSet());
    }

    private Pet toPet(PetEmbeddable petEmbeddable) {
        return new Pet(
                new PetId(petEmbeddable.getPetId()),
                PetType.valueOf(petEmbeddable.getPetType()),
                PetSize.valueOf(petEmbeddable.getSize()),
                petEmbeddable.getName(),
                petEmbeddable.getBreed(),
                petEmbeddable.getGender(),

                petEmbeddable.getWeight(),
                petEmbeddable.getAge()
        );
    }

    private AppointmentService toService(ServiceEmbeddable serviceEmbeddable) {
        return AppointmentService.builder()
                .id(new ServiceId(serviceEmbeddable.getServiceId()))
                .type(serviceEmbeddable.getType())
                .category(ServiceCategory.valueOf(serviceEmbeddable.getCategory()))
                .description(serviceEmbeddable.getDescription())
                .duration(serviceEmbeddable.getDuration())
                .price(new Money(serviceEmbeddable.getPrice()))
                .petSize(PetSize.valueOf(serviceEmbeddable.getPetSize()))
                .build();
    }

    private Billing toBilling(BillingEmbeddable billingEmbeddable) {
        return Billing.builder()
                .fullName(new FullName(billingEmbeddable.getFirstName(), billingEmbeddable.getLastName()))
                .document(DocumentFactory.from(billingEmbeddable.getDocument()))
                .phone(new Phone(billingEmbeddable.getPhone()))
                .email(new Email(billingEmbeddable.getEmail()))
                .address(this.toAddress(billingEmbeddable.getAddress()))
                .build();
    }

    private Address toAddress(AddressEmbeddable addressEmbeddable) {
        return Address.builder()
                .street(addressEmbeddable.getStreet())
                .number(addressEmbeddable.getNumber())
                .neighborhood(addressEmbeddable.getNeighborhood())
                .complement(addressEmbeddable.getComplement())
                .city(addressEmbeddable.getCity())
                .state(addressEmbeddable.getState())
                .zipCode(new ZipCode(addressEmbeddable.getZipCode()))
                .build();
    }

}
