package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.AbstractEventSourceEntity;
import br.com.ipet.ordering.domain.model.commons.valueobject.Billing;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.order.OrderDoesNotAssociatedWithCompany;
import br.com.ipet.ordering.domain.model.order.OrderDoesNotBelongsToTheCustomer;

import java.time.OffsetDateTime;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;

public abstract class Booking extends AbstractEventSourceEntity {

    private BookingId id;
    private CustomerId customerId;
    private CompanyId companyId;
    private Quantity totalPets;
    private Money totalAmount;
    private BookingPaymentMethod paymentMethod;
    private Billing billing;
    private OffsetDateTime createdAt;
    private OffsetDateTime requestedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime scheduledAt;
    private OffsetDateTime completedAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime refundedAt;
    private String cancelationReason;


    protected Booking(BookingId id, CustomerId customerId, CompanyId companyId,
                      Quantity totalPets, Money totalAmount, BookingPaymentMethod paymentMethod, Billing billing,
                      OffsetDateTime createdAt, OffsetDateTime requestedAt,
                      OffsetDateTime paidAt, OffsetDateTime scheduledAt,
                      OffsetDateTime completedAt, OffsetDateTime canceledAt, OffsetDateTime refundedAt) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setCompanyId(companyId);
        this.setTotalPets(totalPets);
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

    public BookingPaymentMethod paymentMethod() {
        return paymentMethod;
    }

    protected void setPaymentMethod(BookingPaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
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
