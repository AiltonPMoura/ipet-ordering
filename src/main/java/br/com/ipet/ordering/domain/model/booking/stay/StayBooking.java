package br.com.ipet.ordering.domain.model.booking.stay;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.booking.AppointmentBookingIsNotDraftToChangeException;
import br.com.ipet.ordering.domain.model.booking.Booking;
import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.domain.model.booking.Pet;
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

import static br.com.ipet.ordering.domain.model.booking.stay.StayBookingStatus.CANCELED;
import static br.com.ipet.ordering.domain.model.booking.stay.StayBookingStatus.CHECKED_IN;
import static br.com.ipet.ordering.domain.model.booking.stay.StayBookingStatus.CHECKED_OUT;
import static br.com.ipet.ordering.domain.model.booking.stay.StayBookingStatus.COMPLETED;
import static br.com.ipet.ordering.domain.model.booking.stay.StayBookingStatus.DRAFT;
import static br.com.ipet.ordering.domain.model.booking.stay.StayBookingStatus.PAID;
import static br.com.ipet.ordering.domain.model.booking.stay.StayBookingStatus.REFUNDED;
import static br.com.ipet.ordering.domain.model.booking.stay.StayBookingStatus.REQUESTED;
import static br.com.ipet.ordering.domain.model.booking.stay.StayBookingStatus.SCHEDULED;

public class StayBooking extends Booking
        implements AggregateRoot<BookingId> {

    private OffsetDateTime scheduledCheckIn;
    private OffsetDateTime scheduledCheckOut;
    private StayBookingStatus status;
    private Set<PetStay> stays;
    private OffsetDateTime checkInAt;
    private OffsetDateTime checkOutAt;

    static StayBooking create(CustomerId customerId, CompanyId companyId) {
        return new StayBooking(new BookingId(), customerId, companyId,
                Quantity.ZERO, Money.ZERO, null, null,
                null, null,
                DRAFT, Set.of(),
                OffsetDateTime.now(ZoneOffset.UTC), null, null,
                null, null, null, null,
                null, null
        );
    }

    @Builder(builderClassName = "ExistingAppointmentBookingBuilder", builderMethodName = "existing")
    private StayBooking(BookingId id, CustomerId customerId, CompanyId companyId,
                         Quantity totalItems, Money totalAmount, PaymentMethod paymentMethod, Billing billing,
                         OffsetDateTime scheduledCheckIn, OffsetDateTime scheduledCheckOut,
                         StayBookingStatus status, Set<PetStay> stays,
                         OffsetDateTime createdAt, OffsetDateTime requestedAt,
                         OffsetDateTime paidAt, OffsetDateTime scheduledAt,
                         OffsetDateTime completedAt, OffsetDateTime cancelAt, OffsetDateTime refundedAt,
                         OffsetDateTime checkInAt, OffsetDateTime checkOutAt) {
        super(id, customerId, companyId, totalItems, totalAmount, paymentMethod, billing,
                createdAt, requestedAt, paidAt, scheduledAt, completedAt, cancelAt, refundedAt);
        this.setScheduledCheckIn(scheduledCheckIn);
        this.setScheduledCheckOut(scheduledCheckOut);
        this.setStatus(status);
        this.setStays(stays);
        this.setCheckInAt(checkInAt);
        this.setCheckOutAt(checkOutAt);
    }

    void addPetStay(Pet pet, StayService service) {
        this.verifyIfChangeable();
        this.verifyIfServiceSupportsPetSize(pet, service);
        var item = PetStay.create(this.id(), pet, service);
        this.stays.add(item);
        this.recalculateTotals();
        this.recalculateSchedule();
    }

    public void removePetStay(PetStayId petStayId) {
        this.verifyIfChangeable();
        var petStay = this.findPetStayById(petStayId);
        this.stays.remove(petStay);
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

    public void checkIn() {
        this.changeStatus(CHECKED_IN);
        this.setCheckInAt(OffsetDateTime.now(ZoneOffset.UTC));
    }

    public void checkOut() {
        this.changeStatus(CHECKED_OUT);
        this.setCheckOutAt(OffsetDateTime.now(ZoneOffset.UTC));
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

/*    public void noShow() {
        this.changeStatus(NO_SHOW);
        this.setNoShow(OffsetDateTime.now(ZoneOffset.UTC));
    }*/

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

    public boolean isInCheckIn() {
        return CHECKED_IN.equals(this.status);
    }

    public boolean isInCheckOut() {
        return CHECKED_OUT.equals(this.status);
    }

    public boolean isCompleted() {
        return COMPLETED.equals(this.status);
    }

    public boolean isCanceled() {
        return CANCELED.equals(this.status);
    }

    public boolean isRefunded() {
        return REFUNDED.equals(this.status);
    }

    private void verifyIfChangeable() {
        if (!isDraft())
            throw new AppointmentBookingIsNotDraftToChangeException(this.id().toString());
    }

    private void verifyIfServiceSupportsPetSize(Pet pet, StayService stayService) {
        if (!pet.size().equals(stayService.petSize()))
            throw new StayBookingDoesNotSupportPetSizeException(pet.size(), stayService.petSize());
    }

    private void recalculateTotals() {
        var totalPetAppointmentTransportAmount = this.stays.stream()
                .map(petStay -> petStay.service().price())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.setTotalAmount(new Money(totalPetAppointmentTransportAmount));
        this.setTotalPets(new Quantity(this.stays.size()));
    }

    private void recalculateSchedule() {
        var totalDuration = this.stays.stream()
                .mapToLong(petStay -> petStay.service().duration())
                .sum();

        this.setScheduledEnd(this.scheduledStart().plusMinutes(totalDuration));
    }

    private PetStay findPetStayById(PetStayId petStayId) {
        return this.stays.stream()
                .filter(petStay -> petStay.id().equals(petStayId))
                .findFirst()
                .orElseThrow(() -> new PetStayNotFoundException(this.id().value().toString(), petStayId.toString()));
    }

    private void changeStatus(StayBookingStatus newStatus) {
        if (this.status.canNotChangeTo(newStatus))
            throw new CannotChangeStatusException(this.status.name(), newStatus.name());

        this.setStatus(newStatus);
    }

    private void verifyIfCanChangeToRequested(CustomerId customerId, CompanyId companyId) {
        //this.verifyIfSchedulingBelongsToTheCustomer(customerId);
        //this.verifySchedulingAssociatedWithCompany(companyId);

        if (this.stays.isEmpty())
            throw StayBookingCannotBeRequestedException.noStays(this.id().toString());

        if (this.paymentMethod() == null)
            throw StayBookingCannotBeRequestedException.noPaymentMethod(this.id().toString());

        if (this.billing() == null)
            throw StayBookingCannotBeRequestedException.noBilling(this.id().toString());

    }

    public OffsetDateTime scheduledCheckIn() {
        return scheduledCheckIn;
    }

    private void setScheduledCheckIn(OffsetDateTime scheduledCheckIn) {
        this.scheduledCheckIn = scheduledCheckIn;
    }

    public OffsetDateTime scheduledCheckOut() {
        return scheduledCheckOut;
    }

    private void setScheduledCheckOut(OffsetDateTime scheduledCheckOut) {
        this.scheduledCheckOut = scheduledCheckOut;
    }

    public StayBookingStatus status() {
        return status;
    }

    private void setStatus(StayBookingStatus status) {
        FieldValidator.requiresNonNull("status", status);
        this.status = status;
    }

    public Set<PetStay> stays() {
        return stays;
    }

    private void setStays(Set<PetStay> stays) {
        this.stays = stays;
    }

    public OffsetDateTime checkInAt() {
        return checkInAt;
    }

    private void setCheckInAt(OffsetDateTime checkInAt) {
        this.checkInAt = checkInAt;
    }

    public OffsetDateTime checkOutAt() {
        return checkOutAt;
    }

    private void setCheckOutAt(OffsetDateTime checkOutAt) {
        this.checkOutAt = checkOutAt;
    }

}
