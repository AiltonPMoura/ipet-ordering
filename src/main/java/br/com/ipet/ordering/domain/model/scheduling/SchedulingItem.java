package br.com.ipet.ordering.domain.model.scheduling;

import br.com.ipet.ordering.domain.model.commons.valueobject.SchedulingId;
import br.com.ipet.ordering.domain.model.commons.valueobject.SchedulingItemId;
import lombok.Builder;

import java.util.Objects;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;

public class SchedulingItem {
    private SchedulingItemId id;
    private SchedulingId schedulingId;
    private Pet pet;
    private Service service;

    static SchedulingItem create(SchedulingId schedulingId, Pet pet, Service service) {
        return new SchedulingItem(new SchedulingItemId(), schedulingId, pet, service);
    }

    @Builder(builderClassName = "ExistingSchedulingItemBuilder", builderMethodName = "existing")
    private SchedulingItem(SchedulingItemId id, SchedulingId schedulingId, Pet pet, Service service) {
        this.setId(id);
        this.setSchedulingId(schedulingId);
        this.setPet(pet);
        this.setService(service);
    }

    void changePet(Pet pet) {
        if (!this.pet.size().equals(pet.size()))
            throw new CannotChangePetException(this.pet.size().name(), pet.size().name());

        this.setPet(pet);
    }

    void changeService(Service service) {
        if (!this.service.petSize().equals(service.petSize()))
            throw new CannotChangeServiceException(this.service.petSize().name(), service.petSize().name());

        this.setService(service);
    }

    public SchedulingItemId id() {
        return id;
    }

    private void setId(SchedulingItemId id) {
        requiresNonNull("scheduling pet id", id);
        this.id = id;
    }

    public SchedulingId schedulingId() {
        return schedulingId;
    }

    private void setSchedulingId(SchedulingId schedulingId) {
        requiresNonNull("scheduling id", schedulingId);
        this.schedulingId = schedulingId;
    }

    public Service service() {
        return service;
    }

    private void setService(Service service) {
        requiresNonNull("service", service);
        this.service = service;
    }

    public Pet pet() {
        return this.pet;
    }

    private void setPet(Pet pet) {
        requiresNonNull("pet", pet);
        this.pet = pet;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SchedulingItem that = (SchedulingItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
