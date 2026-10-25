package br.com.ipet.ordering.domain.model.transport;

import br.com.ipet.ordering.domain.model.RemoveCapableRepository;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;

import java.util.Optional;

public interface PetTransports extends RemoveCapableRepository<PetTransport, PetTransportId> {

    Optional<PetTransport> ofIdAndCompanyId(PetTransportId id, CompanyId companyId);
    Optional<PetTransport> ofCompanyWithHighestCapacity(CompanyId companyId);
    long countByCompany(CompanyId companyId);

}
