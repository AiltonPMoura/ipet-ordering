package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import lombok.Builder;

import java.util.Objects;

public class SchedulingItem {
    private SchedulingItemId id;
    private SchedulingId schedulingId;
    private Service service;
    private Pet pet;

    @Builder(builderClassName = "CreateSchedulingItemBuilder", builderMethodName = "createNew")
    private static SchedulingItem create(SchedulingId schedulingId, Service service, Pet pet) {
        return new SchedulingItem(new SchedulingItemId(), schedulingId, service, pet);
    }

    @Builder(builderClassName = "ExistingSchedulingItemBuilder", builderMethodName = "existing")
    private SchedulingItem(SchedulingItemId id, SchedulingId schedulingId, Service service, Pet pet) {
        this.setId(id);
        this.setSchedulingId(schedulingId);
        this.setService(service);
        this.setPet(pet);
    }

    public SchedulingItemId id() {
        return id;
    }

    public SchedulingId schedulingId() {
        return schedulingId;
    }

    public Service service() {
        return service;
    }

    public Pet pet() {
        return pet;
    }

    private void setId(SchedulingItemId id) {
        FieldValidator.requiresNonNull("schedulingItemId", id);
        this.id = id;
    }

    private void setSchedulingId(SchedulingId schedulingId) {
        FieldValidator.requiresNonNull("schedulingId", schedulingId);
        this.schedulingId = schedulingId;
    }

    private void setService(Service service) {
        FieldValidator.requiresNonNull("service", service);
        this.service = service;
    }

    private void setPet(Pet pet) {
        FieldValidator.requiresNonNull("pet", pet);
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
