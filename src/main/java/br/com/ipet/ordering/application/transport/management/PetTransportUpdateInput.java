package br.com.ipet.ordering.application.transport.management;

import lombok.Builder;

@Builder
public record PetTransportUpdateInput(DriverInput driver, Integer petCapacity) {
}
