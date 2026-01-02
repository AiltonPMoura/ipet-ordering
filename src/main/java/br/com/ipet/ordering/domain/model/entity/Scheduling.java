package br.com.ipet.ordering.domain.model.entity;

import br.com.ipet.ordering.domain.model.exception.CanNotChangeSchedulingAtException;
import br.com.ipet.ordering.domain.model.exception.CannotBeChangeStatusException;
import br.com.ipet.ordering.domain.model.exception.SchedulingIsNotDraftToChangeException;
import br.com.ipet.ordering.domain.model.exception.SchedulingItemNotFoundException;
import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.valueobject.*;
import br.com.ipet.ordering.domain.model.valueobject.Pet;
import br.com.ipet.ordering.domain.model.valueobject.Service;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static br.com.ipet.ordering.domain.model.entity.SchedulingStatus.*;

public class Scheduling {
    private SchedulingId id;
    private CustumerId custumerId;
    private CompanyId companyId;
    private Quantity totalItems;
    private Money totalAmount;
    private SchedulingStatus status;
    private Set<SchedulingItem> items;
    private OffsetDateTime createdAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime schedulingAt;
    private OffsetDateTime cancelAt;

    @Builder(builderClassName = "CreateSchedulingBuilder", builderMethodName = "createNew")
    private static Scheduling create(CustumerId custumerId, CompanyId companyId) {
        return new Scheduling(
                new SchedulingId(), custumerId, companyId, OffsetDateTime.now(), null, null,
                Quantity.ZERO, Money.ZERO, DRAFT, new HashSet<>()
        );
    }

    @Builder(builderClassName = "ExistingSchedulingBuilder", builderMethodName = "existing")
    public Scheduling(SchedulingId id, CustumerId custumerId, CompanyId companyId,
                      OffsetDateTime createdAt, OffsetDateTime schedulingAt, OffsetDateTime cancelAt,
                      Quantity totalItems, Money totalAmount, SchedulingStatus status,
                      Set<SchedulingItem> items) {
        this.setId(id);
        this.setCustumerId(custumerId);
        this.setCompanyId(companyId);
        this.setCreatedAt(createdAt);
        this.setSchedulingAt(schedulingAt);
        this.setCancelAt(cancelAt);
        this.setTotalItems(totalItems);
        this.setTotalAmount(totalAmount);
        this.setStatus(status);
        this.setItems(items);
    }

    public void addItem(Service service, Set<Pet> pets) {
        this.verifyIfChangeable();

        var item = SchedulingItem.createNew()
                .schedulingId(this.id)
                .service(service)
                .pets(pets)
                .build();

        this.items.add(item);

        this.recalculateTotals();
    }

    public void removeItem(SchedulingItemId id) {
        this.verifyIfChangeable();

        var item = this.findISchedulingtemById(id);

        this.items.remove(item);

        this.recalculateTotals();
    }

    public void changeItemService(Service service, SchedulingItemId id) {
        this.verifyIfChangeable();

        var item = this.findISchedulingtemById(id);

        item.changeService(service);
    }

    public void changePets(Set<Pet> pets, SchedulingItemId id) {
        this.verifyIfChangeable();

        var item = this.findISchedulingtemById(id);

        item.changePets(pets);
    }

    public void changeSchedulingAt(OffsetDateTime schedulingAt) {
        FieldValidator.requiresNonNull("schedulingAt", schedulingAt);
        FieldValidator.requireDateTimeIsAfterNow("schedulingAt", schedulingAt);
        this.canChangeSchedulingAt();

        this.setSchedulingAt(schedulingAt);
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

    public CustumerId custumerId() {
        return custumerId;
    }

    public CompanyId companyId() {
        return companyId;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    public OffsetDateTime paidAt() {
        return paidAt;
    }

    public OffsetDateTime schedulingAt() {
        return schedulingAt;
    }

    public OffsetDateTime cancelAt() {
        return cancelAt;
    }

    public Quantity totalItems() {
        return totalItems;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public SchedulingStatus status() {
        return status;
    }

    public Set<SchedulingItem> items() {
        return Collections.unmodifiableSet(items);
    }

    private SchedulingItem findISchedulingtemById(SchedulingItemId id) {
        return this.items.stream()
                .filter(item -> item.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new SchedulingItemNotFoundException(this.id.value().toString(), id.value().toString()));
    }

    private void recalculateTotals() {
        var total = this.items.stream().map(item -> item.totalAmount().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var quantity = this.items.stream().map(item -> item.pets().size())
                .reduce(0, Integer::sum);

        this.setTotalAmount(new Money(total));
        this.setTotalItems(new Quantity(quantity));
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

    private void setCustumerId(CustumerId custumerId) {
        FieldValidator.requiresNonNull("Scheduling custumerId", custumerId);
        this.custumerId = custumerId;
    }

    private void setCompanyId(CompanyId companyId) {
        FieldValidator.requiresNonNull("Scheduling companyId", companyId);
        this.companyId = companyId;
    }

    private void setCreatedAt(OffsetDateTime createdAt) {
        FieldValidator.requiresNonNull("Scheduling, createdAt", createdAt);
        this.createdAt = createdAt;
    }

    private void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    private void setSchedulingAt(OffsetDateTime schedulingAt) {
        this.schedulingAt = schedulingAt;
    }

    private void setCancelAt(OffsetDateTime cancelAt) {
        this.cancelAt = cancelAt;
    }

    private void setTotalItems(Quantity totalItems) {
        FieldValidator.requiresNonNull("Scheduling totalItems", totalItems);
        this.totalItems = totalItems;
    }

    private void setTotalAmount(Money totalAmount) {
        FieldValidator.requiresNonNull("Scheduling totalAmount", totalAmount);
        this.totalAmount = totalAmount;
    }

    private void setStatus(SchedulingStatus status) {
        FieldValidator.requiresNonNull("SchedulingStatus", status);
        this.status = status;
    }

    private void setItems(Set<SchedulingItem> items) {
        FieldValidator.requiresNonNull("SchedulingService", items);
        this.items = items;
    }
}