package br.com.ipet.ordering.application.transport.management;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.document.DocumentFactory;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import br.com.ipet.ordering.domain.model.transport.Driver;
import br.com.ipet.ordering.domain.model.transport.PetCapacity;
import br.com.ipet.ordering.domain.model.transport.PetTransportId;
import br.com.ipet.ordering.domain.model.transport.PetTransportNotFoundException;
import br.com.ipet.ordering.domain.model.transport.PetTransportRegistrationService;
import br.com.ipet.ordering.domain.model.transport.PetTransports;
import br.com.ipet.ordering.domain.model.transport.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PetTransportManagementApplicationService {

    private final PetTransportRegistrationService petTransportRegistrationService;
    private final PetTransports petTransports;

    public UUID create(PetTransportInput input) {
        FieldValidator.requiresNonNull("petTransportInput", input);

        var petTransport = petTransportRegistrationService.register(
                new CompanyId(input.companyId()),
                this.toDriver(input.driver()),
                this.toVehicle(input.vehicle()),
                new PetCapacity(input.petCapacity())
        );

        petTransports.add(petTransport);

        return petTransport.id().value();
    }

    public void update(UUID companyId, UUID petTransportId, PetTransportUpdateInput input) {
        FieldValidator.requiresNonNull("companyId", companyId);
        FieldValidator.requiresNonNull("petTransportId", petTransportId);
        FieldValidator.requiresNonNull("driverUpdateInput", input);

        var petTransport = petTransports.ofIdAndCompanyId(new PetTransportId(petTransportId), new CompanyId(companyId))
                .orElseThrow(() -> new PetTransportNotFoundException(petTransportId.toString()));

        petTransport.changeDriver(this.toDriver(input.driver()));
        petTransport.changeCapacity(new PetCapacity(input.petCapacity()));

        petTransports.add(petTransport);
    }

    public void delete(UUID companyId, UUID petTransportId) {
        FieldValidator.requiresNonNull("companyId", companyId);
        FieldValidator.requiresNonNull("petTransportId", petTransportId);

        var petTransport = petTransports.ofIdAndCompanyId(new PetTransportId(petTransportId), new CompanyId(companyId))
                .orElseThrow(() -> new PetTransportNotFoundException(petTransportId.toString()));

        petTransportRegistrationService.validateRemoval(new CompanyId(companyId));
        petTransports.remove(petTransport);
    }

    private Driver toDriver(DriverInput input) {
        FieldValidator.requiresNonNull("driverInput", input);

        return Driver.builder()
                .fullName(FullName.builder()
                        .firstName(input.firstName())
                        .lastName(input.lastName())
                        .build())
                .document(DocumentFactory.cnh(input.document()))
                .build();
    }

    private Vehicle toVehicle(VehicleInput input) {
        FieldValidator.requiresNonNull("vehicleInput", input);

        return Vehicle.builder()
                .licensePlate(input.licensePlate())
                .brand(input.brand())
                .model(input.model())
                .build();
    }
}
