package br.com.ipet.ordering.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
import java.time.OffsetTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "working_day")
public class WorkingDayPersistenceEntity {

    @Id
    private UUID id;

    @ManyToOne(optional = false)
    private AgendaPersistenceEntity agenda;

    private DayOfWeek dayOfWeek;
    private OffsetTime startTime;
    private OffsetTime endTime;
    private OffsetTime lockedStartTime;
    private OffsetTime lockedEndTimeTime;

}
