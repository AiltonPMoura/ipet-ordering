package br.com.ipet.ordering.domain.model.scheduling;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.SchedulingId;
import br.com.ipet.ordering.domain.model.commons.valueobject.SchedulingPetId;
import br.com.ipet.ordering.domain.model.commons.valueobject.Service;
import br.com.ipet.ordering.domain.model.pet.PetId;
import lombok.Builder;

import java.util.Objects;

public class SchedulingPet {
    private SchedulingPetId id;
    private SchedulingId schedulingId;
    private PetId petId;
    private Service service;

    @Builder(builderClassName = "CreateSchedulingPetBuilder", builderMethodName = "createNew")
    private static SchedulingPet create(SchedulingId schedulingId, PetId petId, Service service) {
        return new SchedulingPet(new SchedulingPetId(), schedulingId, petId, service);
    }

    @Builder(builderClassName = "ExistingSchedulingPetBuilder", builderMethodName = "existing")
    private SchedulingPet(SchedulingPetId id, SchedulingId schedulingId, PetId petId, Service service) {
        this.setId(id);
        this.setSchedulingId(schedulingId);
        this.setPetId(petId);
        this.setService(service);
    }

    void changePet(PetId petId) {
        this.setPetId(petId);
    }

    public SchedulingPetId id() {
        return id;
    }

    public SchedulingId schedulingId() {
        return schedulingId;
    }

    public PetId petId() {
        return this.petId;
    }

    public Service service() {
        return service;
    }

    private void setId(SchedulingPetId id) {
        FieldValidator.requiresNonNull("schedulingPetId", id);
        this.id = id;
    }

    private void setSchedulingId(SchedulingId schedulingId) {
        FieldValidator.requiresNonNull("schedulingId", schedulingId);
        this.schedulingId = schedulingId;
    }

    private void setPetId(PetId petId) {
        FieldValidator.requiresNonNull("petId", petId);
        this.petId = petId;
    }

    private void setService(Service service) {
        FieldValidator.requiresNonNull("service", service);
        this.service = service;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SchedulingPet that = (SchedulingPet) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
