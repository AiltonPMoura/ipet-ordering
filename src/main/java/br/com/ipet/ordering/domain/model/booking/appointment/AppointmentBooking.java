package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.booking.AppointmentBookingDoesNotSupportPetSizeException;
import br.com.ipet.ordering.domain.model.booking.AppointmentBookingIsNotDraftToChangeException;
import br.com.ipet.ordering.domain.model.booking.Booking;
import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.domain.model.booking.Pet;
import br.com.ipet.ordering.domain.model.booking.PetAppointmentNotFoundException;
import br.com.ipet.ordering.domain.model.commons.exception.CannotChangeStatusException;
import br.com.ipet.ordering.domain.model.commons.valueobject.Billing;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.order.PaymentMethod;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;

import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.CANCELED;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.COMPLETED;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.DRAFT;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.DROPPING_OFF;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.IN_PROGRESS;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.PAID;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.PICKING_UP;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.READY;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.REFUNDED;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.REQUESTED;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.RETURNED;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.SCHEDULED;

public class AppointmentBooking
        extends Booking
        implements AggregateRoot<BookingId> {

    private OffsetDateTime scheduledStart;
    private OffsetDateTime scheduledEnd;
    private PetTransport transport;
    private AppointmentBookingStatus status;
    private Set<PetAppointment> appointments;
    private OffsetDateTime inProgressAt;
    private OffsetDateTime readyAt;
    private OffsetDateTime returnedAt;

    static AppointmentBooking create(CustomerId customerId, CompanyId companyId) {
        return new AppointmentBooking(new BookingId(), customerId, companyId,
                Quantity.ZERO, Money.ZERO, null, null,
                null, null,
                null, AppointmentBookingStatus.DRAFT, Set.of(),
                OffsetDateTime.now(ZoneOffset.UTC), null, null,
                null, null, null, null,
                null, null, null
        );
    }

    @Builder(builderClassName = "ExistingAppointmentBookingBuilder", builderMethodName = "existing")
    private AppointmentBooking(BookingId id, CustomerId customerId, CompanyId companyId,
                               Quantity totalItems, Money totalAmount, PaymentMethod paymentMethod, Billing billing,
                               OffsetDateTime scheduledStart, OffsetDateTime scheduledEnd,
                               PetTransport transport, AppointmentBookingStatus status, Set<PetAppointment> appointments,
                               OffsetDateTime createdAt, OffsetDateTime requestedAt,
                               OffsetDateTime paidAt, OffsetDateTime scheduledAt,
                               OffsetDateTime completedAt, OffsetDateTime cancelAt, OffsetDateTime refundedAt,
                               OffsetDateTime inProgressAt, OffsetDateTime readyAt, OffsetDateTime returnedAt) {
        super(id, customerId, companyId, totalItems, totalAmount, paymentMethod, billing,
                createdAt, requestedAt, paidAt, scheduledAt, completedAt, cancelAt, refundedAt);
        this.setScheduledStart(scheduledStart);
        this.setScheduledEnd(scheduledEnd);
        this.setTransport(transport);
        this.setStatus(status);
        this.setAppointments(appointments);
        this.setInProgressAt(inProgressAt);
        this.setReadyAt(readyAt);
        this.setReturnedAt(returnedAt);
    }

    void addPetAppointment(Pet pet, AppointmentService appointmentService) {
        this.verifyIfChangeable();
        this.verifyIfServiceSupportsPetSize(pet, appointmentService);
        var item = PetAppointment.create(this.id(), pet, appointmentService);
        this.appointments.add(item);
        this.recalculateTotals();
        this.recalculateSchedule();
    }

    public void removePetAppointment(PetAppointmentId petAppointmentId) {
        this.verifyIfChangeable();
        var petAppointment = this.findPetAppointmentById(petAppointmentId);
        this.appointments.remove(petAppointment);
        this.recalculateTotals();
        this.recalculateSchedule();
    }

    public void request(CustomerId customerId, CompanyId companyId) {
        this.verifyIfCanChangeToRequested(customerId, companyId);
        this.changeStatus(REQUESTED);
        this.setRequestedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    public void markAsPaid() {
        this.changeStatus(PAID);
        this.setPaidAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    public void markAsScheduled() {
        this.changeStatus(SCHEDULED);
        this.setScheduledAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    public void pickingUp() {
        // ativar localização.
        this.changeStatus(PICKING_UP);
    }

    public void markAsInProgress() {
        this.changeStatus(IN_PROGRESS);
        this.setInProgressAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    public void markAsReady() {
        this.changeStatus(READY);
        this.setReadyAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    public void droppingOff() {
        // ativar localização.
        this.changeStatus(DROPPING_OFF);
    }

    public void returnPet() {
        this.changeStatus(RETURNED);
        this.setReturnedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    public void complete() {
        this.changeStatus(COMPLETED);
        this.setCompletedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    public void cancel(String reason) {
        this.changeStatus(CANCELED);
        this.setCanceledAt(OffsetDateTime.now());
        this.setCancelationReason(reason);
    }

    public void markAsRefunded() {
        this.changeStatus(REFUNDED);
        this.setRefundedAt(OffsetDateTime.now(ZoneOffset.UTC));
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

    public boolean isReady() {
        return READY.equals(this.status);
    }

    public boolean isRefunded() {
        return REFUNDED.equals(this.status);
    }

    private void verifyIfChangeable() {
        if (!isDraft())
            throw new AppointmentBookingIsNotDraftToChangeException(this.id().toString());
    }

    private void verifyIfServiceSupportsPetSize(Pet pet, AppointmentService appointmentService) {
        if (!pet.size().equals(appointmentService.petSize()))
            throw new AppointmentBookingDoesNotSupportPetSizeException(pet.size(), appointmentService.petSize());
    }

    private void recalculateTotals() {
        var totalPetAppointmentTransportAmount = this.appointments.stream()
                .map(item -> item.service().price())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(this.transport.cost().value());

        this.setTotalAmount(new Money(totalPetAppointmentTransportAmount));
        this.setTotalPets(new Quantity(this.appointments.size()));
    }

    private void recalculateSchedule() {
        var totalDuration = this.appointments.stream()
                .mapToLong(item -> item.service().duration())
                .sum();

        this.setScheduledEnd(this.scheduledStart().plusMinutes(totalDuration));
    }

    private PetAppointment findPetAppointmentById(PetAppointmentId appointmentItemId) {
        return this.appointments.stream()
                .filter(appointmentBookingItem -> appointmentBookingItem.id().equals(appointmentItemId))
                .findFirst()
                .orElseThrow(() -> new PetAppointmentNotFoundException(this.id().value().toString(), appointmentItemId.toString()));
    }

    private void changeStatus(AppointmentBookingStatus newStatus) {
        if (this.status.canNotChangeTo(newStatus))
            throw new CannotChangeStatusException(this.status.name(), newStatus.name());

        this.setStatus(newStatus);
    }

    private void verifyIfCanChangeToRequested(CustomerId customerId, CompanyId companyId) {
        //this.verifyIfSchedulingBelongsToTheCustomer(customerId);
        //this.verifySchedulingAssociatedWithCompany(companyId);

        if (this.appointments.isEmpty())
            throw AppointmentBookingCannotBeRequestedException.noAppointments(this.id().toString());

        if (this.paymentMethod() == null)
            throw AppointmentBookingCannotBeRequestedException.noPaymentMethod(this.id().toString());

        if (this.billing() == null)
            throw AppointmentBookingCannotBeRequestedException.noBilling(this.id().toString());

        if (this.transport == null)
            throw AppointmentBookingCannotBeRequestedException.noPetTransport(this.id().toString());
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

    public PetTransport transport() {
        return transport;
    }

    private void setTransport(PetTransport transport) {
        this.transport = transport;
    }

    public AppointmentBookingStatus status() {
        return status;
    }

    private void setStatus(AppointmentBookingStatus status) {
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
