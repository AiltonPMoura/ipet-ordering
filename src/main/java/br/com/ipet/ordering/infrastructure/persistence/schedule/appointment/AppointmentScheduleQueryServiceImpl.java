package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import br.com.ipet.ordering.application.booking.appointment.query.AppointmentBookingBlockOutput;
import br.com.ipet.ordering.application.booking.appointment.query.AppointmentBookingQueryService;
import br.com.ipet.ordering.application.commons.AppointmentPetServiceInput;
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
import br.com.ipet.ordering.domain.model.schedule.ScheduleStatus;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentSchedule;
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
        var appointmentSchedulePersistence = repository.findByCompanyId(companyId).orElseThrow(() -> new ScheduleNotFoundException(""));
        return mapper.convert(appointmentSchedulePersistence, AppointmentScheduleDetailOutput.class);
    }

    @Override
    public Page<AppointmentScheduleSummaryOutput> filter(AppointmentScheduleFilter filter, Pageable pageable) {
        return repository.findAll(toSpecification(filter), pageable)
                .map(appointmentSchedulePersistenceEntity ->
                        mapper.convert(appointmentSchedulePersistenceEntity, AppointmentScheduleSummaryOutput.class));
    }


    // Verificar mover para um QueryService específico de disponibilidade de agenda
    @Override
    public ScheduleAvailabilityOutput findScheduleAvailability(UUID customerId, AppointmentScheduleAvailabilityFilter filter) {
        var companyId = filter.companyId();

        // TODO: Usar schedule.getCompany().getTimezone()
        var zoneId = ZoneId.of("America/Sao_Paulo");
        var today = LocalDate.now(zoneId);
        var currentTime = LocalTime.now(zoneId);
        var nowOffset = LocalDateTime.now(zoneId);

        var schedule = repository.findByCompanyIdAndStatus(companyId, ScheduleStatus.ACTIVE.name());
        var existsPetTransports = petTransportRepository.existsActiveByCompanyId(companyId);
        long totalServiceDurationMinutes = this.calculateTotalServiceDurationInMinutes(companyId, filter.petServices());

        if (schedule.isEmpty() || !existsPetTransports || totalServiceDurationMinutes == 0)
            return buildEmptyAvailability(today);

        var appointmentSchedule = schedule.get();
        var addDayToSkip = this.shouldExtendLeadTime(appointmentSchedule, today, currentTime, totalServiceDurationMinutes, zoneId);
        var workingDates = this.generateWorkingDates(appointmentSchedule, today, addDayToSkip ? 1 : 0);

        if (workingDates.isEmpty()) return buildEmptyAvailability(today);

        var blockedBookingsByDate = this.findBlockedBookingsByDate(appointmentSchedule.getId(), workingDates, nowOffset, zoneId);
        var workDayMap = this.mapWorkDaysByDayOfWeek(appointmentSchedule.getWorkDays());
        var availableDays = this.generateAvailableDaysWithTimeSlots(workingDates, workDayMap, blockedBookingsByDate, totalServiceDurationMinutes, zoneId);
        var daysWithAvailableTimes = this.removeDaysWithoutTimeSlots(availableDays);

        return buildScheduleAvailability(daysWithAvailableTimes, today);
    }

    private long calculateTotalServiceDurationInMinutes(UUID companyId, Set<AppointmentPetServiceInput> petServices) {
        var serviceIds = petServices.stream()
                .map(petService -> new ServiceId(petService.serviceId()))
                .collect(Collectors.toSet());

        var services = serviceOfferingCatalogService.ofIdsAndCompanyId(serviceIds, new CompanyId(companyId));

        var serviceDurationsById = services.stream().collect(Collectors.toMap(AppointmentService::id, AppointmentService::duration));

        if (!serviceDurationsById.keySet().containsAll(serviceIds)) return 0;

        return petServices.stream()
                .mapToInt(petService -> serviceDurationsById.get(new ServiceId(petService.serviceId())))
                .sum();
    }

    private boolean shouldExtendLeadTime(AppointmentSchedulePersistenceEntity schedule,
                                         LocalDate today, LocalTime currentTime,
                                         Long totalServiceDurationMinutes,
                                         ZoneId zoneId) {
        return this.findAvailableWorkDayToday(schedule, today, currentTime, totalServiceDurationMinutes)
                .map(workDayToday -> this.isTodayFullyBooked(schedule.getId(), workDayToday, today, zoneId, totalServiceDurationMinutes))
                .orElse(true);

    }

    private List<LocalDate> generateWorkingDates(AppointmentSchedulePersistenceEntity schedule, LocalDate today, long daysToSkip) {
        var workingDays = schedule.getWorkDays().stream()
                .map(AppointmentWorkDayPersistenceEntity::getDayOfWeek)
                .collect(Collectors.toSet());

        var endOfWindow = today.plusDays(15);

        return Stream.iterate(today.plusDays(1), date -> !date.isAfter(endOfWindow), date -> date.plusDays(1))
                .filter(date -> workingDays.contains(date.getDayOfWeek()))
                .filter(date -> !this.isDateLocked(schedule, date))
                .skip(daysToSkip)
                .toList();
    }

    private Map<LocalDate, List<AppointmentBookingBlockOutput>> findBlockedBookingsByDate(UUID scheduleId,
                                                                                          List<LocalDate> workingDates,
                                                                                          LocalDateTime nowOffset, ZoneId zoneId) {
        var windowStart = workingDates.getFirst().atStartOfDay(zoneId).toLocalDateTime();
        var windowEnd = workingDates.getLast().atTime(LocalTime.MAX).atZone(zoneId).toLocalDateTime();
        var gracePeriod = nowOffset.minusMinutes(20);

        return bookingQueryService.findBlockedBookings(scheduleId, windowStart, windowEnd, gracePeriod)
                .stream()
                .collect(Collectors.groupingBy(block -> block.scheduledStart().atZone(zoneId).toLocalDate()));
    }

    private Map<DayOfWeek, AppointmentWorkDayPersistenceEntity> mapWorkDaysByDayOfWeek(Set<AppointmentWorkDayPersistenceEntity> workDays) {
        return workDays.stream().collect(Collectors.toMap(AppointmentWorkDayPersistenceEntity::getDayOfWeek, workDay -> workDay));
    }

    private List<DailyAvailabilityOutput> generateAvailableDaysWithTimeSlots(List<LocalDate> candidateDates,
                                                                             Map<DayOfWeek, AppointmentWorkDayPersistenceEntity> workDayMap,
                                                                             Map<LocalDate, List<AppointmentBookingBlockOutput>> bookingsByDate,
                                                                             Long totalServiceDurationMinutes,
                                                                             ZoneId zoneId) {
        return candidateDates.stream().map(availableDate -> {
            var workDay = workDayMap.get(availableDate.getDayOfWeek());
            var dayBookings = bookingsByDate.getOrDefault(availableDate, List.of());
            var times = this.generateTimeSlots(workDay, dayBookings, totalServiceDurationMinutes, zoneId);
            return new DailyAvailabilityOutput(availableDate, times);
        }).toList();
    }

    private List<DailyAvailabilityOutput> removeDaysWithoutTimeSlots(List<DailyAvailabilityOutput> days) {
        return days.stream()
                .filter(day -> !day.times().isEmpty())
                .toList();
    }

    private Optional<AppointmentWorkDayPersistenceEntity> findAvailableWorkDayToday(AppointmentSchedulePersistenceEntity schedule,
                                                                                    LocalDate today, LocalTime currentTime,
                                                                                    Long totalServiceDurationMinutes) {
        if (this.isDateLocked(schedule, today)) return Optional.empty();

        return schedule.getWorkDays().stream()
                .filter(workDay -> workDay.getDayOfWeek().equals(today.getDayOfWeek()))
                .filter(workDayToday -> !currentTime.plusMinutes(totalServiceDurationMinutes).isAfter(workDayToday.getEndTime()))
                .findFirst();
    }

    private boolean isDateLocked(AppointmentSchedulePersistenceEntity schedule, LocalDate today) {
        return schedule.getLockedDates().contains(today);
    }

    private boolean isTodayFullyBooked(UUID scheduleId, AppointmentWorkDayPersistenceEntity workDayToday,
                                       LocalDate today, ZoneId zoneId,
                                       Long totalServiceDurationMinutes) {
        var windowStartToday = today.atTime(workDayToday.getStartTime());
        var windowEndToday = today.atTime(workDayToday.getEndTime());

        var confirmedBookingsToday = bookingQueryService.findConfirmedBookings(scheduleId, windowStartToday, windowEndToday);

        var availableSlots = this.generateTimeSlots(workDayToday, confirmedBookingsToday, totalServiceDurationMinutes, zoneId);

        return availableSlots.isEmpty();
    }

    private List<LocalTime> generateTimeSlots(AppointmentWorkDayPersistenceEntity workDay,
                                              List<AppointmentBookingBlockOutput> existingBookings,
                                              Long totalServiceDurationMinutes,
                                              ZoneId zoneId) {
        return Stream.iterate(workDay.getStartTime(),
                        time -> !time.plusMinutes(totalServiceDurationMinutes).isAfter(workDay.getEndTime()),
                        time -> time.plusMinutes(AppointmentSchedule.SLOT_INCREMENT_MINUTES)
                )
                .filter(time -> !this.isLockedTime(time, totalServiceDurationMinutes, workDay.getLockedTimeSlots()))
                .filter(time -> !this.isAlreadyBooked(time, totalServiceDurationMinutes, existingBookings, zoneId))
                .toList();
    }

    private boolean isLockedTime(LocalTime slotStart, Long totalServiceDurationMinutes, Set<LockedTimeSlotEmbeddable> lockedTimes) {
        var slotEnd = slotStart.plusMinutes(totalServiceDurationMinutes);

        return lockedTimes.stream().anyMatch(lockedTime ->
                slotStart.isBefore(lockedTime.getEndTime()) && slotEnd.isAfter(lockedTime.getStartTime()));
    }

    private boolean isAlreadyBooked(LocalTime slotStart, Long totalServiceDurationMinutes,
                                    List<AppointmentBookingBlockOutput> existingBookings,
                                    ZoneId zoneId) {
        var slotEnd = slotStart.plusMinutes(totalServiceDurationMinutes);

        return existingBookings.stream().anyMatch(booking -> {
            var bookingStart = booking.scheduledStart().atZone(zoneId).toLocalTime();
            var bookingEnd = booking.scheduledEnd().atZone(zoneId).toLocalTime();

            return slotStart.isBefore(bookingEnd) && slotEnd.isAfter(bookingStart);
        });
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
