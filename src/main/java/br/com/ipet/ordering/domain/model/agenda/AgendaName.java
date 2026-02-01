package br.com.ipet.ordering.domain.model.agenda;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record AgendaName(String value) {

    public AgendaName {
        FieldValidator.requiresNonNull("Agenda name", value);
    }

}
