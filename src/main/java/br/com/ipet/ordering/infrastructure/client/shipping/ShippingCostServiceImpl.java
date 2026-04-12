package br.com.ipet.ordering.infrastructure.client.shipping;

import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.order.shipping.ShippingCostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ShippingCostServiceImpl implements ShippingCostService {

    private final ShippingAPIClient shippingAPIClient;

    @Override
    public CalculationResult calculate(CalculationRequest request) {
        var response = shippingAPIClient.calculate(
                new DeliveryCostRequest(request.origin().value(), request.destination().value())
        );

        return CalculationResult.builder()
                .cost(new Money(response.getDeliveryCost()))
                .expetedDate(LocalDate.now().plusDays(response.getEstimatedDaysToDelivery()))
                .build();
    }
}
