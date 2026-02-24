package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class AgendaIsNotDraftToChangeException extends DomainException {

    private final String agendaId;

    public AgendaIsNotDraftToChangeException(String agendaId) {
        super(MessageCode.ERROR_AGENDA_IS_NOT_DRAFT_TO_CHANGE);
        this.agendaId = agendaId;
    }
}
