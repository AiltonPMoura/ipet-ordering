package br.com.ipet.ordering.domain.model;

public class DomainEntityNotFoundException extends DomainException {

    public DomainEntityNotFoundException(String messageKey, String value) {
        super(messageKey, value);
    }

}
