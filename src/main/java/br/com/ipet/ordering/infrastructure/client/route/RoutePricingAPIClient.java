package br.com.ipet.ordering.infrastructure.client.route;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface RoutePricingAPIClient {

    @PostExchange("/delivery-cost")
    DeliveryCostResponse calculate(@RequestBody RouteCostRequest request);

    @PostExchange("/route-cost")
    RouteCostResponse calculateRouteCost(@RequestBody RouteCostRequest request);

}
