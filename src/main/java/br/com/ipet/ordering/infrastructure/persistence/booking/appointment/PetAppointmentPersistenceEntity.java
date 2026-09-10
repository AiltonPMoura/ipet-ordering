package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "pet_appointment")
@Entity
public class PetAppointmentPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private UUID bookingId;

    @Embedded
    private PetEmbeddable pet;

    @Embedded
    private ServiceEmbeddable service;

}
