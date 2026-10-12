package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.domain.model.booking.Pet;
import lombok.Builder;

import java.util.Objects;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;

public class PetAppointment {
    private PetAppointmentId id;
    private BookingId appointmentBookingId;
    private Pet pet;
    private AppointmentService service;

    static PetAppointment create(BookingId appointmentBookingId, Pet pet, AppointmentService service) {
        return new PetAppointment(new PetAppointmentId(), appointmentBookingId, pet, service);
    }

    @Builder(builderClassName = "ExistingPetAppointmentBuilder", builderMethodName = "existing")
    private PetAppointment(PetAppointmentId id, BookingId appointmentBookingId, Pet pet, AppointmentService service) {
        this.setId(id);
        this.setAppointmentBookingId(appointmentBookingId);
        this.setPet(pet);
        this.setService(service);
    }

    /*void changePet(Pet pet) {
        if (!this.pet.size().equals(pet.size()))
            throw new CannotChangePetException(this.pet.size().name(), pet.size().name());

        this.setPet(pet);
    }*/

    public PetAppointmentId id() {
        return id;
    }

    private void setId(PetAppointmentId id) {
        requiresNonNull("pet id", id);
        this.id = id;
    }

    public BookingId bookingId() {
        return appointmentBookingId;
    }

    private void setAppointmentBookingId(BookingId appointmentBookingId) {
        requiresNonNull("booking id", appointmentBookingId);
        this.appointmentBookingId = appointmentBookingId;
    }

    public AppointmentService service() {
        return service;
    }

    private void setService(AppointmentService service) {
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
        PetAppointment that = (PetAppointment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
