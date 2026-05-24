package br.com.ipet.ordering.domain.model.scheduling;

import br.com.ipet.ordering.domain.model.AbstractEventSourceEntity;
import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.commons.exception.CannotChangeStatusException;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.commons.valueobject.SchedulingId;
import br.com.ipet.ordering.domain.model.commons.valueobject.SchedulingItemId;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.order.OrderDoesNotAssociatedWithCompany;
import br.com.ipet.ordering.domain.model.order.OrderDoesNotBelongsToTheCustomer;
import br.com.ipet.ordering.domain.model.order.PaymentMethod;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonEmpty;
import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;
import static br.com.ipet.ordering.domain.model.scheduling.SchedulingStatus.CANCELED;
import static br.com.ipet.ordering.domain.model.scheduling.SchedulingStatus.COMPLETED;
import static br.com.ipet.ordering.domain.model.scheduling.SchedulingStatus.DRAFT;
import static br.com.ipet.ordering.domain.model.scheduling.SchedulingStatus.IN_PROGRESS;
import static br.com.ipet.ordering.domain.model.scheduling.SchedulingStatus.PAID;
import static br.com.ipet.ordering.domain.model.scheduling.SchedulingStatus.SCHEDULED;
import static br.com.ipet.ordering.domain.model.scheduling.SchedulingStatus.WAITING_CONFIRMATION;
import static br.com.ipet.ordering.domain.model.scheduling.SchedulingStatus.WAITING_RESCHEDULING;

public class Scheduling
        extends AbstractEventSourceEntity
        implements AggregateRoot<SchedulingId> {

    private SchedulingId id;
    private CustomerId customerId;
    private CompanyId companyId;
    private Set<SchedulingItem> items;
    private Quantity totalItems;
    private Money totalAmount;
    private SchedulingStatus status;
    private PaymentMethod paymentMethod;
    private OffsetDateTime checkIn;
    private OffsetDateTime checkOut;
    private OffsetDateTime createdAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime scheduledAt;
    private OffsetDateTime rescheduledAt;
    private OffsetDateTime completedAt;
    private OffsetDateTime cancelAt;

    static Scheduling create(CustomerId customerId, CompanyId companyId, OffsetDateTime checking) {
        return new Scheduling(new SchedulingId(), customerId, companyId,
                new HashSet<>(), Quantity.ZERO, Money.ZERO,
                DRAFT, null,
                checking, null,
                OffsetDateTime.now(), null,
                null, null,
                null, null
        );
    }

    @Builder(builderClassName = "ExistingSchedulingBuilder", builderMethodName = "existing")
    public Scheduling(SchedulingId id, CustomerId customerId, CompanyId companyId,
                      Set<SchedulingItem> items, Quantity totalItems, Money totalAmount,
                      SchedulingStatus status, PaymentMethod paymentMethod,
                      OffsetDateTime checkIn, OffsetDateTime checkOut,
                      OffsetDateTime createdAt, OffsetDateTime paidAt,
                      OffsetDateTime scheduledAt, OffsetDateTime rescheduledAt,
                      OffsetDateTime completedAt, OffsetDateTime cancelAt) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setCompanyId(companyId);
        this.setItems(items);
        this.setTotalItems(totalItems);
        this.setTotalAmount(totalAmount);
        this.setStatus(status);
        this.setPaymentMethod(paymentMethod);
        this.setCheckIn(checkIn);
        this.setCheckout(checkOut);
        this.setCreatedAt(createdAt);
        this.setPaidAt(paidAt);
        this.setScheduledAt(scheduledAt);
        this.setRescheduledAt(rescheduledAt);
        this.setCompletedAt(completedAt);
        this.setCancelAt(cancelAt);
    }

    void addItem(Pet pet, Service service) {
        this.verifyIfChangeable();
        this.verifyIfServiceSupportsPetSize(pet, service);
        var item = SchedulingItem.create(this.id, pet, service);
        this.items.add(item);
        this.recalculateTotals();
    }

    public void removeItem(SchedulingItemId schedulingItemId) {
        this.verifyIfChangeable();
        var schedulingPet = this.findSchedulingItemById(schedulingItemId);
        this.items.remove(schedulingPet);
        this.recalculateTotals();
    }

    public void changeItemPet(Pet newPet, SchedulingItemId id) {
        if (isInProgress() || isCompleted() || isCanceled())
            throw new PetCannotBeChangeInThisStatusException(this.status.name());

        var item = this.findSchedulingItemById(id);
        item.changePet(newPet);
    }

    public void changeItemService(Service newService, SchedulingItemId id) {
        this.verifyIfChangeable();
        var item = this.findSchedulingItemById(id);
        item.changeService(newService);
        recalculateTotals();
    }

    public void markToPaid() {
        this.changeStatus(PAID);
        this.setPaidAt(OffsetDateTime.now());
    }

    public void markToWaitingConfirmation() {
        this.changeStatus(WAITING_CONFIRMATION);
    }

    public void scheduled() {
        this.changeStatus(SCHEDULED);
        this.setScheduledAt(OffsetDateTime.now());
    }

    public void markToWaitingRescheduling() {
        this.changeStatus(WAITING_RESCHEDULING);
        this.setRescheduledAt(OffsetDateTime.now());
    }

    public void markToInProgress() {
        this.changeStatus(IN_PROGRESS);
    }

    public void markToCompleted() {
        this.changeStatus(COMPLETED);
        this.setCompletedAt(OffsetDateTime.now());
    }

    public void cancel() {
        this.changeStatus(CANCELED);
        this.setCancelAt(OffsetDateTime.now());
    }

    public boolean isDraft() {
        return DRAFT.equals(this.status);
    }

    public boolean isPaid() {
        return PAID.equals(this.status);
    }

    public boolean isWaitingConfirmation() {
        return WAITING_CONFIRMATION.equals(this.status);
    }

    public boolean isScheduled() {
        return SCHEDULED.equals(this.status);
    }

    public boolean isWaitingRescheduling() {
        return WAITING_RESCHEDULING.equals(this.status);
    }

    public boolean isInProgress() {
        return IN_PROGRESS.equals(this.status);
    }

    public boolean isCompleted() {
        return COMPLETED.equals(this.status);
    }

    public boolean isCanceled() {
        return CANCELED.equals(this.status);
    }

    private SchedulingItem findSchedulingItemById(SchedulingItemId schedulingItemId) {
        return this.items.stream()
                .filter(schedulingItem -> schedulingItem.id().equals(schedulingItemId))
                .findFirst()
                .orElseThrow(() -> new SchedulingItemNotFoundException(this.id.value().toString(), id.value().toString()));
    }

    private void recalculateTotals() {
        var quantity = new Quantity(this.items.size());
        var total = this.items.stream()
                .map(item -> item.service().price())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.setTotalAmount(new Money(total));
        this.setTotalItems(quantity);
    }

    private void verifyIfChangeable() {
        if (!isDraft())
            throw new SchedulingIsNotDraftToChangeException(this.id.toString());
    }

    private void changeStatus(SchedulingStatus newStatus) {
        if (this.status.canNotChangeTo(newStatus))
            throw new CannotChangeStatusException(this.status.name(), newStatus.name());

        this.setStatus(newStatus);
    }

    private void verifyIfServiceSupportsPetSize(Pet pet, Service service) {
        if (!pet.size().equals(service.petSize()))
            throw new ServiceDoesNotSupportPetSizeException(pet.size().name(), service.petSize().name());
    }

    private void verifyIfCanChangeToPlaced(CustomerId customerId, CompanyId companyId) {
        this.verifyIfSchedulingBelongsToTheCustomer(customerId);
        this.verifySchedulingAssociatedWithCompany(companyId);

        if (this.items.isEmpty())
            throw SchedulingCannotBePlacedException.noItems(this.id.toString());

        if (this.paymentMethod == null)
            throw SchedulingCannotBePlacedException.noPaymentMethod(this.id.toString());

        if (this.deliveryCompany == null)
            throw SchedulingCannotBePlacedException.noDeliveryCompany(this.id.toString());
    }

    private void verifyIfSchedulingBelongsToTheCustomer(CustomerId customerId) {
        if (!this.customerId.equals(customerId))
            throw new OrderDoesNotBelongsToTheCustomer("");
    }

    private void verifySchedulingAssociatedWithCompany(CompanyId companyId) {
        if (!this.companyId.equals(companyId))
            throw new OrderDoesNotAssociatedWithCompany("");
    }


    public SchedulingId id() {
        return id;
    }

    private void setId(SchedulingId id) {
        requiresNonNull("Scheduling id", id);
        this.id = id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    private void setCustomerId(CustomerId customerId) {
        requiresNonNull("custumer id", customerId);
        this.customerId = customerId;
    }

    public CompanyId companyId() {
        return companyId;
    }

    private void setCompanyId(CompanyId companyId) {
        requiresNonNull("company id", companyId);
        this.companyId = companyId;
    }

    public Set<SchedulingItem> items() {
        return Collections.unmodifiableSet(items);
    }

    private void setItems(Set<SchedulingItem> items) {
        requiresNonEmpty("items", items);
        this.items = items;
    }

    public Quantity totalItems() {
        return totalItems;
    }

    private void setTotalItems(Quantity totalItems) {
        requiresNonNull("totalItems", totalItems);
        this.totalItems = totalItems;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    private void setTotalAmount(Money totalAmount) {
        requiresNonNull("totalAmount", totalAmount);
        this.totalAmount = totalAmount;
    }

    public SchedulingStatus status() {
        return status;
    }

    private void setStatus(SchedulingStatus status) {
        requiresNonNull("status", status);
        this.status = status;
    }

    public PaymentMethod paymentMethod() {
        return paymentMethod;
    }

    private void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OffsetDateTime checkIn() {
        return checkIn;
    }

    private void setCheckIn(OffsetDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public OffsetDateTime checkOut() {
        return checkOut;
    }

    private void setCheckout(OffsetDateTime checkOut) {
        this.checkOut = checkOut;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    private void setCreatedAt(OffsetDateTime createdAt) {
        requiresNonNull("createdAt", createdAt);
        this.createdAt = createdAt;
    }

    public OffsetDateTime paidAt() {
        return paidAt;
    }

    private void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public OffsetDateTime scheduledAt() {
        return scheduledAt;
    }

    private void setScheduledAt(OffsetDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public OffsetDateTime rescheduledAt() {
        return rescheduledAt;
    }

    private void setRescheduledAt(OffsetDateTime rescheduledAt) {
        this.rescheduledAt = rescheduledAt;
    }

    public OffsetDateTime completedAt() {
        return completedAt;
    }

    private void setCompletedAt(OffsetDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public OffsetDateTime cancelAt() {
        return cancelAt;
    }

    private void setCancelAt(OffsetDateTime cancelAt) {
        this.cancelAt = cancelAt;
    }








}
