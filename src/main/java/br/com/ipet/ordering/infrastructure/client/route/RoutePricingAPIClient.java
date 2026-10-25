package br.com.ipet.ordering.infrastructure.client.route;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface RoutePricingAPIClient {

    @PostExchange("/route-pricing")
    RouteResponse calculate(@RequestBody RoutetRequest request);

}
