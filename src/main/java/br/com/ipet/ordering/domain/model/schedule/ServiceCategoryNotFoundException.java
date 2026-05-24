package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;

import java.util.UUID;

public class ServiceCategoryNotFoundException extends DomainException {
    public ServiceCategoryNotFoundException(UUID companyId) {
        super(companyId.toString());
    }
}
