package br.com.ipet.ordering.domain.model.booking.stay;

import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.domain.model.booking.BookingPet;
import lombok.Builder;

import java.util.Objects;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;

public class PetStay {
    private PetStayId id;
    private BookingId bookingId;
    private BookingPet bookingPet;
    private StayService service;

    static PetStay create(BookingId bookingId, BookingPet bookingPet, StayService service) {
        return new PetStay(new PetStayId(), bookingId, bookingPet, service);
    }

    @Builder(builderClassName = "ExistingPetStayBuilder", builderMethodName = "existing")
    private PetStay(PetStayId id, BookingId bookingId, BookingPet bookingPet, StayService service) {
        this.setId(id);
        this.setBookingId(bookingId);
        this.setBookingPet(bookingPet);
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

    public BookingPet bookingPet() {
        return this.bookingPet;
    }

    private void setBookingPet(BookingPet bookingPet) {
        requiresNonNull("pet", bookingPet);
        this.bookingPet = bookingPet;
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
