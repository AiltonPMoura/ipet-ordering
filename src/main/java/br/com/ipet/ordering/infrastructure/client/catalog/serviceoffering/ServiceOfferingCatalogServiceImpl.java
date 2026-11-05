package br.com.ipet.ordering.infrastructure.client.catalog.serviceoffering;

import br.com.ipet.ordering.domain.model.commons.enums.PetSize;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.serviceoffering.AppointmentService;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceCategory;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceId;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceOfferingCatalogService;
import br.com.ipet.ordering.infrastructure.client.catalog.CatalogAPIClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingCatalogServiceImpl implements ServiceOfferingCatalogService {

    private static final UUID MEDIUM_PET_SERVICE_ID = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
    private static final UUID SMALL_PET_SERVICE_ID = UUID.fromString("297d9874-69dd-462c-a7de-8dcea1ee12db");

    private final CatalogAPIClient catalogAPIClient;

    /*@Override
    public Optional<AppointmentService> ofIdAndCompanyId(ServiceId serviceId, CompanyId companyId) {
        var serviceResponse = catalogAPIClient.findServiceById(companyId.value(), serviceId.value());
        return Optional.of(this.buildAppointmentService(serviceResponse));
    }*/

    @Override
    public Optional<AppointmentService> ofIdAndCompanyId(ServiceId serviceId, CompanyId companyId) {
        return Optional.of(buildMockAppointmentService(serviceId));
    }

    @Override
    public Set<AppointmentService> ofIdsAndCompanyId(Set<ServiceId> ids, CompanyId companyId) {
        return ids.stream()
                .map(this::buildMockAppointmentService)
                .collect(Collectors.toSet());
    }

    private AppointmentService buildMockAppointmentService(ServiceId serviceId) {
        PetSize petSize = PetSize.MEDIUM;
        String type = "BATH";
        int duration = 60;
        Money price = new Money("50");

        if (serviceId.value().equals(MEDIUM_PET_SERVICE_ID)) {
            petSize = PetSize.MEDIUM;
        } else if (serviceId.value().equals(SMALL_PET_SERVICE_ID)) {
            petSize = PetSize.SMALL;
            type = "GROOMING_SCISSOR";
            duration = 90;
            price = new Money("100");
        }

        return AppointmentService.builder()
                .id(serviceId)
                .type(type)
                .category(ServiceCategory.valueOf("HYGIENE"))
                .description("Bath service for pets")
                .duration(duration)
                .price(price)
                .petSize(petSize)
                .build();
    }

    /*@Override
    public Set<AppointmentService> ofIdsAndCompanyId(Set<ServiceId> ids, CompanyId companyId) {
        var serviceIds = ids.stream().map(ServiceId::value).toList();

        return catalogAPIClient.findAllServicesById(companyId.value(), serviceIds).stream()
                .map(this::buildAppointmentService)
                .collect(Collectors.toSet());
    }*/

    private AppointmentService buildAppointmentService(AppointmentServiceResponse serviceResponse) {
        return AppointmentService.builder()
                .id(new ServiceId(serviceResponse.id()))
                .type(serviceResponse.type())
                .category(ServiceCategory.valueOf(serviceResponse.category()))
                .description(serviceResponse.description())
                .duration(serviceResponse.duration())
                .price(new Money(serviceResponse.price()))
                .petSize(PetSize.valueOf(serviceResponse.petSize()))
                .build();
    }

}
