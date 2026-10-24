package br.com.ipet.ordering.domain.model.transport;

import br.com.ipet.ordering.domain.model.DomainException;

public record PetCapacity(Integer value) {

    public PetCapacity {
        if (value < 1 || value > 10) {
            throw new DomainException("Pet capacity must be between 1 and 10.");
        }
    }

}
