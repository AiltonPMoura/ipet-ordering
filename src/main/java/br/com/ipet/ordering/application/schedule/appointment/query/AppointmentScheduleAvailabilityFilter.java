package br.com.ipet.ordering.application.schedule.appointment.query;

import br.com.ipet.ordering.application.commons.AppointmentPetServiceInput;

import java.util.Set;
import java.util.UUID;

public record AppointmentScheduleAvailabilityFilter(UUID companyId,
                                                    Set<AppointmentPetServiceInput> petServices) {
}
