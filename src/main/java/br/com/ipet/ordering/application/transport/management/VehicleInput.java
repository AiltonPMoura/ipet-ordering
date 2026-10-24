package br.com.ipet.ordering.application.transport.management;

import lombok.Builder;

@Builder
public record VehicleInput(String licensePlate,
                           String brand,
                           String model) {
}
