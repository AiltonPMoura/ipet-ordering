package br.com.ipet.ordering.infrastructure.client.shipping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface ShippingAPIClient {

    @PostExchange("/api/delivery-cost")
    DeliveryCostResponse calculate(@RequestBody DeliveryCostRequest request);

}
