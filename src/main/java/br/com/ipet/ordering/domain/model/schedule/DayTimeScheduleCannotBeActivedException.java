package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;

public class DayTimeScheduleCannotBeActivedException extends DomainException {
    public DayTimeScheduleCannotBeActivedException(String param) {
        super(param);
    }
}
