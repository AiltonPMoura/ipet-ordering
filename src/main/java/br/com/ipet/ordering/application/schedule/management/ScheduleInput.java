package br.com.ipet.ordering.application.schedule.management;

import java.util.UUID;

public record ScheduleInput(UUID companyId, String name, String subCategory) {
}


