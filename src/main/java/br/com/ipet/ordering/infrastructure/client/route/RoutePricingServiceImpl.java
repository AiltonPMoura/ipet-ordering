package br.com.ipet.ordering.infrastructure.client.route;

import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.routing.RoutePricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class RoutePricingServiceImpl implements RoutePricingService {

    private final RoutePricingAPIClient routePricingAPIClient;

    @Override
    public CalculationResult calculate(CalculationRequest request) {
        var response = routePricingAPIClient.calculate(
                new RouteCostRequest(request.origin().value(), request.destination().value())
        );

        return CalculationResult.builder()
                .cost(new Money(response.getDeliveryCost()))
                .expetedDate(LocalDate.now(ZoneOffset.UTC).plusDays(response.getEstimatedDaysToDelivery()))
                .build();
    }

    @Override
    public CalculationRouteCost calculateRouteCost(CalculationRequest request) {
        var response = routePricingAPIClient.calculateRouteCost(
                new RouteCostRequest(request.origin().value(), request.destination().value())
        );

        return CalculationRouteCost.builder().cost(new Money(response.getDeliveryCost())).build();
    }
}
