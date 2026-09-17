package br.com.ipet.ordering.application.schedule.appointment.query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record AppointmentScheduleDetailOutput(UUID id,
                                              String name,
                                              String serviceCategory,
                                              String status,
                                              List<LocalDate> lockedDates,
                                              List<WorkDayOutput> workDays) {
}
