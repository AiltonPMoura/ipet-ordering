package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.booking.Booking;
import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.domain.model.commons.exception.CannotChangeStatusException;
import br.com.ipet.ordering.domain.model.commons.valueobject.Billing;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.order.PaymentMethod;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;

import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentStatus.CANCELED;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentStatus.COMPLETED;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentStatus.DRAFT;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentStatus.IN_PROGRESS;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentStatus.PAID;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentStatus.REQUESTED;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentStatus.SCHEDULED;

public class AppointmentBooking
        extends Booking
        implements AggregateRoot<BookingId> {

    private OffsetDateTime scheduledStart;
    private OffsetDateTime scheduledEnd;
    private PetTransport petTransport;
    private AppointmentStatus status;
    private Set<PetAppointment> appointments;
    private OffsetDateTime inProgressAt;
    private OffsetDateTime readyAt;
    private OffsetDateTime returnedAt;

    static AppointmentBooking create(CustomerId customerId, CompanyId companyId) {
        return new AppointmentBooking(new BookingId(), customerId, companyId,
                Quantity.ZERO, Money.ZERO, null, null,
                null, null,
                null, AppointmentStatus.DRAFT, Set.of(),
                OffsetDateTime.now(ZoneOffset.UTC), null, null,
                null, null
        );
    }

    @Builder(builderClassName = "ExistingAppointmentBookingBuilder", builderMethodName = "existing")
    private AppointmentBooking(BookingId id, CustomerId customerId, CompanyId companyId,
                               Quantity totalItems, Money totalAmount, PaymentMethod paymentMethod, Billing billing,
                               OffsetDateTime scheduledStart, OffsetDateTime scheduledEnd,
                               PetTransport petTransport, AppointmentStatus status, Set<PetAppointment> appointments,
                               OffsetDateTime createdAt, OffsetDateTime requestedAt,
                               OffsetDateTime paidAt, OffsetDateTime scheduledAt,
                               OffsetDateTime completedAt, OffsetDateTime cancelAt, OffsetDateTime refundedAt) {
        super(id, customerId, companyId, totalItems, totalAmount, paymentMethod, billing,
                createdAt, requestedAt, paidAt, scheduledAt, completedAt, cancelAt, refundedAt);
        this.setScheduledStart(scheduledStart);
        this.setScheduledEnd(scheduledEnd);
        this.setPetTransport(petTransport);
        this.setStatus(status);
        this.setAppointments(appointments);
    }

    /*void addItem(Pet pet, Service service) {
        this.verifyIfChangeable();
        this.verifyIfServiceSupportsPetSize(pet, service);
        var item = PetAppointment.create(this.id, pet, service);
        this.appointments.add(item);
        this.recalculateTotals();
        this.recalculateCheckout();
    }

    public void removeItem(SchedulingItemId schedulingItemId) {
        this.verifyIfChangeable();
        var schedulingPet = this.findSchedulingItemById(schedulingItemId);
        this.appointments.remove(schedulingPet);
        this.recalculateTotals();
        this.recalculateCheckout();
    }

    public void changeItemPet(Pet newPet, SchedulingItemId id) {
        this.verifyIfChangeable();
        var item = this.findSchedulingItemById(id);
        item.changePet(newPet);
    }*/

    public void request(CustomerId customerId, CompanyId companyId) {
        this.verifyIfCanChangeToPlaced(customerId, companyId);
        this.changeStatus(REQUESTED);
        this.setRequestedAt(OffsetDateTime.now());
    }

    public void markAsPaid() {
        this.changeStatus(PAID);
        this.setPaidAt(OffsetDateTime.now());
    }

    public void markAsScheduled() {
        this.changeStatus(SCHEDULED);
        this.setConfirmedAt(OffsetDateTime.now());
    }

    public void markAsInProgress() {
        this.changeStatus(IN_PROGRESS);
    }

    public void complete() {
        this.changeStatus(COMPLETED);
        this.setCompletedAt(OffsetDateTime.now());
    }

    public void cancel() {
        this.changeStatus(CANCELED);
        this.setCanceledAt(OffsetDateTime.now());
    }

    public boolean isDraft() {
        return DRAFT.equals(this.status);
    }

    public boolean isRequested() {
        return REQUESTED.equals(this.status);
    }

    public boolean isPaid() {
        return PAID.equals(this.status);
    }

    public boolean isScheduled() {
        return SCHEDULED.equals(this.status);
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

    private void changeStatus(AppointmentStatus newStatus) {
        if (this.status.canNotChangeTo(newStatus))
            throw new CannotChangeStatusException(this.status.name(), newStatus.name());

        this.setStatus(status);
    }

    private void verifyIfCanChangeToPlaced(CustomerId customerId, CompanyId companyId) {
        /*this.verifyIfSchedulingBelongsToTheCustomer(customerId);
        this.verifySchedulingAssociatedWithCompany(companyId);

        if (this.items.isEmpty())
            throw SchedulingCannotBePlacedException.noItems(this.id.toString());

        if (this.paymentMethod == null)
            throw SchedulingCannotBePlacedException.noPaymentMethod(this.id.toString());

        if (this.deliveryCompany == null)
            throw SchedulingCannotBePlacedException.noDeliveryCompany(this.id.toString());*/
    }

    public OffsetDateTime scheduledStart() {
        return scheduledStart;
    }

    private void setScheduledStart(OffsetDateTime scheduledStart) {
        this.scheduledStart = scheduledStart;
    }

    public OffsetDateTime scheduledEnd() {
        return scheduledEnd;
    }

    private void setScheduledEnd(OffsetDateTime scheduledEnd) {
        this.scheduledEnd = scheduledEnd;
    }

    public PetTransport petTransport() {
        return petTransport;
    }

    private void setPetTransport(PetTransport petTransport) {
        this.petTransport = petTransport;
    }

    public AppointmentStatus status() {
        return status;
    }

    private void setStatus(AppointmentStatus status) {
        FieldValidator.requiresNonNull("status", status);
        this.status = status;
    }

    public Set<PetAppointment> appointments() {
        return appointments;
    }

    private void setAppointments(Set<PetAppointment> appointments) {
        this.appointments = appointments;
    }

    public OffsetDateTime inProgressAt() {
        return inProgressAt;
    }

    private void setInProgressAt(OffsetDateTime inProgressAt) {
        this.inProgressAt = inProgressAt;
    }

    public OffsetDateTime readyAt() {
        return readyAt;
    }

    private void setReadyAt(OffsetDateTime readyAt) {
        this.readyAt = readyAt;
    }

    public OffsetDateTime returnedAt() {
        return returnedAt;
    }

    private void setReturnedAt(OffsetDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }
}
