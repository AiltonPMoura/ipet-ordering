package br.com.ipet.ordering.application.schedule.management.appointment;

import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.util.UUID;


public record WorkDayInput(UUID scheduleId,
                           DayOfWeek dayOfWeek,
                           OffsetTime startTime,
                           OffsetTime endTime) {
}
