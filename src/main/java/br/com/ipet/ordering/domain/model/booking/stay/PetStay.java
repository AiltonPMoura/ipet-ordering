package br.com.ipet.ordering.domain.model.booking.stay;

import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.domain.model.booking.Pet;
import lombok.Builder;

import java.util.Objects;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;

public class PetStay {
    private PetStayId id;
    private BookingId bookingId;
    private Pet pet;
    private StayService service;

    static PetStay create(BookingId bookingId, Pet pet, StayService service) {
        return new PetStay(new PetStayId(), bookingId, pet, service);
    }

    @Builder(builderClassName = "ExistingPetStayBuilder", builderMethodName = "existing")
    private PetStay(PetStayId id, BookingId bookingId, Pet pet, StayService service) {
        this.setId(id);
        this.setBookingId(bookingId);
        this.setPet(pet);
        this.setService(service);
    }

    /*void changePet(Pet pet) {
        if (!this.pet.size().equals(pet.size()))
            throw new CannotChangePetException(this.pet.size().name(), pet.size().name());

        this.setPet(pet);
    }*/

    public PetStayId id() {
        return id;
    }

    private void setId(PetStayId id) {
        requiresNonNull("pet id", id);
        this.id = id;
    }

    public BookingId bookingId() {
        return bookingId;
    }

    private void setBookingId(BookingId bookingId) {
        requiresNonNull("booking id", bookingId);
        this.bookingId = bookingId;
    }

    public StayService service() {
        return service;
    }

    private void setService(StayService service) {
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
        PetStay that = (PetStay) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
