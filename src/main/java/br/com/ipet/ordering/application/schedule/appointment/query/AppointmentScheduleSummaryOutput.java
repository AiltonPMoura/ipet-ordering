package br.com.ipet.ordering.application.schedule.appointment.query;

import java.util.UUID;

public record AppointmentScheduleSummaryOutput(UUID appointmentScheduleId,
                                               UUID companyId,
                                               String name,
                                               String subCategory) {
}
