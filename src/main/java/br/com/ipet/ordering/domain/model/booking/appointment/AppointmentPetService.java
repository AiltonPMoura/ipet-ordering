package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.domain.model.booking.Pet;
import br.com.ipet.ordering.domain.model.serviceoffering.AppointmentService;
import lombok.Builder;

import java.util.Objects;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;

public class AppointmentPetService {
    private AppointmentPetServiceId id;
    private BookingId appointmentBookingId;
    private Pet pet;
    private AppointmentService service;

    static AppointmentPetService create(BookingId appointmentBookingId, Pet pet, AppointmentService service) {
        return new AppointmentPetService(new AppointmentPetServiceId(), appointmentBookingId, pet, service);
    }

    @Builder(builderClassName = "ExistingPetAppointmentBuilder", builderMethodName = "existing")
    private AppointmentPetService(AppointmentPetServiceId id, BookingId appointmentBookingId, Pet pet, AppointmentService service) {
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

    public AppointmentPetServiceId id() {
        return id;
    }

    private void setId(AppointmentPetServiceId id) {
        requiresNonNull("pet service id", id);
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
        AppointmentPetService that = (AppointmentPetService) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
