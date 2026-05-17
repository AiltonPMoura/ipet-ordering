package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.FieldValidator;

import java.time.LocalDate;

public record LockedDate(LocalDate date) {

    public LockedDate {
        FieldValidator.requiresNonNull("date", date);

        if (!date().isAfter(LocalDate.now()))
            throw new LockedDateMustBeAfterNow("");
    }

}
