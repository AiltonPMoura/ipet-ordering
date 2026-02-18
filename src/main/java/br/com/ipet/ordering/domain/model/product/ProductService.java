package br.com.ipet.ordering.domain.model.product;

import br.com.ipet.ordering.domain.model.commons.valueobject.Product;

import java.util.UUID;

public interface ProductService {
    UUID create(Product product);
}
