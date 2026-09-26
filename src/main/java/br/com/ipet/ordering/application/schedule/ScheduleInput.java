package br.com.ipet.ordering.application.schedule;

import java.util.UUID;

public record ScheduleInput(UUID companyId, String name, String subCategory) {
}


