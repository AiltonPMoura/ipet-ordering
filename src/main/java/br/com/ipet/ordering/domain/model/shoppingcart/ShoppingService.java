package br.com.ipet.ordering.domain.model.shoppingcart;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.customer.CustomerNotFoundException;
import br.com.ipet.ordering.domain.model.customer.Customers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingService {

    private final ShoppingCarts shoppingCarts;
    private final Customers customers;

    public ShoppingCart startShopping(CustomerId customerId) {
        FieldValidator.requiresNonNull("customerId", customerId);

        if (!customers.exists(customerId))
            throw new CustomerNotFoundException("");

        if (shoppingCarts.ofCustomer(customerId).isPresent())
            throw new CustomerAlreadyHaveShoppingCartException("");

        return ShoppingCart.startShopping(customerId);
    }

    public void verifyIfBelongToTheCustomer(ShoppingCart shoppingCart, CustomerId customerId) {
        if (!shoppingCart.customerId().equals(customerId))
            throw new ShoppingCartDoesNotBelongToTheCustomer("");
    }

}
