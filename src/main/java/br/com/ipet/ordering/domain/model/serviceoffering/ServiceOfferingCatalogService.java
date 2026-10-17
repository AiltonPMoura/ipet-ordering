package br.com.ipet.ordering.domain.model.serviceoffering;

import br.com.ipet.ordering.domain.model.company.CompanyId;

import java.util.Optional;

public interface ServiceOfferingCatalogService {
    Optional<AppointmentService> ofId(ServiceOfferingId id, CompanyId companyId);
}
