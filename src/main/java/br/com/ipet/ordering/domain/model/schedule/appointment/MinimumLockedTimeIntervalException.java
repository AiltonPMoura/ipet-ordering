package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.DomainException;

public class MinimumLockedTimeIntervalException extends DomainException {
    public MinimumLockedTimeIntervalException() {
        super("Locked time interval is below the minimum required.");
    }
}
