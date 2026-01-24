package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.util.IdGenerator;

import java.util.UUID;

public record AgendaId(UUID value) {

    public AgendaId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public AgendaId {
        FieldValidator.requiresNonNull("agenda id", value);
    }

}
