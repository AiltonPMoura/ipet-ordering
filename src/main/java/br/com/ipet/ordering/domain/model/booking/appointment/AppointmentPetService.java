package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.domain.model.booking.BookingPet;
import br.com.ipet.ordering.domain.model.serviceoffering.AppointmentService;
import lombok.Builder;

import java.util.Objects;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;

public class AppointmentPetService {
    private AppointmentPetServiceId id;
    private BookingId appointmentBookingId;
    private BookingPet bookingPet;
    private AppointmentService service;

    static AppointmentPetService create(BookingId appointmentBookingId, BookingPet bookingPet, AppointmentService service) {
        return new AppointmentPetService(new AppointmentPetServiceId(), appointmentBookingId, bookingPet, service);
    }

    @Builder(builderClassName = "ExistingPetAppointmentBuilder", builderMethodName = "existing")
    private AppointmentPetService(AppointmentPetServiceId id, BookingId appointmentBookingId, BookingPet bookingPet, AppointmentService service) {
        this.setId(id);
        this.setAppointmentBookingId(appointmentBookingId);
        this.setBookingPet(bookingPet);
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

    public BookingPet bookingPet() {
        return this.bookingPet;
    }

    private void setBookingPet(BookingPet bookingPet) {
        requiresNonNull("bookingPet", bookingPet);
        this.bookingPet = bookingPet;
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
