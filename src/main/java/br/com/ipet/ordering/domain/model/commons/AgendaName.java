package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record AgendaName(String value) {

    public AgendaName {
        FieldValidator.requiresNonNull("Agenda name", value);
    }

}
