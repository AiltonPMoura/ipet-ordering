package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@Entity
@Table(name = "appointment_schedule")
public class AppointmentSchedulePersistenceEntity extends AbstractSchedule {

    @ElementCollection
    @CollectionTable(
            name = "appointment_schedule_locked_date",
            joinColumns = @JoinColumn(name = "appointment_schedule_id")
    )
    @Column(name = "date")
    private Set<LocalDate> lockedDates = new HashSet<>();

    @OneToMany(mappedBy = "appointmentSchedule", cascade = CascadeType.ALL)
    private Set<AppointmentWorkDayPersistenceEntity> workDays = new HashSet<>();

    public void setWorkDays(Set<AppointmentWorkDayPersistenceEntity> workDays) {
        workDays.forEach(workDay -> workDay.setAppointmentSchedule(this));
        this.workDays = workDays;
    }

}
