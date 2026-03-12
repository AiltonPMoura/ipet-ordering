package br.com.ipet.ordering.infrastructure.rest.service;

import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Service;
import br.com.ipet.ordering.domain.model.commons.valueobject.ServiceDescription;
import br.com.ipet.ordering.domain.model.commons.valueobject.ServiceId;
import br.com.ipet.ordering.domain.model.service.ServiceDomain;

import java.util.Set;

public class ServiceDomainImpl implements ServiceDomain {
    @Override
    public Set<Service> findByServiceType(String type) {
        return Set.of(
                new Service(
                        new ServiceId(),
                        "DOG_BATH",
                        "SMALL",
                        new ServiceDescription(""),
                        new Money("10")
                ),
                new Service(
                        new ServiceId(),
                        "DOG_BATH",
                        "LARGE",
                        new ServiceDescription(""),
                        new Money("20")
                )
        );
    }
}
