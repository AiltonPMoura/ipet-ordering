package br.com.ipet.ordering.domain.model.scheduling;

import br.com.ipet.ordering.domain.model.DomainException;

public class CannotChangePetException extends DomainException {
    public CannotChangePetException(String currentSize, String newSize) {
         super("Cannot change pet with different size. Current pet size: " + currentSize + ", new pet size: " + newSize);
    }
}
