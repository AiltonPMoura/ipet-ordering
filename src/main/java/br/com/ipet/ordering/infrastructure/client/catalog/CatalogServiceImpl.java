package br.com.ipet.ordering.infrastructure.client.catalog;

import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.product.Product;
import br.com.ipet.ordering.domain.model.product.ProductCatalogService;
import br.com.ipet.ordering.domain.model.product.ProductDescription;
import br.com.ipet.ordering.domain.model.product.ProductId;
import br.com.ipet.ordering.domain.model.product.ProductName;
import br.com.ipet.ordering.domain.model.schedule.category.ServiceCategory;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceOfferingCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CatalogServiceImpl implements ProductCatalogService, ServiceOfferingCatalogService {

    private final CatalogAPIClient catalogAPIClient;

    @Override
    public Optional<Product> ofId(ProductId productId) {
        var productResponse = catalogAPIClient.findProductById(productId.value());
        return Optional.of(Product.builder()
                .id(new ProductId(productResponse.id()))
                .companyId(new CompanyId(productResponse.companyId()))
                .name(new ProductName(productResponse.name()))
                .description(new ProductDescription(productResponse.description()))
                .price(new Money(productResponse.price()))
                .inStock(productResponse.inStock())
                .build());
    }

    @Override
    public Optional<Object> ofId(Object id) {
        return Optional.empty();
    }

    @Override
    public Optional<ServiceCategory> ofCompanyId(CompanyId companyId) {
        return Optional.of(ServiceCategory.valueOf(catalogAPIClient.findSubcategory(companyId).name()));
    }

}
