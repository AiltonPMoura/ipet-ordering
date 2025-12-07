package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class SchedulingIsNotDraftToChangeException extends DomainException {

    private final String schedulingId;

    public SchedulingIsNotDraftToChangeException(String schedulingId) {
        super(MessageCode.ERROR_SCHEDULING_IS_NOT_DRAFT_TO_CHANE);
        this.schedulingId = schedulingId;
    }
}
