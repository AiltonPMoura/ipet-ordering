package br.com.ipet.ordering.domain.model.commons;

import lombok.Builder;

@Builder
public record Pet(String name,
                  String size,
                  String type,
                  String gender,
                  String breed) {
}
