package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = "id")
@Table(name = "appointment_booking")
@Entity
public class AppointmentBookingPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private UUID scheduleId;
    private UUID companyId;
    private UUID customerId;

    @OneToMany(mappedBy = "appointmentBooking", cascade = CascadeType.ALL)
    private Set<PetAppointmentPersistenceEntity> pets;

}
