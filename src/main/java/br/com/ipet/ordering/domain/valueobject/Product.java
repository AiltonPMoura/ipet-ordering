package br.com.ipet.ordering.domain.valueobject;

public record Product(
        String name,
        String description,
        Money price) {

}
