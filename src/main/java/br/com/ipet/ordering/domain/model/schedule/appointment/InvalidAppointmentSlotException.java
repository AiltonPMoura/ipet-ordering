package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class InvalidAppointmentSlotException extends DomainException {

    public InvalidAppointmentSlotException() {
        super(MessageCode.ERROR_INVALID_APPOINTMENT_SLOT,
                String.valueOf(AppointmentSlotPolicy.INCREMENT_MINUTES));
    }
}
