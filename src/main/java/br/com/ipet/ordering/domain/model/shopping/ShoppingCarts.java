package br.com.ipet.ordering.domain.model.shopping;

import br.com.ipet.ordering.domain.model.RemoveCapableRepository;
import br.com.ipet.ordering.domain.model.customer.CustomerId;

public interface ShoppingCarts extends RemoveCapableRepository<ShoppingCart, ShoppingCartId> {
    ShoppingCart ofCustomer(CustomerId customerId);
}
