package br.com.ipet.ordering.domain.model.order.shipping;

import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.ZipCode;
import lombok.Builder;

import java.time.LocalDate;

public interface ShippingCostService {
    CalculationResult calculate(CalculationRequest request);

    @Builder
    record CalculationRequest(ZipCode origin, ZipCode destination) {}

    @Builder
    record CalculationResult(Money cost, LocalDate expetedDate) {}
}
