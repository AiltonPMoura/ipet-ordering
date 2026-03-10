package br.com.ipet.ordering.domain.model.service;

import br.com.ipet.ordering.domain.model.commons.valueobject.Service;
import br.com.ipet.ordering.domain.model.schedule.ServiceSubCategory;

import java.util.Set;

public interface ServiceDomain {
    Set<Service> servicesByServiceType(String type);
}
