package br.com.ipet.ordering.application.schedule.management;

import java.util.UUID;

public record ScheduleInput(UUID scheduleId,
                            UUID companyId,
                            String name,
                            String subCategory) {
}


