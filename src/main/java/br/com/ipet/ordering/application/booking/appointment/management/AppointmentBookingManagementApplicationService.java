package br.com.ipet.ordering.application.booking.appointment.management;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.booking.BookingPet;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingService;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookings;
import br.com.ipet.ordering.domain.model.booking.appointment.PetServicePair;
import br.com.ipet.ordering.domain.model.booking.PetBookingTransport;
import br.com.ipet.ordering.domain.model.commons.enums.PaymentMethod;
import br.com.ipet.ordering.domain.model.commons.routing.RoutePricingService;
import br.com.ipet.ordering.domain.model.commons.valueobject.ZipCode;
import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import br.com.ipet.ordering.domain.model.company.CompanyNotFoundException;
import br.com.ipet.ordering.domain.model.customer.CustomerAddressId;
import br.com.ipet.ordering.domain.model.commons.id.CustomerId;
import br.com.ipet.ordering.domain.model.customer.CustomerNotFoundException;
import br.com.ipet.ordering.domain.model.customer.Customers;
import br.com.ipet.ordering.domain.model.commons.id.PetId;
import br.com.ipet.ordering.domain.model.commons.enums.PetSize;
import br.com.ipet.ordering.domain.model.pet.Pet;
import br.com.ipet.ordering.domain.model.pet.Pets;
import br.com.ipet.ordering.domain.model.commons.id.ScheduleId;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentSchedules;
import br.com.ipet.ordering.domain.model.serviceoffering.AppointmentService;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceId;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceOfferingCatalogService;
import br.com.ipet.ordering.domain.model.transport.PetTransports;
import br.com.ipet.ordering.domain.model.transport.PetTransportPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    private final ServiceOfferingCatalogService serviceOfferingCatalogService;
    private final PetTransportPricingService petTransportPricingService;
    private final RoutePricingService routePricingService;
    private final AppointmentBookings appointmentBookings;
    private final AppointmentSchedules appointmentSchedules;
    private final Pets pets;
    private final PetTransports petTransports;
    private final Customers customers;
    private final Companies companies;

    public UUID create(AppointmentBookingInput input) {
        var appointmentSchedule = appointmentSchedules.ofId(new ScheduleId(input.appointmentScheduleId()))
                .orElseThrow(() -> new DomainException("Agenda não encontrada."));

        var customer = customers.ofId(new CustomerId(input.customerId()))
                .orElseThrow(() -> new CustomerNotFoundException(""));

        var company = companies.ofId(new CompanyId(input.companyId()))
                .orElseThrow(() -> new CompanyNotFoundException(""));

        var petTransport = petTransports.ofCompanyWithHighestCapacityAndActive(new CompanyId(input.companyId()))
                .orElseThrow(() -> new DomainException("Empresa não possui um transporte ativo para o agendamento."));

        var petsById = this.findCustomerPetsMappedById(input);
        var servicesById = this.findAppointmentServicesMappedById(input);
        var petServicePairs = this.buildPetServicePairs(input, petsById, servicesById);

        var customerAddress = customer.findAddress(new CustomerAddressId(input.customerAddressId()));
        var routeResult = this.calculateRoutePrice(company.address().zipCode(), customerAddress.zipCode());
        var petSizes = this.getPetSizes(petsById);
        var pricingResult = this.petTransportPricingService.calculate(petTransport, petSizes, routeResult.cost());

        var appointmentBooking = appointmentBookingService.book(
                new CustomerId(input.customerId()),
                appointmentSchedule,
                input.requestedStart(),
                petServicePairs,
                PetBookingTransport.builder()
                        .cost(pricingResult.totalTransportCost())
                        .address(customer.principalAddress())
                        .build(),
                PaymentMethod.valueOf(input.paymentMethod())
        );

        appointmentBookings.add(appointmentBooking);

        return appointmentBooking.id().value();
    }

    private Map<ServiceId, AppointmentService> findAppointmentServicesMappedById(AppointmentBookingInput input) {
        var serviceIds = input.petServices().stream()
                .map(appointmentInput -> new ServiceId(appointmentInput.serviceId()))
                .collect(Collectors.toSet());

        return serviceOfferingCatalogService.ofIdsAndCompanyId(serviceIds, new CompanyId(input.companyId())).stream()
                .collect(Collectors.toMap(AppointmentService::id, service -> service));
    }

    private Map<PetId, BookingPet> findCustomerPetsMappedById(AppointmentBookingInput input) {
        var petIds = input.petServices().stream()
                .map(appointmentInput -> new PetId(appointmentInput.petId()))
                .collect(Collectors.toSet());

        return pets.ofCustomerAndIds(new CustomerId(input.customerId()), petIds).stream()
                .map(this::buildBookingPet)
                .collect(Collectors.toMap(BookingPet::id, pet -> pet));
    }

    private Set<PetServicePair> buildPetServicePairs(AppointmentBookingInput input,
                                                     Map<PetId, BookingPet> petsById,
                                                     Map<ServiceId, AppointmentService> servicesById) {
        return input.petServices().stream()
                .map(appointmentInput -> {
                    var pet = Optional.ofNullable(petsById.get(new PetId(appointmentInput.petId())))
                            .orElseThrow(() -> new DomainException("Pet não encontrado: " + appointmentInput.petId()));

                    var service = Optional.ofNullable(servicesById.get(new ServiceId(appointmentInput.serviceId())))
                            .orElseThrow(() -> new DomainException("Serviço não encontrado: " + appointmentInput.serviceId()));

                    return new PetServicePair(pet, service);
                })
                .collect(Collectors.toSet());
    }

    private List<PetSize> getPetSizes(Map<PetId, BookingPet> petsById) {
        return petsById.values().stream().map(BookingPet::size).toList();
    }

    private RoutePricingService.CalculationResult calculateRoutePrice(ZipCode origin, ZipCode destination) {
        return routePricingService.calculate(RoutePricingService.CalculationRequest.builder()
                .origin(origin)
                .destination(destination)
                .build());
    }

    private BookingPet buildBookingPet(Pet pet) {
        return BookingPet.builder()
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
