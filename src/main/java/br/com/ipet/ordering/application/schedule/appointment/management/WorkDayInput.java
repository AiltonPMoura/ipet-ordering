package br.com.ipet.ordering.application.schedule.appointment.management;

import java.time.DayOfWeek;
import java.time.LocalTime;


public record WorkDayInput(DayOfWeek dayOfWeek,
                           LocalTime startTime,
                           LocalTime endTime) {
}
