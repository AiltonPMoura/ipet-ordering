package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import br.com.ipet.ordering.application.booking.appointment.query.AppointmentBookingBlockOutput;
import br.com.ipet.ordering.application.booking.appointment.query.AppointmentBookingQueryService;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleAvailabilityFilter;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleDetailOutput;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleFilter;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleQueryService;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleSummaryOutput;
import br.com.ipet.ordering.application.util.Mapper;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.DailyAvailabilityOutput;
import br.com.ipet.ordering.domain.model.schedule.ScheduleAvailabilityOutput;
import br.com.ipet.ordering.domain.model.schedule.ScheduleNotFoundException;
import br.com.ipet.ordering.domain.model.serviceoffering.AppointmentService;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceId;
import br.com.ipet.ordering.domain.model.serviceoffering.ServiceOfferingCatalogService;
import br.com.ipet.ordering.infrastructure.persistence.transport.PetTransportPersistenceRepository;
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
    private final ServiceOfferingCatalogService serviceOfferingCatalogService;
    private final PetTransportPersistenceRepository petTransportRepository;
    private final Mapper mapper;

    @Override
    public AppointmentScheduleDetailOutput findById(UUID scheduleId) {
        var appointmentSchedulePersistence = repository.findById(scheduleId).orElseThrow(() -> new ScheduleNotFoundException(""));
        return mapper.convert(appointmentSchedulePersistence, AppointmentScheduleDetailOutput.class);
    }

    @Override
    public AppointmentScheduleDetailOutput findByCompanyId(UUID companyId) {
        var appointmentSchedulePersistence = repository.findByCompany_Id(companyId).orElseThrow(() -> new ScheduleNotFoundException(""));
        return mapper.convert(appointmentSchedulePersistence, AppointmentScheduleDetailOutput.class);
    }

    @Override
    public Page<AppointmentScheduleSummaryOutput> filter(AppointmentScheduleFilter filter, Pageable pageable) {
        return repository.findAll(toSpecification(filter), pageable)
                .map(appointmentSchedulePersistenceEntity ->
                        mapper.convert(appointmentSchedulePersistenceEntity, AppointmentScheduleSummaryOutput.class));
    }


    @Override
    public ScheduleAvailabilityOutput findScheduleAvailability(AppointmentScheduleAvailabilityFilter filter) {
        var companyId = filter.companyId();

        var zoneId = ZoneId.of("America/Sao_Paulo"); // TODO: Usar schedule.getCompany().getTimezone()
        var today = LocalDate.now(zoneId);
        var currentTime = LocalTime.now(zoneId);
        var nowOffset = LocalDateTime.now(zoneId);

        var schedule = repository.findByCompany_Id(companyId);

        if (schedule.isEmpty() || this.hasNoActivePetTransport(companyId))
            return buildEmptyAvailability(today);

        var appointmentSchedule = schedule.get();

        long totalServiceDurationMinutes = this.calculateTotalServiceDurationInMinutes(companyId, filter.serviceIds());

        long daysToSkip = this.calculateWorkDaysToSkip(appointmentSchedule, today, currentTime, totalServiceDurationMinutes, zoneId, nowOffset);
        var candidateDates = this.generateCandidateDates(appointmentSchedule, today, daysToSkip);

        if (candidateDates.isEmpty()) return buildEmptyAvailability(today);

        var blockedBookingsByDate = this.findBlockedBookingsByDate(appointmentSchedule.getId(), candidateDates, nowOffset, zoneId);
        var workDayMap = this.mapWorkDaysByDayOfWeek(appointmentSchedule.getWorkDays());
        var availableDays = this.generateAvailableDaysWithTimeSlots(candidateDates, workDayMap, blockedBookingsByDate, totalServiceDurationMinutes, zoneId);
        var daysWithAvailableTimes = this.removeDaysWithoutTimeSlots(availableDays);

        return buildScheduleAvailability(daysWithAvailableTimes, today);
    }

    private boolean hasNoActivePetTransport(UUID companyId) {
        return !petTransportRepository.existsActiveByCompanyId(companyId);
    }

    private long calculateTotalServiceDurationInMinutes(UUID companyId, List<UUID> serviceIds) {
        var services = serviceOfferingCatalogService.ofIdsAndCompanyId(
                serviceIds.stream().map(ServiceId::new).collect(Collectors.toSet()),
                new CompanyId(companyId)
        );

        return services.stream().mapToInt(AppointmentService::duration).sum();
    }

    private long calculateWorkDaysToSkip(
            AppointmentSchedulePersistenceEntity schedule,
            LocalDate today,
            LocalTime currentTime,
            Long totalServiceDurationMinutes,
            ZoneId zoneId,
            LocalDateTime nowOffset) {

        var workDayTodayOptional = this.findAvailableWorkDayToday(schedule, today, currentTime, totalServiceDurationMinutes);
        if (workDayTodayOptional.isEmpty()) return 1;

        boolean isFullyBooked = this.checkIfTodayIsFullyBooked(schedule.getId(), workDayTodayOptional.get(), today, zoneId, totalServiceDurationMinutes, nowOffset);
        return isFullyBooked ? 1 : 0;
    }

    private Optional<AppointmentWorkDayPersistenceEntity> findAvailableWorkDayToday(
            AppointmentSchedulePersistenceEntity schedule,
            LocalDate today,
            LocalTime currentTime,
            Long totalServiceDurationMinutes) {

        if (schedule.getBlockedDates().contains(today)) return Optional.empty();

        return schedule.getWorkDays().stream()
                .filter(workDay -> workDay.getDayOfWeek().equals(today.getDayOfWeek()))
                .filter(workDay -> !currentTime.plusMinutes(totalServiceDurationMinutes).isAfter(workDay.getEndTime()))
                .findFirst();
    }

    private boolean checkIfTodayIsFullyBooked(
            UUID scheduleId,
            AppointmentWorkDayPersistenceEntity workDay,
            LocalDate today,
            ZoneId zoneId,
            Long totalServiceDurationMinutes,
            LocalDateTime nowOffset) {

        LocalDateTime windowStart = today.atTime(workDay.getStartTime());
        LocalDateTime windowEnd = today.atTime(workDay.getEndTime());
        LocalDateTime gracePeriod = nowOffset.minusMinutes(20);

        var todayBookings = bookingQueryService.findBlockedBookings(
                scheduleId, windowStart, windowEnd, gracePeriod
        );

        var availableSlots = this.generateTimeSlotsForDay(workDay, todayBookings, totalServiceDurationMinutes, zoneId);

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
            Long totalServiceDurationMinutes,
            ZoneId zoneId) {

        return candidateDates.stream().map(availableDate -> {
            var workDay = workDayMap.get(availableDate.getDayOfWeek());
            var dayBookings = bookingsByDate.getOrDefault(availableDate, List.of());
            var times = this.generateTimeSlotsForDay(workDay, dayBookings, totalServiceDurationMinutes, zoneId);
            return new DailyAvailabilityOutput(availableDate, times);
        }).toList();
    }

    private List<LocalTime> generateTimeSlotsForDay(
            AppointmentWorkDayPersistenceEntity workDay,
            List<AppointmentBookingBlockOutput> dayBookings,
            Long totalServiceDurationMinutes,
            ZoneId zoneId) {

        return Stream.iterate(workDay.getStartTime(),
                        time -> !time.plusMinutes(totalServiceDurationMinutes).isAfter(workDay.getEndTime()),
                        time -> time.plusMinutes(30)
                )
                .filter(time -> !this.isDuringBreak(time, totalServiceDurationMinutes, workDay.getBlockedTimeSlots()))
                .filter(time -> !this.isAlreadyBooked(time, totalServiceDurationMinutes, dayBookings, zoneId))
                .toList();
    }

    private boolean isDuringBreak(LocalTime slotStart, Long totalServiceDurationMinutes, Set<BlockedTimeSlotEmbeddable> breaks) {
        LocalTime slotEnd = slotStart.plusMinutes(totalServiceDurationMinutes);

        return breaks.stream().anyMatch(pause ->
                slotStart.isBefore(pause.getEndTime()) && slotEnd.isAfter(pause.getStartTime())
        );
    }

    private boolean isAlreadyBooked(LocalTime slotStart, Long totalServiceDurationMinutes,
                                    List<AppointmentBookingBlockOutput> bookings, ZoneId zoneId) {
        LocalTime slotEnd = slotStart.plusMinutes(totalServiceDurationMinutes);

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

    private ScheduleAvailabilityOutput buildEmptyAvailability(LocalDate today) {
        return new ScheduleAvailabilityOutput(today, today, List.of());
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
