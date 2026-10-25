package br.com.ipet.ordering.domain.model.shoppingcart;

import br.com.ipet.ordering.domain.model.commons.id.CustomerId;

import java.time.OffsetDateTime;

public record ShoppingCartDiscartedEvent(ShoppingCartId shoppingCartId,
                                         CustomerId customerId,
                                         OffsetDateTime discartedAt) {
}
