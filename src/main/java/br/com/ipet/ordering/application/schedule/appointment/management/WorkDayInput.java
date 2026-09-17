package br.com.ipet.ordering.application.schedule.appointment.management;

import java.time.DayOfWeek;
import java.time.OffsetTime;


public record WorkDayInput(DayOfWeek dayOfWeek,
                           OffsetTime startTime,
                           OffsetTime endTime) {
}
