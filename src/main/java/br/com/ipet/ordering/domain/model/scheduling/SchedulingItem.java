package br.com.ipet.ordering.domain.model.scheduling;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.Money;
import br.com.ipet.ordering.domain.model.commons.Pet;
import br.com.ipet.ordering.domain.model.commons.Quantity;
import br.com.ipet.ordering.domain.model.commons.SchedulingId;
import br.com.ipet.ordering.domain.model.commons.SchedulingItemId;
import br.com.ipet.ordering.domain.model.commons.Service;
import lombok.Builder;

import java.util.Objects;
import java.util.Set;

public class SchedulingItem {
    private SchedulingItemId id;
    private SchedulingId schedulingId;
    private Service service;
    private Set<Pet> pets;
    private Money totalAmount;

    @Builder(builderClassName = "CreateSchedulingItemBuilder", builderMethodName = "createNew")
    private static SchedulingItem create(SchedulingId schedulingId, Service service, Set<Pet> pets) {
        var schedulingItem = new SchedulingItem(new SchedulingItemId(), schedulingId, service, pets, Money.ZERO);

        schedulingItem.recalculateTotals();

        return schedulingItem;
    }

    @Builder(builderClassName = "ExistingSchedulingItemBuilder", builderMethodName = "existing")
    private SchedulingItem(SchedulingItemId id, SchedulingId schedulingId, Service service, Set<Pet> pets, Money totalAmount) {
        this.setId(id);
        this.setSchedulingId(schedulingId);
        this.setService(service);
        this.setPets(pets);
        this.setTotalAmount(totalAmount);
    }

    void changeService(Service service) {
        this.setService(service);
    }

    void changePets(Set<Pet> pets) {
        this.setPets(pets);
        this.recalculateTotals();
    }

    private void recalculateTotals() {
        this.setTotalAmount(this.service.price().multiply(new Quantity(pets.size())));
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

    public Set<Pet> pets() {
        return this.pets;
    }

    public Money totalAmount() {
        return totalAmount;
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

    private void setPets(Set<Pet> pets) {
        FieldValidator.requiresNonEmpty("pets", pets);
        this.pets = pets;
    }

    private void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
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
