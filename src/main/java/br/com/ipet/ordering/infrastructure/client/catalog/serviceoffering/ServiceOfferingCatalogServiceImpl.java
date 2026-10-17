package br.com.ipet.ordering.infrastructure.client.catalog.serviceoffering;

import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.serviceoffering.AppointmentService;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceOfferingCatalogService;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceOfferingId;
import br.com.ipet.ordering.infrastructure.client.catalog.CatalogAPIClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceOfferingCatalogServiceImpl implements ServiceOfferingCatalogService {

    private final CatalogAPIClient catalogAPIClient;

    @Override
    public Optional<AppointmentService> ofId(ServiceOfferingId serviceOfferingId, CompanyId companyId) {
        var serviceResponse = catalogAPIClient.findServiceById(companyId.value(), serviceOfferingId.value());
        return Optional.of(AppointmentService.builder()
                .id(new ServiceOfferingId(serviceResponse.id()))
                .type(serviceResponse.type())
                .category(serviceResponse.category())
                .description(serviceResponse.description())
                .duration(serviceResponse.duration())
                .price(new Money(serviceResponse.price()))
                .petSize(serviceResponse.petSize())
                .build());
    }

}
