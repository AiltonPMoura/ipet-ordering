package br.com.ipet.ordering.infrastructure.client.catalog;

import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.category.ServiceCategory;
import br.com.ipet.ordering.infrastructure.client.catalog.product.ProductResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.UUID;

public interface CatalogAPIClient {

    @GetExchange(value = "/v1/companies/{companyId}/products/{productId}", accept = "application/json")
    ProductResponse findProductById(UUID companyId, UUID productId);

    @GetExchange(value = "/v1/companies/{companyId}/services/subcategory", accept = "application/json")
    ServiceCategory findSubcategory(@PathVariable CompanyId companyId);

}
