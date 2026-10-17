package br.com.ipet.ordering.infrastructure.client.catalog.serviceoffering;

import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.pet.PetSize;
import br.com.ipet.ordering.domain.model.serviceoffering.AppointmentService;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceCategory;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceOfferingCatalogService;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceId;
import br.com.ipet.ordering.infrastructure.client.catalog.CatalogAPIClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingCatalogServiceImpl implements ServiceOfferingCatalogService {

    private final CatalogAPIClient catalogAPIClient;

    @Override
    public Optional<AppointmentService> ofIdAndCompanyId(ServiceId serviceId, CompanyId companyId) {
        var serviceResponse = catalogAPIClient.findServiceById(companyId.value(), serviceId.value());
        return Optional.of(this.buildAppointmentService(serviceResponse));
    }

    @Override
    public Set<AppointmentService> ofIdsAndCompanyId(Set<ServiceId> ids, CompanyId companyId) {
        var serviceIds = ids.stream().map(ServiceId::value).toList();

        return catalogAPIClient.findAllServicesById(companyId.value(), serviceIds).stream()
                .map(this::buildAppointmentService)
                .collect(Collectors.toSet());
    }

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
