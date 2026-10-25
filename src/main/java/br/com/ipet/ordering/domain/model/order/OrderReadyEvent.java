package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import br.com.ipet.ordering.domain.model.commons.id.CustomerId;

import java.time.OffsetDateTime;

public record OrderReadyEvent(OrderId orderId,
                              CustomerId customerId,
                              CompanyId companyId,
                              OffsetDateTime readyAt) {
}
