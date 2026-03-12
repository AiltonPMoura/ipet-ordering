package br.com.ipet.ordering.domain.model.service;

import br.com.ipet.ordering.domain.model.DomainException;

public class ServiceNotFoundException extends DomainException {
    public ServiceNotFoundException() {
        super("");
    }
}
