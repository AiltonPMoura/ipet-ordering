package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record ScheduleName(String value) {

    public ScheduleName {
        FieldValidator.requiresNonNull("Schedule name", value);
    }

}
