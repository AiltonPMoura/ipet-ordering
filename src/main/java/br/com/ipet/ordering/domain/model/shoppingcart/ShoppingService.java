package br.com.ipet.ordering.domain.model.shoppingcart;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.company.CompanyNotFoundException;
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
    private final Companies companies;

    public ShoppingCart startShopping(CustomerId customerId, CompanyId companyId) {
        FieldValidator.requiresNonNull("customerId", customerId);
        FieldValidator.requiresNonNull("companyId", companyId);

        if (!customers.exists(customerId))
            throw new CustomerNotFoundException("");

        if (!companies.exists(companyId))
            throw new CompanyNotFoundException("");

        if (shoppingCarts.ofCustomer(customerId).isPresent())
            throw new CustomerAlreadyHaveShoppingCartException("");

        return ShoppingCart.startShopping(customerId, companyId);
    }

}
