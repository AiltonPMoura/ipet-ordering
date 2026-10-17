package br.com.ipet.ordering.infrastructure.client.catalog.serviceoffering;

import java.math.BigDecimal;
import java.util.UUID;

public record AppointmentServiceResponse(UUID id,
                                         String type,
                                         String category,
                                         String description,
                                         Integer duration,
                                         BigDecimal price,
                                         String petSize) {
}
