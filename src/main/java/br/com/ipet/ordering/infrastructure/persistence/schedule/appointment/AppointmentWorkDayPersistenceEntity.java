package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = "id")
@Entity
@Table(name = "appointment_workday")
public class AppointmentWorkDayPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @JoinColumn
    @ManyToOne(optional = false)
    private AppointmentSchedulePersistenceEntity appointmentSchedule;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    @ElementCollection
    @CollectionTable(
            name = "appointment_workday_locked_time",
            joinColumns = @JoinColumn(name = "appointment_workday_id")
    )
    private Set<LockedTimeEmbeddable> lockedTimes = new HashSet<>();

    public UUID getAppointmentScheduleId() {
        return this.appointmentSchedule.getId();
    }

}
