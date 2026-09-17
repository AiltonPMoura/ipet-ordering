package br.com.ipet.ordering.application.schedule.appointment.management;

import java.time.OffsetTime;

public record LockedTimeInput(OffsetTime startTime, OffsetTime endTime) {
}
