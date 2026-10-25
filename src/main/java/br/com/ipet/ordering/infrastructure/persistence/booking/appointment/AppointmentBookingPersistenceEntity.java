package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import br.com.ipet.ordering.infrastructure.persistence.booking.AbstractBooking;
import br.com.ipet.ordering.infrastructure.persistence.schedule.appointment.AppointmentSchedulePersistenceEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@Table(name = "appointment_booking")
@Entity
public class AppointmentBookingPersistenceEntity extends AbstractBooking {

    @JoinColumn
    @ManyToOne(optional = false)
    private AppointmentSchedulePersistenceEntity schedule;

    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;
    private String status;

    @Embedded
    private PetBookingTransportEmbeddable petBookingTransport;

    private OffsetDateTime inProgressAt;
    private OffsetDateTime readyAt;
    private OffsetDateTime returnedAt;

    @OneToMany(mappedBy = "appointmentBooking", cascade = CascadeType.ALL)
    private Set<AppointmentPetServicePersistenceEntity> appointmentPetServices = new HashSet<>();

    public void setAppointmentPetServices(Set<AppointmentPetServicePersistenceEntity> appointmentPetServices) {
        appointmentPetServices.forEach(appointmentPetService -> appointmentPetService.setAppointmentBooking(this));
        this.appointmentPetServices = appointmentPetServices;
    }

    public UUID getScheduleId() {
        return this.schedule.getId();
    }
}
