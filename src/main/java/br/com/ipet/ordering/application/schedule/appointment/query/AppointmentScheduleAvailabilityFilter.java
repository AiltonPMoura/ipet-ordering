package br.com.ipet.ordering.application.schedule.appointment.query;

import java.util.List;
import java.util.UUID;

public record AppointmentScheduleAvailabilityFilter(UUID companyId,
                                                    List<UUID> serviceIds) {
}
