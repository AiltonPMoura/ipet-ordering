package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class LockedTimeEmbeddable {
    private OffsetTime startTime;
    private OffsetTime endTime;
}
