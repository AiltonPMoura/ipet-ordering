package br.com.ipet.ordering.domain.valueobject;

import lombok.Builder;

@Builder
public record Pet(String name,
                  String size,
                  String type,
                  String gender,
                  String breed) {
}
