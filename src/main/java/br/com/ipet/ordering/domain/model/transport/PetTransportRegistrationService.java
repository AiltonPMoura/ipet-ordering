package br.com.ipet.ordering.domain.model.transport;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import br.com.ipet.ordering.domain.model.company.CompanyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PetTransportRegistrationService {

    private final Companies companies;
    private final PetTransports petTransports;

    public PetTransport register(CompanyId companyId, Driver driver, Vehicle vehicle, PetCapacity capacity) {
        FieldValidator.requiresNonNull("companyId", companyId);
        FieldValidator.requiresNonNull("driver", driver);
        FieldValidator.requiresNonNull("vehicle", vehicle);
        FieldValidator.requiresNonNull("capacity", capacity);

        if (!companies.exists(companyId))
            throw new CompanyNotFoundException("");

        return PetTransport.createNew()
                .companyId(companyId)
                .driver(driver)
                .vehicle(vehicle)
                .capacity(capacity)
                .build();
    }

    public void validateRemoval(CompanyId companyId) {
        if (petTransports.countByCompany(companyId) <= 1)
            throw new CannotRemoveLastPetTransportException();
    }

}
