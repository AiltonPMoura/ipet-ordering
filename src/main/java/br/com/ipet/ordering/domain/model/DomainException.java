package br.com.ipet.ordering.domain.model;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

    private final String[] values;

    public DomainException(String messageKey, String... values) {
        super(messageKey);
        this.values = values;
    }

}
