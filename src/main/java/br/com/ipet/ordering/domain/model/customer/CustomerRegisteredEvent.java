package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;

import java.time.OffsetDateTime;

public record CustomerRegisteredEvent(CustomerId customerId,
                                      FullName fullName,
                                      Email email,
                                      OffsetDateTime registeredAd) {
}
