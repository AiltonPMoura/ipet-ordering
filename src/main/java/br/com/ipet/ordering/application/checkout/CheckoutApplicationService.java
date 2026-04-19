package br.com.ipet.ordering.application.checkout;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.ZipCode;
import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.company.CompanyNotFoundException;
import br.com.ipet.ordering.domain.model.customer.CustomerAddressId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.customer.CustomerNotFoundException;
import br.com.ipet.ordering.domain.model.customer.Customers;
import br.com.ipet.ordering.domain.model.order.Billing;
import br.com.ipet.ordering.domain.model.order.CheckoutService;
import br.com.ipet.ordering.domain.model.order.DeliveryCompany;
import br.com.ipet.ordering.domain.model.order.Orders;
import br.com.ipet.ordering.domain.model.order.PaymentMethod;
import br.com.ipet.ordering.domain.model.order.Shipping;
import br.com.ipet.ordering.domain.model.order.shipping.ShippingCostService;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCartId;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCarts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckoutApplicationService {

    private final Orders orders;
    private final CheckoutService checkoutService;
    private final ShoppingCarts shoppingCarts;
    private final Companies companies;
    private final Customers customers;
    private final ShippingCostService shippingCostService;

    public UUID checkout(CheckoutInput input) {
        FieldValidator.requiresNonNull("input", input);

        var shoppingCart = shoppingCarts.ofId(new ShoppingCartId(input.getShoppingCartId()))
                .orElseThrow(() -> new ShoppingCartNotFoundException(""));

        var company = companies.ofId(new CompanyId(input.getCompanyId()))
                .orElseThrow(() -> new CompanyNotFoundException(""));

        var customer = customers.ofId(new CustomerId(input.getCustomerId()))
                .orElseThrow(() -> new CustomerNotFoundException(""));

        var customerAddress = customer.findAddress(new CustomerAddressId(input.getCustomerAddressId()));

        var shippingCost = this.calculateShippingCost(company.address().zipCode(), customerAddress.zipCode());

        var order = checkoutService.checkout(shoppingCart, new CompanyId(company.id().value()),
                Billing.builder()
                        .fullName(customer.fullName())
                        .document(customer.document())
                        .email(customer.email())
                        .phone(customer.phone())
                        .address(customer.principalAddress())
                        .build(),
                Shipping.builder()
                        .address(customerAddress)
                        .cost(shippingCost.cost())
                        .expectedDate(shippingCost.expetedDate())
                        .build(),
                DeliveryCompany.builder()
                        .companyName(company.name())
                        .document(company.document())
                        .email(company.email())
                        .phone(company.phone())
                        .address(company.address())
                        .build(),
                PaymentMethod.valueOf(input.getPaymentMethod())
        );

        orders.add(order);

        return order.id().value();
    }

    private ShippingCostService.CalculationResult calculateShippingCost(ZipCode origin, ZipCode destiny) {
        return shippingCostService.calculate(ShippingCostService.CalculationRequest.builder()
                .origin(origin)
                .destination(destiny)
                .build());
    }

}
