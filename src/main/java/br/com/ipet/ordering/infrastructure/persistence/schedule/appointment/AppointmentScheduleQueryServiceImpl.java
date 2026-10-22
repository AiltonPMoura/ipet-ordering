package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import br.com.ipet.ordering.application.booking.appointment.query.AppointmentBookingBlockOutput;
import br.com.ipet.ordering.application.booking.appointment.query.AppointmentBookingQueryService;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleDetailOutput;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleFilter;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleQueryService;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleSummaryOutput;
import br.com.ipet.ordering.application.util.Mapper;
import br.com.ipet.ordering.domain.model.schedule.DailyAvailabilityOutput;
import br.com.ipet.ordering.domain.model.schedule.ScheduNotFoundException;
import br.com.ipet.ordering.domain.model.schedule.ScheduleAvailabilityOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AppointmentScheduleQueryServiceImpl implements AppointmentScheduleQueryService {

    private final AppointmentSchedulePersistenceRepository repository;
    private final AppointmentBookingQueryService bookingQueryService;
    private final Mapper mapper;

    @Override
    public AppointmentScheduleDetailOutput findById(UUID scheduleId) {
        var appointmentSchedulePersistence = repository.findById(scheduleId).orElseThrow(() -> new ScheduNotFoundException(""));
        return mapper.convert(appointmentSchedulePersistence, AppointmentScheduleDetailOutput.class);
    }

    @Override
    public AppointmentScheduleDetailOutput findByCompanyId(UUID companyId) {
        var appointmentSchedulePersistence = repository.findByCompany_Id(companyId).orElseThrow(() -> new ScheduNotFoundException(""));
        return mapper.convert(appointmentSchedulePersistence, AppointmentScheduleDetailOutput.class);
    }

    @Override
    public Page<AppointmentScheduleSummaryOutput> filter(AppointmentScheduleFilter filter, Pageable pageable) {
        return repository.findAll(toSpecification(filter), pageable)
                .map(appointmentSchedulePersistenceEntity ->
                        mapper.convert(appointmentSchedulePersistenceEntity, AppointmentScheduleSummaryOutput.class));
    }


    @Override
    public ScheduleAvailabilityOutput findScheduleAvailability(UUID companyId) {
        var schedule = repository.findByCompany_Id(companyId)
                .orElseThrow(() -> new ScheduNotFoundException("Agenda não encontrada para a empresa."));

        var zoneId = ZoneId.of("America/Sao_Paulo"); // TODO: Usar schedule.getCompany().getTimezone()
        var today = LocalDate.now(zoneId);
        var currentTime = LocalTime.now(zoneId);
        var nowOffset = LocalDateTime.now(zoneId);

        long daysToSkip = this.calculateWorkDaysToSkip(schedule, today, currentTime, zoneId, nowOffset);
        var candidateDates = this.generateCandidateDates(schedule, today, daysToSkip);

        if (candidateDates.isEmpty()) return buildEmptyAvailability(today);

        var blockedBookingsByDate = this.findBlockedBookingsByDate(schedule.getId(), candidateDates, nowOffset, zoneId);
        var workDayMap = this.mapWorkDaysByDayOfWeek(schedule.getWorkDays());
        var availableDays = this.generateAvailableDaysWithTimeSlots(candidateDates, workDayMap, blockedBookingsByDate, zoneId);
        var daysWithAvailableTimes = this.removeDaysWithoutTimeSlots(availableDays);

        return buildScheduleAvailability(daysWithAvailableTimes, today);
    }

    private long calculateWorkDaysToSkip(
            AppointmentSchedulePersistenceEntity schedule,
            LocalDate today,
            LocalTime currentTime,
            ZoneId zoneId,
            LocalDateTime nowOffset) {

        var workDayTodayOptional = this.findAvailableWorkDayToday(schedule, today, currentTime);
        if (workDayTodayOptional.isEmpty()) return 1;

        boolean isFullyBooked = this.checkIfTodayIsFullyBooked(schedule.getId(), workDayTodayOptional.get(), today, zoneId, nowOffset);
        return isFullyBooked ? 1 : 0;
    }

    private Optional<AppointmentWorkDayPersistenceEntity> findAvailableWorkDayToday(
            AppointmentSchedulePersistenceEntity schedule,
            LocalDate today,
            LocalTime currentTime) {

        if (schedule.getBlockedDates().contains(today)) return Optional.empty();

        return schedule.getWorkDays().stream()
                .filter(workDay -> workDay.getDayOfWeek().equals(today.getDayOfWeek()))
                .filter(workDay -> currentTime.isBefore(workDay.getEndTime()))
                .findFirst();
    }

    private boolean checkIfTodayIsFullyBooked(
            UUID scheduleId,
            AppointmentWorkDayPersistenceEntity workDay,
            LocalDate today,
            ZoneId zoneId,
            LocalDateTime nowOffset) {

        LocalDateTime windowStart = today.atTime(workDay.getStartTime());
        LocalDateTime windowEnd = today.atTime(workDay.getEndTime());
        LocalDateTime gracePeriod = nowOffset.minusMinutes(20);

        var todayBookings = bookingQueryService.findBlockedBookings(
                scheduleId, windowStart, windowEnd, gracePeriod
        );

        var availableSlots = this.generateTimeSlotsForDay(workDay, todayBookings, zoneId);

        return availableSlots.isEmpty();
    }

    private List<LocalDate> generateCandidateDates(AppointmentSchedulePersistenceEntity schedule, LocalDate today, long daysToSkip) {
        Set<DayOfWeek> workingDays = schedule.getWorkDays().stream()
                .map(AppointmentWorkDayPersistenceEntity::getDayOfWeek)
                .collect(Collectors.toSet());

        var endOfWindow = today.plusDays(15);

        return Stream.iterate(today.plusDays(1), date -> !date.isAfter(endOfWindow), date -> date.plusDays(1))
                .filter(date -> workingDays.contains(date.getDayOfWeek()))
                .filter(date -> !schedule.getBlockedDates().contains(date))
                .skip(daysToSkip)
                .toList();
    }

    private Map<LocalDate, List<AppointmentBookingBlockOutput>> findBlockedBookingsByDate(
            UUID scheduleId, List<LocalDate> candidateDates, LocalDateTime nowOffset, ZoneId zoneId) {

        LocalDateTime windowStart = candidateDates.getFirst().atStartOfDay(zoneId).toLocalDateTime();
        LocalDateTime windowEnd = candidateDates.getLast().atTime(LocalTime.MAX).atZone(zoneId).toLocalDateTime();
        LocalDateTime gracePeriod = nowOffset.minusMinutes(20);

        List<AppointmentBookingBlockOutput> blockedBookings = bookingQueryService.findBlockedBookings(
                scheduleId, windowStart, windowEnd, gracePeriod
        );

        return blockedBookings.stream()
                .collect(Collectors.groupingBy(block -> block.scheduledStart().atZone(zoneId).toLocalDate()));
    }

    private Map<DayOfWeek, AppointmentWorkDayPersistenceEntity> mapWorkDaysByDayOfWeek(Set<AppointmentWorkDayPersistenceEntity> workDays) {
        return workDays.stream().collect(Collectors.toMap(AppointmentWorkDayPersistenceEntity::getDayOfWeek, workDay -> workDay));
    }

    private List<DailyAvailabilityOutput> generateAvailableDaysWithTimeSlots(
            List<LocalDate> candidateDates,
            Map<DayOfWeek, AppointmentWorkDayPersistenceEntity> workDayMap,
            Map<LocalDate, List<AppointmentBookingBlockOutput>> bookingsByDate,
            ZoneId zoneId) {

        return candidateDates.stream().map(availableDate -> {
            var workDay = workDayMap.get(availableDate.getDayOfWeek());
            var dayBookings = bookingsByDate.getOrDefault(availableDate, List.of());
            var times = this.generateTimeSlotsForDay(workDay, dayBookings, zoneId);
            return new DailyAvailabilityOutput(availableDate, times);
        }).toList();
    }

    private List<LocalTime> generateTimeSlotsForDay(
            AppointmentWorkDayPersistenceEntity workDay,
            List<AppointmentBookingBlockOutput> dayBookings,
            ZoneId zoneId) {

        return Stream.iterate(workDay.getStartTime(), time -> time.plusMinutes(30).isBefore(workDay.getEndTime())
                        || time.plusMinutes(30).equals(workDay.getEndTime()), time -> time.plusMinutes(30))
                .filter(time -> !this.isDuringBreak(time, workDay.getBlockedTimeSlots()))
                .filter(time -> !this.isAlreadyBooked(time, dayBookings, zoneId))
                .toList();
    }

    private boolean isDuringBreak(LocalTime slotStart, Set<BlockedTimeSlotEmbeddable> breaks) {
        LocalTime slotEnd = slotStart.plusMinutes(30);

        return breaks.stream().anyMatch(pause ->
                slotStart.isBefore(pause.getEndTime()) && slotEnd.isAfter(pause.getStartTime())
        );
    }

    private boolean isAlreadyBooked(LocalTime slotStart, List<AppointmentBookingBlockOutput> bookings, ZoneId zoneId) {
        LocalTime slotEnd = slotStart.plusMinutes(30);

        return bookings.stream().anyMatch(booking -> {
            LocalTime bookingStart = booking.scheduledStart().atZone(zoneId).toLocalTime();
            LocalTime bookingEnd = booking.scheduledEnd().atZone(zoneId).toLocalTime();

            return slotStart.isBefore(bookingEnd) && slotEnd.isAfter(bookingStart);
        });
    }

    private List<DailyAvailabilityOutput> removeDaysWithoutTimeSlots(List<DailyAvailabilityOutput> days) {
        return days.stream()
                .filter(day -> !day.times().isEmpty())
                .toList();
    }

    private ScheduleAvailabilityOutput buildEmptyAvailability(LocalDate fallbackDate) {
        return new ScheduleAvailabilityOutput(fallbackDate, fallbackDate, List.of());
    }

    private ScheduleAvailabilityOutput buildScheduleAvailability(List<DailyAvailabilityOutput> days, LocalDate fallbackDate) {
        if (days.isEmpty()) {
            return buildEmptyAvailability(fallbackDate);
        }
        return new ScheduleAvailabilityOutput(days.getFirst().date(), days.getLast().date(), days);
    }

    private Specification<AppointmentSchedulePersistenceEntity> toSpecification(AppointmentScheduleFilter filter) {
        return null;/*firstNameLike(filter.getFirstName())
                .or(lastNameLike(filter.getLastName()))
                .or(emailLike(filter.getEmail()));*/
    }

}
