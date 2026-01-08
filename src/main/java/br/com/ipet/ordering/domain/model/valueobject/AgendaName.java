package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record AgendaName(String value) {

    public AgendaName {
        FieldValidator.requiresNonNull("Agenda name", value);
    }

}
