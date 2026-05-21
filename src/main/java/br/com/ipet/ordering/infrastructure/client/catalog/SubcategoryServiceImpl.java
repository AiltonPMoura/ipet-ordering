package br.com.ipet.ordering.infrastructure.client.catalog;

import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.category.ServiceSubcategory;
import br.com.ipet.ordering.domain.model.schedule.category.SubcategoryService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class SubcategoryServiceImpl implements SubcategoryService {

    private final CatalogAPIClient catalogAPIClient;

    @Override
    public Optional<ServiceSubcategory> ofCompanyId(CompanyId companyId) {
        return Optional.ofNullable(catalogAPIClient.findSubcategory(companyId));
    }
}
