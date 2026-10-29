package br.com.ipet.ordering.infrastructure.persistence.transport;

import br.com.ipet.ordering.domain.model.transport.PetTransport;
import br.com.ipet.ordering.domain.model.transport.Driver;
import br.com.ipet.ordering.domain.model.transport.Vehicle;
import br.com.ipet.ordering.infrastructure.persistence.company.CompanyPersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetTransportPersistenceMapper {

    private final CompanyPersistenceRepository companyPersistenceRepository;

    public PetTransportPersistenceEntity fromDomain(PetTransport petTransport) {
        return merge(new PetTransportPersistenceEntity(), petTransport);
    }

    public PetTransportPersistenceEntity merge(PetTransportPersistenceEntity persistenceEntity, PetTransport petTransport) {
        persistenceEntity.setId(petTransport.id().value());
        persistenceEntity.setCompany(companyPersistenceRepository.getReferenceById(petTransport.companyId().value()));
        persistenceEntity.setDriver(this.toDriverEmbeddable(petTransport.driver()));
        persistenceEntity.setVehicle(this.toVehicleEmbeddable(petTransport.vehicle()));
        persistenceEntity.setPetCapacity(petTransport.capacity().value());
        persistenceEntity.setStatus(petTransport.status().name());
        persistenceEntity.setRegisteredAt(petTransport.registeredAt());
        persistenceEntity.addEvents(petTransport.domainEvents());
        return persistenceEntity;
    }

    private DriverEmbeddable toDriverEmbeddable(Driver driver) {
        return DriverEmbeddable.builder()
                .firstName(driver.fullName().firstName())
                .lastName(driver.fullName().lastName())
                .document(driver.document().value())
                .build();
    }

    private VehicleEmbeddable toVehicleEmbeddable(Vehicle vehicle) {
        return VehicleEmbeddable.builder()
                .licensePlate(vehicle.licensePlate().value())
                .brand(vehicle.brand())
                .model(vehicle.model())
                .build();
    }
}
