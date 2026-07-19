package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.AbstractEventSourceEntity;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.booking.appointment.PetAppointment;
import br.com.ipet.ordering.domain.model.commons.valueobject.Billing;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.order.OrderDoesNotAssociatedWithCompany;
import br.com.ipet.ordering.domain.model.order.OrderDoesNotBelongsToTheCustomer;
import br.com.ipet.ordering.domain.model.order.PaymentMethod;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentStatus.CANCELED;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentStatus.COMPLETED;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentStatus.DRAFT;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentStatus.IN_PROGRESS;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentStatus.PAID;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentStatus.REQUESTED;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentStatus.SCHEDULED;

public abstract class Booking extends AbstractEventSourceEntity {

    private BookingId id;
    private CustomerId customerId;
    private CompanyId companyId;
    private Quantity totalItems;
    private Money totalAmount;
    private PaymentMethod paymentMethod;
    private Billing billing;
    private OffsetDateTime createdAt;
    private OffsetDateTime requestedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime scheduledAt;
    private OffsetDateTime completedAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime refundedAt;


    protected Booking(BookingId id, CustomerId customerId, CompanyId companyId,
                      Quantity totalItems, Money totalAmount, PaymentMethod paymentMethod, Billing billing,
                      OffsetDateTime createdAt, OffsetDateTime requestedAt,
                      OffsetDateTime paidAt, OffsetDateTime scheduledAt,
                      OffsetDateTime completedAt, OffsetDateTime canceledAt, OffsetDateTime refundedAt) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setCompanyId(companyId);
        this.setTotalItems(totalItems);
        this.setTotalAmount(totalAmount);
        this.setPaymentMethod(paymentMethod);
        this.setBilling(billing);
        this.setCreatedAt(createdAt);
        this.setRequestedAt(requestedAt);
        this.setPaidAt(paidAt);
        this.setScheduledAt(scheduledAt);
        this.setCompletedAt(completedAt);
        this.setCanceledAt(canceledAt);
        this.setRefundedAt(refundedAt);
    }

    private PetAppointment findSchedulingItemById(SchedulingItemId schedulingItemId) {
        return this.items.stream()
                .filter(appointmentBookingItem -> appointmentBookingItem.id().equals(schedulingItemId))
                .findFirst()
                .orElseThrow(() -> new SchedulingItemNotFoundException(this.id.value().toString(), id.value().toString()));
    }

    private void recalculateTotals() {
        var quantity = new Quantity(this.items.size());
        var total = this.items.stream()
                .map(item -> item.service().price().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.setTotalAmount(new Money(total));
        this.setTotalItems(quantity);
    }

    private void recalculateCheckout() {
        var totalDuration = this.items.stream()
                .map(item -> item.service().duration())
                .reduce(Duration.ZERO, Duration::plus);

        this.setCheckOut(checkIn.plus(totalDuration));
    }

    private void verifyIfChangeable() {
        if (!isDraft())
            throw new SchedulingIsNotDraftToChangeException(this.id.toString());
    }

    /*private void changeStatus(AppointmentBookingStatus newStatus) {
        if (this.status.canNotChangeTo(newStatus))
            throw new CannotChangeStatusException(this.status.name(), newStatus.name());

        this.setStatus(newStatus);
    }*/

    private void verifyIfServiceSupportsPetSize(Pet pet, Service service) {
        if (!pet.size().equals(service.petSize()))
            throw new ServiceDoesNotSupportPetSizeException(pet.size().name(), service.petSize().name());
    }

    private void verifyIfSchedulingBelongsToTheCustomer(CustomerId customerId) {
        if (!this.customerId.equals(customerId))
            throw new OrderDoesNotBelongsToTheCustomer("");
    }

    private void verifySchedulingAssociatedWithCompany(CompanyId companyId) {
        if (!this.companyId.equals(companyId))
            throw new OrderDoesNotAssociatedWithCompany("");
    }


    public BookingId id() {
        return id;
    }

    private void setId(BookingId id) {
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
        FieldValidator.requiresNonNull("checkIn", checkIn);
        this.checkIn = checkIn;
    }

    public OffsetDateTime checkOut() {
        return checkOut;
    }

    private void setCheckOut(OffsetDateTime checkOut) {
        this.checkOut = checkOut;
    }

    public Billing billing() {
        return billing;
    }

    private void setBilling(Billing billing) {
        this.billing = billing;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    private void setCreatedAt(OffsetDateTime createdAt) {
        requiresNonNull("createdAt", createdAt);
        this.createdAt = createdAt;
    }

    public OffsetDateTime requestedAt() {
        return requestedAt;
    }

    private void setRequestedAt(OffsetDateTime requestedAt) {
        this.requestedAt = requestedAt;
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

    /*public OffsetDateTime rescheduledAt() {
        return rescheduledAt;
    }

    private void setRescheduledAt(OffsetDateTime rescheduledAt) {
        this.rescheduledAt = rescheduledAt;
    }*/

    public OffsetDateTime completedAt() {
        return completedAt;
    }

    private void setCompletedAt(OffsetDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public OffsetDateTime canceledAt() {
        return canceledAt;
    }

    private void setCanceledAt(OffsetDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    public OffsetDateTime refundedAt() {
        return refundedAt;
    }

    private void setRefundedAt(OffsetDateTime refundedAt) {
        this.refundedAt = refundedAt;
    }

}
