package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.AbstractEventSourceEntity;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.enums.PaymentMethod;
import br.com.ipet.ordering.domain.model.commons.enums.PaymentStatus;
import br.com.ipet.ordering.domain.model.commons.exception.CannotChangeStatusException;
import br.com.ipet.ordering.domain.model.commons.valueobject.Billing;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import br.com.ipet.ordering.domain.model.commons.id.CustomerId;
import br.com.ipet.ordering.domain.model.order.OrderDoesNotAssociatedWithCompany;
import br.com.ipet.ordering.domain.model.order.OrderDoesNotBelongsToTheCustomer;
import br.com.ipet.ordering.domain.model.commons.id.ScheduleId;

import java.time.OffsetDateTime;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;
import static br.com.ipet.ordering.domain.model.commons.enums.PaymentStatus.PAID;
import static br.com.ipet.ordering.domain.model.commons.enums.PaymentStatus.REFUNDED;

public abstract class Booking extends AbstractEventSourceEntity {

    private BookingId id;
    private CustomerId customerId;
    private CompanyId companyId;
    private ScheduleId scheduleId;
    private Quantity totalPets;
    private Money totalAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private Billing billing;
    private OffsetDateTime requestedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime scheduledAt;
    private OffsetDateTime completedAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime refundedAt;
    private String cancelationReason;


    protected Booking(BookingId id, CustomerId customerId, CompanyId companyId, ScheduleId scheduleId,
                      Quantity totalPets, Money totalAmount, PaymentMethod paymentMethod, PaymentStatus paymentStatus, Billing billing,
                      OffsetDateTime requestedAt,
                      OffsetDateTime paidAt, OffsetDateTime scheduledAt,
                      OffsetDateTime completedAt, OffsetDateTime canceledAt, OffsetDateTime refundedAt, String cancelationReason) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setCompanyId(companyId);
        this.setScheduleId(scheduleId);
        this.setTotalPets(totalPets);
        this.setTotalAmount(totalAmount);
        this.setPaymentMethod(paymentMethod);
        this.setPaymentStatus(paymentStatus);
        this.setBilling(billing);
        this.setRequestedAt(requestedAt);
        this.setPaidAt(paidAt);
        this.setScheduledAt(scheduledAt);
        this.setCompletedAt(completedAt);
        this.setCanceledAt(canceledAt);
        this.setRefundedAt(refundedAt);
        this.setCancelationReason(cancelationReason);
    }

    protected void changePaymentStatus(PaymentStatus newStatus) {
        if (this.paymentStatus.canNotChangeTo(newStatus))
            throw new CannotChangeStatusException(this.paymentStatus.name(), newStatus.name());

        this.setPaymentStatus(newStatus);
    }

    protected boolean isPaid() {
        return PAID.equals(this.paymentStatus);
    }

    protected boolean isRefunded() {
        return REFUNDED.equals(this.paymentStatus);
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

    public ScheduleId scheduleId() {
        return scheduleId;
    }

    private void setScheduleId(ScheduleId scheduleId) {
        FieldValidator.requiresNonNull("scheduleId", scheduleId);
        this.scheduleId = scheduleId;
    }

    public Quantity totalPets() {
        return totalPets;
    }

    protected void setTotalPets(Quantity totalPets) {
        requiresNonNull("totalPets", totalPets);
        this.totalPets = totalPets;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    protected void setTotalAmount(Money totalAmount) {
        requiresNonNull("totalAmount", totalAmount);
        this.totalAmount = totalAmount;
    }

    public PaymentMethod paymentMethod() {
        return paymentMethod;
    }

    protected void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus paymentStatus() {
        return this.paymentStatus;
    }

    private void setPaymentStatus(PaymentStatus newStatus) {
        this.paymentStatus = newStatus;
    }

    public Billing billing() {
        return billing;
    }

    private void setBilling(Billing billing) {
        this.billing = billing;
    }

    public OffsetDateTime requestedAt() {
        return requestedAt;
    }

    protected void setRequestedAt(OffsetDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public OffsetDateTime paidAt() {
        return paidAt;
    }

    protected void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public OffsetDateTime scheduledAt() {
        return scheduledAt;
    }

    protected void setScheduledAt(OffsetDateTime scheduledAt) {
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

    protected void setCompletedAt(OffsetDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public OffsetDateTime canceledAt() {
        return canceledAt;
    }

    protected void setCanceledAt(OffsetDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    public OffsetDateTime refundedAt() {
        return refundedAt;
    }

    protected void setRefundedAt(OffsetDateTime refundedAt) {
        this.refundedAt = refundedAt;
    }

    public String cancelationReason() {
        return cancelationReason;
    }

    protected void setCancelationReason(String cancelationReason) {
        this.cancelationReason = cancelationReason;
    }

}
