package br.com.ipet.ordering.application.transport.management;

import lombok.Builder;

@Builder
public record DriverInput(String firstName,
                          String lastName,
                          String document) {
}
