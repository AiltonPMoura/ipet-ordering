package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;

public class ScheduleDoesNotBelongToCompany extends DomainException {
    public ScheduleDoesNotBelongToCompany(String message) {
        super(message);
    }
}
