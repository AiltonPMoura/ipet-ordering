package br.com.ipet.ordering.domain.model.transport;

import br.com.ipet.ordering.domain.model.commons.valueobject.Money;

public record PetTransportPricingResult(int requiredTrips, Money totalTransportCost) {
}
