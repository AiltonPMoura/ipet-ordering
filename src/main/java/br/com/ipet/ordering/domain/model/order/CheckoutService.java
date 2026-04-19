package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    public Order checkout(ShoppingCart shoppingCart, CompanyId companyId,
                          Billing billing, Shipping shipping,
                          DeliveryCompany deliveryCompany, PaymentMethod paymentMethod) {

        if (shoppingCart.isEmpty())
            throw new ShoppingCartCantProceedToCheckoutException("");

        //Specification, verifyIFPertenceCustomer

        var order = Order.draft(shoppingCart.customerId(), companyId);
        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changeDeliveryCompany(deliveryCompany);
        order.changePaymentMethod(paymentMethod);

        shoppingCart.items().forEach(item -> order.addItem(item.product(), item.quantity()));

        order.place();
        shoppingCart.empty();

        return order;
    }

}
