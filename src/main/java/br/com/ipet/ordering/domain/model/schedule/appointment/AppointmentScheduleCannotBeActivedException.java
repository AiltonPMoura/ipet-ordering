package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.DomainException;

public class AppointmentScheduleCannotBeActivedException extends DomainException {
    public AppointmentScheduleCannotBeActivedException(String param) {
        super(param);
    }
}
