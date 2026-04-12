package br.com.ipet.ordering.infrastructure.client.catalog;

import br.com.ipet.ordering.domain.model.product.Product;
import br.com.ipet.ordering.domain.model.product.ProductCatalogService;
import br.com.ipet.ordering.domain.model.product.ProductId;

import java.util.Optional;

public class ProductCatalogServiceImpl implements ProductCatalogService {
    @Override
    public Optional<Product> ofId(ProductId id) {
        return Optional.empty();
    }
}
