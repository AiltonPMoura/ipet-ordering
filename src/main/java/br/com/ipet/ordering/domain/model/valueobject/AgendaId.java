package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

import java.util.UUID;

public record AgendaId(UUID value) {

    public AgendaId {
        FieldValidator.requiresNonNull("agenda id", value);
    }

}
