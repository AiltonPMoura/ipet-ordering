package br.com.ipet.ordering.application.schedule.appointment.management;

import java.time.LocalTime;

public record LockedTimeInput(LocalTime startTime, LocalTime endTime) {
}
