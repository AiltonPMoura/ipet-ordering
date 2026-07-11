package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.commons.valueobject.BookingId;
import br.com.ipet.ordering.domain.model.commons.valueobject.SchedulingItemId;
import lombok.Builder;

import java.util.Objects;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;

public class SchedulingItem {
    private SchedulingItemId id;
    private BookingId bookingId;
    private Pet pet;
    private Service service;

    static SchedulingItem create(BookingId bookingId, Pet pet, Service service) {
        return new SchedulingItem(new SchedulingItemId(), bookingId, pet, service);
    }

    @Builder(builderClassName = "ExistingSchedulingItemBuilder", builderMethodName = "existing")
    private SchedulingItem(SchedulingItemId id, BookingId bookingId, Pet pet, Service service) {
        this.setId(id);
        this.setBookingId(bookingId);
        this.setPet(pet);
        this.setService(service);
    }

    void changePet(Pet pet) {
        if (!this.pet.size().equals(pet.size()))
            throw new CannotChangePetException(this.pet.size().name(), pet.size().name());

        this.setPet(pet);
    }

    public SchedulingItemId id() {
        return id;
    }

    private void setId(SchedulingItemId id) {
        requiresNonNull("scheduling pet id", id);
        this.id = id;
    }

    public BookingId schedulingId() {
        return bookingId;
    }

    private void setBookingId(BookingId bookingId) {
        requiresNonNull("scheduling id", bookingId);
        this.bookingId = bookingId;
    }

    public Service service() {
        return service;
    }

    private void setService(Service service) {
        requiresNonNull("service", service);
        this.service = service;
    }

    public Pet pet() {
        return this.pet;
    }

    private void setPet(Pet pet) {
        requiresNonNull("pet", pet);
        this.pet = pet;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SchedulingItem that = (SchedulingItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
