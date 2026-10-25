package br.com.ipet.ordering.infrastructure.persistence.transport;

import br.com.ipet.ordering.domain.model.transport.PetTransport;
import br.com.ipet.ordering.domain.model.transport.PetTransportId;
import br.com.ipet.ordering.domain.model.transport.PetTransports;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PetTransportPersistenceProvider implements PetTransports {

    private final PetTransportPersistenceRepository petTransportRepository;
    private final PetTransportMapper petTransportMapper;
    private final PetTransportPersistenceMapper petTransportPersistenceMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<PetTransport> ofId(PetTransportId petTransportId) {
        return petTransportRepository.findById(petTransportId.value()).map(petTransportMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetTransport> ofIdAndCompanyId(PetTransportId petTransportId, CompanyId companyId) {
        return petTransportRepository.findByIdAndCompany_Id(petTransportId.value(), companyId.value())
                .map(petTransportMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetTransport> ofCompanyWithHighestCapacity(CompanyId companyId) {
        return petTransportRepository.findFirstByCompany_IdOrderByPetCapacityDesc(companyId.value())
                .map(petTransportMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(PetTransportId petTransportId) {
        return petTransportRepository.existsById(petTransportId.value());
    }

    @Override
    @Transactional
    public void add(PetTransport petTransport) {
        petTransportRepository.findById(petTransport.id().value())
                .ifPresentOrElse(
                        petTransportPersistence -> this.update(petTransportPersistence, petTransport),
                        () -> this.insert(petTransport));
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return petTransportRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByCompany(CompanyId companyId) {
        return petTransportRepository.countByCompany_Id(companyId.value());
    }

    @Override
    @Transactional
    public void remove(PetTransport petTransport) {
        petTransportRepository.deleteById(petTransport.id().value());
    }

    @Override
    @Transactional
    public void remove(PetTransportId id) {
        petTransportRepository.deleteById(id.value());
    }

    private void insert(PetTransport petTransport) {
        var persistenceEntity = petTransportPersistenceMapper.fromDomain(petTransport);
        petTransportRepository.saveAndFlush(persistenceEntity);
    }

    private void update(PetTransportPersistenceEntity persistenceEntity, PetTransport petTransport) {
        persistenceEntity = petTransportPersistenceMapper.merge(persistenceEntity, petTransport);
        petTransportRepository.saveAndFlush(persistenceEntity);
    }

}
