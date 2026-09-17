package br.com.ipet.ordering.application.schedule.appointment.query;

import java.util.List;

public record WorkDayOutput(
        String dayOfWeek,
        String startTime,
        String endTime,
        List<LockedTimeOutput> lockedTimes) {
}
