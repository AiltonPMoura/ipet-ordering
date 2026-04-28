package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;

import java.time.OffsetDateTime;

public record OrderPaidEvent(OrderId orderId,
                             CustomerId customerId,
                             CompanyId companyId,
                             OffsetDateTime paidAt) {
}
