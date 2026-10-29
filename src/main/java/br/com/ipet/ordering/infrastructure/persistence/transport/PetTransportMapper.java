package br.com.ipet.ordering.infrastructure.persistence.transport;

import br.com.ipet.ordering.domain.model.commons.document.DocumentFactory;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import br.com.ipet.ordering.domain.model.transport.Driver;
import br.com.ipet.ordering.domain.model.transport.LicensePlate;
import br.com.ipet.ordering.domain.model.transport.PetCapacity;
import br.com.ipet.ordering.domain.model.transport.PetTransport;
import br.com.ipet.ordering.domain.model.transport.PetTransportId;
import br.com.ipet.ordering.domain.model.transport.PetTransportStatus;
import br.com.ipet.ordering.domain.model.transport.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class PetTransportMapper {

    public PetTransport toDomain(PetTransportPersistenceEntity petTransportPersistence) {
        return PetTransport.existing()
                .id(new PetTransportId(petTransportPersistence.getId()))
                .companyId(new CompanyId(petTransportPersistence.getCompanyId()))
                .driver(this.toDriver(petTransportPersistence.getDriver()))
                .vehicle(this.toVehicle(petTransportPersistence.getVehicle()))
                .capacity(new PetCapacity(petTransportPersistence.getPetCapacity()))
                .status(PetTransportStatus.valueOf(petTransportPersistence.getStatus()))
                .registeredAt(petTransportPersistence.getRegisteredAt())
                .build();
    }

    private Driver toDriver(DriverEmbeddable driverEmbeddable) {
        return Driver.builder()
                .fullName(FullName.builder()
                        .firstName(driverEmbeddable.getFirstName())
                        .lastName(driverEmbeddable.getLastName())
                        .build())
                .document(DocumentFactory.cnh(driverEmbeddable.getDocument()))
                .build();
    }

    private Vehicle toVehicle(VehicleEmbeddable vehicleEmbeddable) {
        return Vehicle.builder()
                .licensePlate(new LicensePlate(vehicleEmbeddable.getLicensePlate()))
                .brand(vehicleEmbeddable.getBrand())
                .model(vehicleEmbeddable.getModel())
                .build();
    }
}
