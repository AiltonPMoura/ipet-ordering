package br.com.ipet.ordering.domain.model.service;

import br.com.ipet.ordering.domain.model.commons.valueobject.Service;

import java.util.Set;

public interface ServiceDomain {
    Set<Service> findByServiceType(String type);
}
