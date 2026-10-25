package br.com.ipet.ordering.domain.model.commons.routing;

import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.ZipCode;
import lombok.Builder;

import java.time.LocalDate;

public interface RoutePricingService {
    CalculationResult calculate(CalculationRequest request);

    CalculationRouteCost calculateRouteCost(CalculationRequest request);

    @Builder
    record CalculationRequest(ZipCode origin, ZipCode destination) {}

    @Builder
    record CalculationResult(Money cost, LocalDate expetedDate) {}

    @Builder
    record CalculationRouteCost(Money cost) {}
}
