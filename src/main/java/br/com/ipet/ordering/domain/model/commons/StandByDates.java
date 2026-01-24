package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

import java.time.LocalDate;

public record StandByDates(LocalDate startDate, LocalDate endDate) {

    public StandByDates {
        FieldValidator.requiresNonNull("stand by start date", startDate);
        FieldValidator.requiresNonNull("stand by end date", endDate);
        FieldValidator.requireDateIsAfterNow("stand by start date", startDate);
        FieldValidator.requireEndDateIsAfterStartDate(startDate, endDate);
    }

    public boolean isBetween(LocalDate date) {
        return (this.startDate.isEqual(date) || this.startDate.isBefore(date))
                && (this.endDate.isEqual(date) || this.endDate.isAfter(date));
    }

    public boolean isNotBetween(LocalDate date) {
        return !isBetween(date);
    }

    public boolean isCurrent() {
        return isBetween(LocalDate.now());
    }
}
