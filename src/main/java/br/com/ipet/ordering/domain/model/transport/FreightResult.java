package br.com.ipet.ordering.domain.model.transport;

import br.com.ipet.ordering.domain.model.commons.valueobject.Money;

public record FreightResult(int requiredTrips, Money totalFreightCost) {
}
