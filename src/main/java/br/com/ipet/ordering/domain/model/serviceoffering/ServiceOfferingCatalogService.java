package br.com.ipet.ordering.domain.model.serviceoffering;

import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.category.ServiceCategory;

import java.util.Optional;

public interface ServiceOfferingCatalogService {
    Optional<Object> ofId(Object id);
    Optional<ServiceCategory> ofCompanyId(CompanyId companyId);
}
