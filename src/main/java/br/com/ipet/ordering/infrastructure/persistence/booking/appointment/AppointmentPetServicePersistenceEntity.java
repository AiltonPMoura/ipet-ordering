package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = "id")
@Table(name = "appointment_pet_service")
@Entity
public class AppointmentPetServicePersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @JoinColumn
    @ManyToOne(optional = false)
    private AppointmentBookingPersistenceEntity appointmentBooking;

    @Embedded
    private PetEmbeddable pet;

    @Embedded
    private ServiceEmbeddable service;

    public UUID getAppointmentBookingId() {
        return this.appointmentBooking.getId();
    }

}
