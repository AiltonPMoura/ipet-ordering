package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.FieldValidator;

import java.time.LocalDate;
import java.time.ZoneOffset;

public record LockedDate(LocalDate date) {

    public LockedDate {
        FieldValidator.requiresNonNull("date", date);

        if (!date().isAfter(LocalDate.now(ZoneOffset.UTC)))
            throw new LockedDateMustBeAfterNow("");
    }

}
