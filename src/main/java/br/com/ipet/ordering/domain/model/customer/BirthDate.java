package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.FieldValidator;

import java.time.LocalDate;
import java.time.Period;

public record BirthDate(LocalDate value) {

    public BirthDate {
        FieldValidator.requiresNonNull("birthDate value", value);

        if (value.isAfter(LocalDate.now()))
            throw new BirthDateMustBeInPast();
    }

    public int age() {
        return Period.between(value, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
