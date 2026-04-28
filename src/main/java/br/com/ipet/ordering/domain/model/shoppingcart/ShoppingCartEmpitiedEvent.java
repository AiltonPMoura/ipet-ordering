package br.com.ipet.ordering.domain.model.shoppingcart;

import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;

import java.time.OffsetDateTime;

public record ShoppingCartEmpitiedEvent(ShoppingCartId shoppingCartId,
                                        CustomerId customerId,
                                        CompanyId companyId,
                                        OffsetDateTime empitedAt) {
}
