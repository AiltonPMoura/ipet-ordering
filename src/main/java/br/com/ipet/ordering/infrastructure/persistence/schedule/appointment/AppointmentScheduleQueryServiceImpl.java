package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import br.com.ipet.ordering.application.booking.appointment.AppointmentBookingBlockOutput;
import br.com.ipet.ordering.application.booking.appointment.AppointmentBookingQueryService;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleDetailOutput;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleFilter;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleQueryService;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleSummaryOutput;
import br.com.ipet.ordering.application.util.Mapper;
import br.com.ipet.ordering.domain.model.schedule.AvailableDateTimes;
import br.com.ipet.ordering.domain.model.schedule.AvailableDay;
import br.com.ipet.ordering.domain.model.schedule.ScheduNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AppointmentScheduleQueryServiceImpl implements AppointmentScheduleQueryService {

    private final AppointmentSchedulePersistenceRepository repository;
    private final AppointmentBookingQueryService bookingQueryService;
    private final Mapper mapper;

    @Override
    public AppointmentScheduleDetailOutput findByCompany(UUID companyId) {
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
    public AvailableDateTimes findAvailableDateTimes(UUID companyId) {
        var schedule = repository.findByCompany_Id(companyId)
                .orElseThrow(() -> new ScheduNotFoundException("Agenda não encontrada para a empresa."));

        var zoneId = ZoneId.of("America/Sao_Paulo"); // TODO: Usar schedule.getCompany().getTimezone()
        var today = LocalDate.now(zoneId);
        var currentTime = LocalTime.now(zoneId);
        var nowOffset = OffsetDateTime.now(zoneId);

        long daysToSkip = this.calculateWorkingDaysToSkip(schedule, today, currentTime, zoneId, nowOffset);
        var baseAvailableDates = this.generateBaseAvailableDates(schedule, today, daysToSkip);

        if (baseAvailableDates.isEmpty()) {
            return buildEmptyResponse(today);
        }

        var blockedBookingsByDate = this.fetchAndGroupBlockedBookings(schedule.getId(), baseAvailableDates, nowOffset, zoneId);
        var workDayMap = this.buildWorkDayMap(schedule.getWorkDays());

        var availableDays = this.generateAvailableDaysWithTimeSlots(baseAvailableDates, workDayMap, blockedBookingsByDate, zoneId);
        var daysWithAvailableTimes = this.removeDaysWithoutTimeSlots(availableDays);

        return buildResponse(daysWithAvailableTimes, today);
    }

    private long calculateWorkingDaysToSkip(
            AppointmentSchedulePersistenceEntity schedule,
            LocalDate today,
            LocalTime currentTime,
            ZoneId zoneId,
            OffsetDateTime nowOffset) {

        var workDayOpt = schedule.getWorkDays().stream()
                .filter(workDay -> workDay.getDayOfWeek().equals(today.getDayOfWeek()))
                .findFirst();

        boolean isWorkingDay = workDayOpt.isPresent() && currentTime.isBefore(workDayOpt.get().getEndTime());
        boolean lockedDay = schedule.getLockedDates().contains(today);

        if (!isWorkingDay || lockedDay) {
            return 1;
        }

        boolean isFullyBooked = this.checkIfTodayIsFullyBooked(schedule.getId(), workDayOpt.get(), today, zoneId, nowOffset);

        return isFullyBooked ? 1 : 0;
    }

    private boolean checkIfTodayIsFullyBooked(
            UUID scheduleId,
            AppointmentWorkDayPersistenceEntity workDay,
            LocalDate today,
            ZoneId zoneId,
            OffsetDateTime nowOffset) {

        OffsetDateTime windowStart = today.atStartOfDay(zoneId).toOffsetDateTime();
        OffsetDateTime windowEnd = today.atTime(LocalTime.MAX).atZone(zoneId).toOffsetDateTime();
        OffsetDateTime gracePeriod = nowOffset.minusMinutes(20);

        var todayBookings = bookingQueryService.findBlockedBookings(
                scheduleId, windowStart, windowEnd, gracePeriod
        );

        var availableSlots = this.generateTimeSlotsForDay(workDay, todayBookings, zoneId);

        return availableSlots.isEmpty();
    }

    private List<LocalDate> generateBaseAvailableDates(AppointmentSchedulePersistenceEntity schedule, LocalDate today, long daysToSkip) {
        Set<DayOfWeek> workingDays = schedule.getWorkDays().stream()
                .map(AppointmentWorkDayPersistenceEntity::getDayOfWeek)
                .collect(Collectors.toSet());

        return Stream.iterate(today.plusDays(1), date -> date.plusDays(1))
                .limit(15)
                .filter(date -> workingDays.contains(date.getDayOfWeek()))
                .filter(date -> !schedule.getLockedDates().contains(date))
                .skip(daysToSkip)
                .toList();
    }

    private Map<LocalDate, List<AppointmentBookingBlockOutput>> fetchAndGroupBlockedBookings(
            UUID scheduleId, List<LocalDate> baseAvailableDates, OffsetDateTime nowOffset, ZoneId zoneId) {

        OffsetDateTime windowStart = baseAvailableDates.getFirst().atStartOfDay(zoneId).toOffsetDateTime();
        OffsetDateTime windowEnd = baseAvailableDates.getLast().atTime(LocalTime.MAX).atZone(zoneId).toOffsetDateTime();
        OffsetDateTime gracePeriod = nowOffset.minusMinutes(20);

        List<AppointmentBookingBlockOutput> blockedBookings = bookingQueryService.findBlockedBookings(
                scheduleId, windowStart, windowEnd, gracePeriod
        );

        return blockedBookings.stream()
                .collect(Collectors.groupingBy(block -> block.startDate().atZoneSameInstant(zoneId).toLocalDate()));
    }

    private Map<DayOfWeek, AppointmentWorkDayPersistenceEntity> buildWorkDayMap(Set<AppointmentWorkDayPersistenceEntity> workDays) {
        return workDays.stream()
                .collect(Collectors.toMap(AppointmentWorkDayPersistenceEntity::getDayOfWeek, wd -> wd));
    }

    private List<AvailableDay> generateAvailableDaysWithTimeSlots(
            List<LocalDate> baseAvailableDates,
            Map<DayOfWeek, AppointmentWorkDayPersistenceEntity> workDayMap,
            Map<LocalDate, List<AppointmentBookingBlockOutput>> bookingsByDate,
            ZoneId zoneId) {

        return baseAvailableDates.stream().map(availaleDate -> {
            var workDay = workDayMap.get(availaleDate.getDayOfWeek());
            var dayBookings = bookingsByDate.getOrDefault(availaleDate, List.of());
            var times = this.generateTimeSlotsForDay(workDay, dayBookings, zoneId);

            return new AvailableDay(availaleDate, times);
        }).toList();
    }

    private List<LocalTime> generateTimeSlotsForDay(
            AppointmentWorkDayPersistenceEntity workDay,
            List<AppointmentBookingBlockOutput> dayBookings,
            ZoneId zoneId) {

        return Stream.iterate(workDay.getStartTime(), time -> time.plusMinutes(30).isBefore(workDay.getEndTime())
                        || time.plusMinutes(30).equals(workDay.getEndTime()), time -> time.plusMinutes(30))
                .filter(time -> !this.isDuringBreak(time, workDay.getLockedTimes()))
                .filter(time -> !this.isAlreadyBooked(time, dayBookings, zoneId))
                .toList();
    }

    private boolean isDuringBreak(LocalTime slotStart, Set<LockedTimeEmbeddable> breaks) {
        LocalTime slotEnd = slotStart.plusMinutes(30);

        return breaks.stream().anyMatch(pause ->
                slotStart.isBefore(pause.getEndTime()) && slotEnd.isAfter(pause.getStartTime())
        );
    }

    private boolean isAlreadyBooked(LocalTime slotStart, List<AppointmentBookingBlockOutput> bookings, ZoneId zoneId) {
        LocalTime slotEnd = slotStart.plusMinutes(30);

        return bookings.stream().anyMatch(booking -> {
            LocalTime bookingStart = booking.startDate().atZoneSameInstant(zoneId).toLocalTime();
            LocalTime bookingEnd = booking.endDate().atZoneSameInstant(zoneId).toLocalTime();

            return slotStart.isBefore(bookingEnd) && slotEnd.isAfter(bookingStart);
        });
    }

    private List<AvailableDay> removeDaysWithoutTimeSlots(List<AvailableDay> days) {
        return days.stream()
                .filter(day -> !day.times().isEmpty())
                .toList();
    }

    private AvailableDateTimes buildEmptyResponse(LocalDate fallbackDate) {
        return new AvailableDateTimes(fallbackDate, fallbackDate, List.of());
    }

    private AvailableDateTimes buildResponse(List<AvailableDay> days, LocalDate fallbackDate) {
        if (days.isEmpty()) {
            return buildEmptyResponse(fallbackDate);
        }
        return new AvailableDateTimes(days.getFirst().date(), days.getLast().date(), days);
    }

    private Specification<AppointmentSchedulePersistenceEntity> toSpecification(AppointmentScheduleFilter filter) {
        return null;/*firstNameLike(filter.getFirstName())
                .or(lastNameLike(filter.getLastName()))
                .or(emailLike(filter.getEmail()));*/
    }

}
