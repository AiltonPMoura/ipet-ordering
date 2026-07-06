package br.com.ipet.ordering.infrastructure.client.catalog.product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(UUID id,
                              UUID companyId,
                              String name,
                              String description,
                              BigDecimal price,
                              boolean inStock) {
}
