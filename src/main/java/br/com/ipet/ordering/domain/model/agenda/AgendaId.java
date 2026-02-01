package br.com.ipet.ordering.domain.model.agenda;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record AgendaId(UUID value) {

    public AgendaId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public AgendaId {
        FieldValidator.requiresNonNull("agenda id", value);
    }

}
