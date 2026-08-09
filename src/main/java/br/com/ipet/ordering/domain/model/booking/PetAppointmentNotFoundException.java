package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class PetAppointmentNotFoundException extends DomainException {

    private final String[] fields;

    public PetAppointmentNotFoundException(String... fields) {
        super(MessageCode.ERROR_SCHEDULING_PET_NOT_FOUND);
        this.fields = fields;
    }
}
