package br.com.ipet.ordering.domain.model.transport;

import br.com.ipet.ordering.domain.model.AbstractEventSourceEntity;
import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import lombok.Builder;

import java.time.OffsetDateTime;

public class PetTransport
        extends AbstractEventSourceEntity
        implements AggregateRoot<PetTransportId> {

    private PetTransportId id;
    private CompanyId companyId;
    private Driver driver;
    private Vehicle vehicle;
    private PetCapacity capacity;
    private OffsetDateTime registeredAt;

    @Builder(builderClassName = "CreateNewPetTransportBuilder", builderMethodName = "createNew")
    private static PetTransport create(CompanyId companyId, Driver driver, Vehicle vehicle, PetCapacity capacity) {
        return new PetTransport(new PetTransportId(), companyId, driver, vehicle, capacity, OffsetDateTime.now());
    }

    @Builder(builderClassName = "CreateExistingPetTransportBuilder", builderMethodName = "existing")
    public PetTransport(PetTransportId id, CompanyId companyId,
                        Driver driver, Vehicle vehicle, PetCapacity capacity,
                        OffsetDateTime registeredAt) {
        this.setId(id);
        this.setCompanyId(companyId);
        this.setDriver(driver);
        this.setVehicle(vehicle);
        this.setCapacity(capacity);
        this.setRegisteredAt(registeredAt);
    }

    public void changeDriver(Driver driver) {
        this.setDriver(driver);
    }

    public void changeCapacity(PetCapacity capacity) {
        this.setCapacity(capacity);
    }

    @Override
    public PetTransportId id() {
        return id;
    }

    private void setId(PetTransportId id) {
        FieldValidator.requiresNonNull("petTransportId", id);
        this.id = id;
    }

    public CompanyId companyId() {
        return companyId;
    }

    private void setCompanyId(CompanyId companyId) {
        FieldValidator.requiresNonNull("companyId", companyId);
        this.companyId = companyId;
    }

    public Driver driver() {
        return driver;
    }

    private void setDriver(Driver driver) {
        FieldValidator.requiresNonNull("driver", driver);
        this.driver = driver;
    }

    public Vehicle vehicle() {
        return vehicle;
    }

    private void setVehicle(Vehicle vehicle) {
        FieldValidator.requiresNonNull("vehicle", vehicle);
        this.vehicle = vehicle;
    }

    public PetCapacity capacity() {
        return capacity;
    }

    private void setCapacity(PetCapacity capacity) {
        FieldValidator.requiresNonNull("capacity", capacity);
        this.capacity = capacity;
    }

    public OffsetDateTime registeredAt() {
        return registeredAt;
    }

    private void setRegisteredAt(OffsetDateTime registeredAt) {
        FieldValidator.requiresNonNull("registeredAt", registeredAt);
        this.registeredAt = registeredAt;
    }
}
