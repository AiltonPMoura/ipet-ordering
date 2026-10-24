package br.com.ipet.ordering.application.transport.management;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PetTransportInput(UUID companyId,
                                DriverInput driver,
                                VehicleInput vehicle,
                                Integer petCapacity) {
}
