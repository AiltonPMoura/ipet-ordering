package br.com.ipet.ordering.application.schedule.appointment.query;

import java.time.OffsetTime;

public record LockedTimeOutput(OffsetTime startTime, OffsetTime endTime) {
}
