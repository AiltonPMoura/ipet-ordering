package br.com.ipet.ordering.domain.model.scheduling;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.exception.CannotBeChangeStatusException;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.commons.valueobject.SchedulingId;
import br.com.ipet.ordering.domain.model.commons.valueobject.SchedulingPetId;
import br.com.ipet.ordering.domain.model.commons.valueobject.Service;
import br.com.ipet.ordering.domain.model.customer.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.pet.PetId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static br.com.ipet.ordering.domain.model.scheduling.SchedulingStatus.CANCELED;
import static br.com.ipet.ordering.domain.model.scheduling.SchedulingStatus.DRAFT;
import static br.com.ipet.ordering.domain.model.scheduling.SchedulingStatus.PAID;
import static br.com.ipet.ordering.domain.model.scheduling.SchedulingStatus.PLACED;
import static br.com.ipet.ordering.domain.model.scheduling.SchedulingStatus.SCHEDULED;

public class Scheduling implements AggregateRoot<SchedulingId> {
    private SchedulingId id;
    private CustomerId customerId;
    private CompanyId companyId;
    private Set<SchedulingPet> pets;
    private Quantity totalPets;
    private Money totalAmount;
    private SchedulingStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime checking;
    private OffsetDateTime checkout;
    private OffsetDateTime cancelAt;

    @Builder(builderClassName = "CreateSchedulingBuilder", builderMethodName = "createNew")
    private static Scheduling create(CustomerId customerId, CompanyId companyId) {
        return new Scheduling(
                new SchedulingId(), customerId, companyId, new HashSet<>(),
                Quantity.ZERO, Money.ZERO, DRAFT,
                null, null, OffsetDateTime.now(), null
        );
    }

    @Builder(builderClassName = "ExistingSchedulingBuilder", builderMethodName = "existing")
    public Scheduling(SchedulingId id, CustomerId customerId, CompanyId companyId, Set<SchedulingPet> pets,
                      Quantity totalPets, Money totalAmount, SchedulingStatus status,
                      OffsetDateTime checking, OffsetDateTime checkout, OffsetDateTime createdAt, OffsetDateTime cancelAt) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setCompanyId(companyId);
        this.setPets(pets);
        this.setTotalPets(totalPets);
        this.setTotalAmount(totalAmount);
        this.setStatus(status);
        this.setChecking(checking);
        this.setCheckout(checkout);
        this.setCreatedAt(createdAt);
        this.setCancelAt(cancelAt);
    }

    void addPet(PetId petId, Service service) {
        this.verifyIfChangeable();

        var pet = SchedulingPet.createNew()
                .schedulingId(this.id)
                .petId(petId)
                .service(service)
                .build();

        this.pets.add(pet);

        this.recalculateTotals();
    }

    public void removePet(SchedulingPetId schedulingPetId) {
        this.verifyIfChangeable();

        var schedulingPet = this.findSchedulingPetById(schedulingPetId);

        this.pets.remove(schedulingPet);

        this.recalculateTotals();
    }

    public void changeService(Service service) {
        this.verifyIfChangeable();

        this.setService(service);

        recalculateTotals();
    }

    public void changePets(Set<Pet> pets, SchedulingPetId id) {
        this.verifyIfChangeable();

        var item = this.findSchedulingPetById(id);

        item.changePets(pets);
    }

    public void changeSchedulingAt(OffsetDateTime schedulingAt) {
        FieldValidator.requiresNonNull("schedulingAt", schedulingAt);
        FieldValidator.requireDateTimeIsAfterNow("schedulingAt", schedulingAt);
        this.canChangeSchedulingAt();

        this.setChecking(schedulingAt);
    }

    public void markToPaid() {
        this.changeStatus(PAID);
        this.setPaidAt(OffsetDateTime.now());
    }

    public void cancel() {
        this.changeStatus(CANCELED);
        this.setCancelAt(OffsetDateTime.now());
    }

    public boolean isDraft() {
        return SCHEDULED.equals(this.status);
    }

    public boolean isCancel() {
        return CANCELED.equals(this.status);
    }

    public SchedulingId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public CompanyId companyId() {
        return companyId;
    }

    public Quantity totalPets() {
        return totalPets;
    }

    public OffsetDateTime checking() {
        return checking;
    }

    public OffsetDateTime checkout() {
        return checkout;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    public OffsetDateTime paidAt() {
        return paidAt;
    }

    public OffsetDateTime schedulingAt() {
        return checking;
    }

    public OffsetDateTime cancelAt() {
        return cancelAt;
    }

    public Quantity totalItems() {
        return totalPets;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public SchedulingStatus status() {
        return status;
    }

    public Set<SchedulingPet> pets() {
        return Collections.unmodifiableSet(pets);
    }

    private SchedulingPet findSchedulingPetById(SchedulingPetId schedulingPetId) {
        return this.pets.stream()
                .filter(schedulingPet -> schedulingPet.id().equals(schedulingPetId))
                .findFirst()
                .orElseThrow(() -> new SchedulingPetNotFoundException(this.id.value().toString(), id.value().toString()));
    }

    private void recalculateTotals() {
        var quantity = new Quantity(this.pets.size());
        var total = this.pets.stream()
                .map(pet -> pet.service().price().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.setTotalAmount(new Money(total));
        this.setTotalPets(quantity);
    }

    private void canChangeSchedulingAt() {
        if (!DRAFT.equals(status)
                && !PLACED.equals(status)
                && !PAID.equals(status)
                && !SCHEDULED.equals(status)) {
            throw new CanNotChangeSchedulingAtException(status.name());
        }
    }

    private void verifyIfChangeable() {
        if (!isDraft())
            throw new SchedulingIsNotDraftToChangeException(this.id.toString());
    }

    private void changeStatus(SchedulingStatus newStatus) {
        if (this.status.canNotChange(newStatus))
            throw new CannotBeChangeStatusException(this.status.name(), newStatus.name());

        this.setStatus(newStatus);
    }

    private void setId(SchedulingId id) {
        FieldValidator.requiresNonNull("SchedulingId", id);
        this.id = id;
    }

    private void setCustomerId(CustomerId customerId) {
        FieldValidator.requiresNonNull("custumerId", customerId);
        this.customerId = customerId;
    }

    private void setCompanyId(CompanyId companyId) {
        FieldValidator.requiresNonNull("companyId", companyId);
        this.companyId = companyId;
    }

    private void setPets(Set<SchedulingPet> pets) {
        FieldValidator.requiresNonEmpty("pets", pets);
        this.pets = pets;
    }

    private void setTotalPets(Quantity totalPets) {
        FieldValidator.requiresNonNull("totalPets", totalPets);
        this.totalPets = totalPets;
    }

    private void setTotalAmount(Money totalAmount) {
        FieldValidator.requiresNonNull("totalAmount", totalAmount);
        this.totalAmount = totalAmount;
    }

    private void setStatus(SchedulingStatus status) {
        FieldValidator.requiresNonNull("status", status);
        this.status = status;
    }

    private void setCheckout(OffsetDateTime checkout) {
        FieldValidator.requiresNonNull("checkout", checkout);
        this.checkout = checkout;
    }

    private void setCreatedAt(OffsetDateTime createdAt) {
        FieldValidator.requiresNonNull("createdAt", createdAt);
        this.createdAt = createdAt;
    }

    private void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    private void setChecking(OffsetDateTime checking) {
        this.checking = checking;
    }

    private void setCancelAt(OffsetDateTime cancelAt) {
        this.cancelAt = cancelAt;
    }
}