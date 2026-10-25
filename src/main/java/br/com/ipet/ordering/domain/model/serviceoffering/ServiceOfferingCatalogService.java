package br.com.ipet.ordering.domain.model.serviceoffering;

import br.com.ipet.ordering.domain.model.commons.id.CompanyId;

import java.util.Optional;
import java.util.Set;

public interface ServiceOfferingCatalogService {
    Optional<AppointmentService> ofIdAndCompanyId(ServiceId id, CompanyId companyId);
    Set<AppointmentService> ofIdsAndCompanyId(Set<ServiceId> ids, CompanyId companyId);
}
