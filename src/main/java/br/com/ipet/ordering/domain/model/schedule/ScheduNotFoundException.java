package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;

public class ScheduNotFoundException extends DomainException {
    public ScheduNotFoundException(String s) {
        super(s);
    }
}
