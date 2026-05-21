package br.com.ipet.ordering.infrastructure.client.catalog;

import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.category.ServiceSubcategory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface CatalogAPIClient {

    @GetExchange("/services/subcategory/{companyId}")
    ServiceSubcategory findSubcategory(@PathVariable CompanyId companyId);

}
