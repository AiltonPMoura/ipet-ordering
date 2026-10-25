package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.commons.enums.PaymentMethod;
import br.com.ipet.ordering.domain.model.commons.valueobject.Billing;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import br.com.ipet.ordering.domain.model.commons.id.CustomerId;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    public Order checkout(ShoppingCart shoppingCart, CustomerId customerId, CompanyId companyId,
                          Billing billing, Shipping shipping,
                          DeliveryCompany deliveryCompany, PaymentMethod paymentMethod) {

        if (shoppingCart.isEmpty())
            throw new ShoppingCartCantProceedToCheckoutException("");

        var order = Order.draft(shoppingCart.customerId(), companyId);
        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changeDeliveryCompany(deliveryCompany);
        order.changePaymentMethod(paymentMethod);

        shoppingCart.items().forEach(item -> order.addItem(item.product(), item.quantity()));

        order.place(customerId, companyId);
        shoppingCart.empty(customerId);

        return order;
    }

}
