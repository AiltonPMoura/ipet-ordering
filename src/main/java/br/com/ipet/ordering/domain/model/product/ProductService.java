package br.com.ipet.ordering.domain.model.product;

import br.com.ipet.ordering.domain.model.commons.Product;

import java.util.UUID;

public interface ProductService {
    UUID create(Product product);
}
