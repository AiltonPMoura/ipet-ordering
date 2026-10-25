package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.booking.AppointmentBookingDoesNotSupportPetSizeException;
import br.com.ipet.ordering.domain.model.booking.AppointmentBookingIsNotDraftToChangeException;
import br.com.ipet.ordering.domain.model.booking.Booking;
import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.domain.model.booking.PetBookingTransport;
import br.com.ipet.ordering.domain.model.commons.enums.PaymentMethod;
import br.com.ipet.ordering.domain.model.commons.enums.PaymentStatus;
import br.com.ipet.ordering.domain.model.booking.BookingPet;
import br.com.ipet.ordering.domain.model.booking.PetAppointmentNotFoundException;
import br.com.ipet.ordering.domain.model.commons.exception.CannotChangeStatusException;
import br.com.ipet.ordering.domain.model.commons.valueobject.Billing;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import br.com.ipet.ordering.domain.model.commons.id.CustomerId;
import br.com.ipet.ordering.domain.model.commons.id.ScheduleId;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentSchedule;
import br.com.ipet.ordering.domain.model.serviceoffering.AppointmentService;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.CANCELED;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.COMPLETED;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.DRAFT;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.DROPPING_OFF;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.IN_PROGRESS;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.PICKING_UP;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.READY;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.REQUESTED;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.RETURNED;
import static br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus.SCHEDULED;

import static br.com.ipet.ordering.domain.model.commons.enums.PaymentStatus.PAID;
import static br.com.ipet.ordering.domain.model.commons.enums.PaymentStatus.REFUNDED;

public final class AppointmentBooking
        extends Booking
        implements AggregateRoot<BookingId> {

    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;
    private PetBookingTransport petBookingTransport;
    private AppointmentBookingStatus appointmentStatus;
    private Set<AppointmentPetService> appointmentPetServices;
    private OffsetDateTime inProgressAt;
    private OffsetDateTime readyAt;
    private OffsetDateTime returnedAt;

    static AppointmentBooking create(CustomerId customerId, CompanyId companyId,
                                     AppointmentSchedule appointmentSchedule,
                                     LocalDateTime requestedStart) {

        return new AppointmentBooking(new BookingId(), customerId, companyId, appointmentSchedule.id(),
                Quantity.ZERO, Money.ZERO, null, null, null,
                requestedStart, null,
                null, AppointmentBookingStatus.DRAFT, new HashSet<>(), null, null,
                null, null, null, null,
                null, null, null, null
        );
    }

    @Builder(builderClassName = "ExistingAppointmentBookingBuilder", builderMethodName = "existing")
    private AppointmentBooking(BookingId id, CustomerId customerId, CompanyId companyId, ScheduleId scheduleId,
                               Quantity totalPets, Money totalAmount,
                               PaymentMethod paymentMethod, PaymentStatus paymentStatus, Billing billing,
                               LocalDateTime scheduledStart, LocalDateTime scheduledEnd,
                               PetBookingTransport petBookingTransport, AppointmentBookingStatus appointmentStatus, Set<AppointmentPetService> appointmentPetServices,
                               OffsetDateTime requestedAt,
                               OffsetDateTime paidAt, OffsetDateTime scheduledAt,
                               OffsetDateTime completedAt, OffsetDateTime cancelAt, OffsetDateTime refundedAt,
                               OffsetDateTime inProgressAt, OffsetDateTime readyAt, OffsetDateTime returnedAt, String cancelationReason) {
        super(id, customerId, companyId, scheduleId, totalPets, totalAmount, paymentMethod, paymentStatus, billing,
                requestedAt, paidAt, scheduledAt, completedAt, cancelAt, refundedAt, cancelationReason);
        this.setScheduledStart(scheduledStart);
        this.setScheduledEnd(scheduledEnd);
        this.setPetBookingTransport(petBookingTransport);
        this.setAppointmentStatus(appointmentStatus);
        this.setAppointmentPetServices(appointmentPetServices);
        this.setInProgressAt(inProgressAt);
        this.setReadyAt(readyAt);
        this.setReturnedAt(returnedAt);
    }

    void addAppointmentPetService(BookingPet bookingPet, AppointmentService appointmentService) {
        this.verifyIfChangeable();
        this.verifyIfServiceSupportsPetSize(bookingPet, appointmentService);
        var appointmentPetService = AppointmentPetService.create(this.id(), bookingPet, appointmentService);
        this.appointmentPetServices.add(appointmentPetService);
        this.recalculateTotals();
        this.recalculateScheduleEnd();
    }

    public void removeAppointmentPetService(AppointmentPetServiceId appointmentPetServiceId) {
        this.verifyIfChangeable();
        var appointmentPetService = this.findPetAppointmentById(appointmentPetServiceId);
        this.appointmentPetServices.remove(appointmentPetService);
        this.recalculateTotals();
        this.recalculateScheduleEnd();
    }

    public void request(CustomerId customerId, CompanyId companyId) {
        this.verifyIfCanChangeToRequested(customerId, companyId);
        this.changeStatus(REQUESTED);
        this.setRequestedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    public void changePaymentMethod(PaymentMethod paymentMethod) {
        FieldValidator.requiresNonNull("paymentMethod", paymentMethod);
        this.verifyIfChangeable();
        super.setPaymentMethod(paymentMethod);
    }

    public void changeTransport(PetBookingTransport petBookingTransport) {
        FieldValidator.requiresNonNull("transportDetail", petBookingTransport);
        this.verifyIfChangeable();
        this.setPetBookingTransport(petBookingTransport);
        this.recalculateTotals();
    }

    public void markAsPaid() {
        this.changePaymentStatus(PAID);
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
        this.changePaymentStatus(REFUNDED);
        this.setRefundedAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    public boolean isDraft() {
        return DRAFT.equals(this.appointmentStatus);
    }

    public boolean isRequested() {
        return REQUESTED.equals(this.appointmentStatus);
    }

    public boolean isScheduled() {
        return SCHEDULED.equals(this.appointmentStatus);
    }

    public boolean isInProgress() {
        return IN_PROGRESS.equals(this.appointmentStatus);
    }

    public boolean isCompleted() {
        return COMPLETED.equals(this.appointmentStatus);
    }

    public boolean isCanceled() {
        return CANCELED.equals(this.appointmentStatus);
    }

    public boolean isReady() {
        return READY.equals(this.appointmentStatus);
    }

    private void verifyIfChangeable() {
        if (!isDraft())
            throw new AppointmentBookingIsNotDraftToChangeException(this.id().toString());
    }

    private void verifyIfServiceSupportsPetSize(BookingPet bookingPet, AppointmentService appointmentService) {
        if (!bookingPet.size().equals(appointmentService.petSize()))
            throw new AppointmentBookingDoesNotSupportPetSizeException(bookingPet.size().name(), appointmentService.petSize().name());
    }

    private void recalculateTotals() {
        var totalPetAppointmentTransportAmount = this.appointmentPetServices.stream()
                .map(appointmentPetService -> appointmentPetService.service().price().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (this.petBookingTransport != null)
            totalPetAppointmentTransportAmount = totalPetAppointmentTransportAmount.add(this.petBookingTransport.cost().value());

        this.setTotalAmount(new Money(totalPetAppointmentTransportAmount));
        this.setTotalPets(new Quantity(this.appointmentPetServices.size()));
    }

    private void recalculateScheduleEnd() {
        var totalDuration = this.appointmentPetServices.stream()
                .mapToInt(appointmentPetService -> appointmentPetService.service().duration())
                .sum();

        this.setScheduledEnd(this.scheduledStart().plusMinutes(totalDuration));
    }

    private AppointmentPetService findPetAppointmentById(AppointmentPetServiceId appointmentPetServiceId) {
        return this.appointmentPetServices.stream()
                .filter(appointmentPetService -> appointmentPetService.id().equals(appointmentPetServiceId))
                .findFirst()
                .orElseThrow(() -> new PetAppointmentNotFoundException(this.id().value().toString(), appointmentPetServiceId.toString()));
    }

    private void changeStatus(AppointmentBookingStatus newStatus) {
        if (this.appointmentStatus.canNotChangeTo(newStatus))
            throw new CannotChangeStatusException(this.appointmentStatus.name(), newStatus.name());

        this.setAppointmentStatus(newStatus);
    }

    private void verifyIfCanChangeToRequested(CustomerId customerId, CompanyId companyId) {
        //this.verifyIfSchedulingBelongsToTheCustomer(customerId);
        //this.verifySchedulingAssociatedWithCompany(companyId);

        if (this.appointmentPetServices.isEmpty())
            throw AppointmentBookingCannotBeRequestedException.noAppointments(this.id().toString());

        if (this.paymentMethod() == null)
            throw AppointmentBookingCannotBeRequestedException.noPaymentMethod(this.id().toString());

/*        if (this.billing() == null)
            throw AppointmentBookingCannotBeRequestedException.noBilling(this.id().toString());*/

        if (this.petBookingTransport == null)
            throw AppointmentBookingCannotBeRequestedException.noPetTransport(this.id().toString());
    }

    public LocalDateTime scheduledStart() {
        return scheduledStart;
    }

    private void setScheduledStart(LocalDateTime scheduledStart) {
        this.scheduledStart = scheduledStart;
    }

    public LocalDateTime scheduledEnd() {
        return scheduledEnd;
    }

    private void setScheduledEnd(LocalDateTime scheduledEnd) {
        this.scheduledEnd = scheduledEnd;
    }

    public PetBookingTransport petBookingTransport() {
        return petBookingTransport;
    }

    private void setPetBookingTransport(PetBookingTransport petBookingTransport) {
        this.petBookingTransport = petBookingTransport;
    }

    public AppointmentBookingStatus appointmentStatus() {
        return appointmentStatus;
    }

    private void setAppointmentStatus(AppointmentBookingStatus appointmentStatus) {
        FieldValidator.requiresNonNull("appointmentStatus", appointmentStatus);
        this.appointmentStatus = appointmentStatus;
    }

    public Set<AppointmentPetService> appointmentPetServices() {
        return appointmentPetServices;
    }

    private void setAppointmentPetServices(Set<AppointmentPetService> appointmentPetServices) {
        this.appointmentPetServices = appointmentPetServices;
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
