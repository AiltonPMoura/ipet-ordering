package br.com.ipet.ordering.domain.model.agenda;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.booking.BookingWindow;
import br.com.ipet.ordering.domain.model.customer.CompanyId;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

public class Agenda implements AggregateRoot<AgendaId> {
    private AgendaId id;
    private CompanyId companyId;
    private AgendaName name;
    private ServiceSubCategory serviceSubCategory;
    private AgendaStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime activedAt;
    private OffsetDateTime standByAt;
    private OffsetDateTime blockedAt;
    private BookingWindow bookingWindow;
    private Set<StandByDates> standByDates;
    private MinuteInterval minuteInterval;

    @Builder(builderClassName = "CreateNewAgendaBuilder", builderMethodName = "createNew")
    private static Agenda create(CompanyId companyId, AgendaName name, ServiceSubCategory subCategory) {
        return new Agenda(new AgendaId(), companyId, name, subCategory, AgendaStatus.DRAFT,
                null, null, null, null,
                new BookingWindow(), new HashSet<>(), new MinuteInterval(15));
    }

    @Builder(builderClassName = "ExistingAgendaBuilder", builderMethodName = "existing")
    public Agenda(AgendaId id, CompanyId companyId, AgendaName name, ServiceSubCategory serviceSubCategory,
                  AgendaStatus status, OffsetDateTime createdAt,
                  OffsetDateTime activedAt, OffsetDateTime standByAt, OffsetDateTime blockedAt,
                  BookingWindow bookingWindow, Set<StandByDates> standByDates, MinuteInterval minuteInterval) {
        this.setId(id);
        this.setCompanyId(companyId);
        this.setName(name);
        this.setServiceSubcategory(serviceSubCategory);
        this.setStatus(status);
        this.setCreatedAt(createdAt);
        this.setActivedAt(activedAt);
        this.setStandByAt(standByAt);
        this.setBlockedAt(blockedAt);
        this.setBookingWindow(bookingWindow);
        this.setStandByDates(standByDates);
        this.setMinuteInterval(minuteInterval);
    }

    void changeName(AgendaName name) {
        this.setName(name);
    }

    void changeServiceSubCategory(ServiceSubCategory serviceSubCategory) {
        this.setServiceSubcategory(serviceSubCategory);
    }

    void changeWorkingHours(WorkingDayId workingDayId, WorkingHours workingHours) {
        var workingDay = findWorkingDay(workingDayId);

        if (serviceSubCategory.isNightShift())
            throw new NitghShiftCannotHaveTime();

        workingDay.changeWorkingHours(workingHours);
    }

    void changeLockedTime(WorkingDayId workingDayId, LockedTime lockedTime) {
        var workingDay = findWorkingDay(workingDayId);

        if (serviceSubCategory.isNightShift())
            throw new NitghShiftCannotHaveTime();

        workingDay.changeLockedTime(lockedTime);
    }

    void changeBookingBy(BookingWindow bookingWindow) {
        this.setBookingWindow(bookingWindow);
    }

    void addWorkingDay(DayOfWeek dayOfWeek, WorkingHours workingHours) {
        FieldValidator.requiresNonNull("dayOfWeek", dayOfWeek);

        verifyExistingWorkingDay(dayOfWeek);

        var workingDay = ScheduledTime.createNew()
                .agendaId(this.id)
                .dayOfWeek(dayOfWeek)
                .workingHours(workingHours)
                .build();

        this.scheduledTimes.add(workingDay);
    }

    public void addLockedTime(WorkingDayId workingDayId, LockedTime lockedTime) {
        var workingDay = findWorkingDay(workingDayId);
        workingDay.addLockedTime(lockedTime);
    }

    public void removeLockedTime(WorkingDayId workingDayId, LockedTime lockedTime) {
        var workingDay = findWorkingDay(workingDayId);
        workingDay.removeLockedTime(lockedTime);
    }

    public void active() {
        FieldValidator.requiresNonEmpty("workingDays", scheduledTimes);
        FieldValidator.requiresNonNull("bookingBy", bookingWindow);

        this.currentStandBy().ifPresent(dates -> this.standByDates.remove(dates));
        this.changeStatus(AgendaStatus.ACTIVED);
        this.setActivedAt(OffsetDateTime.now());
    }

    public void addStandByDates(StandByDates standByDates) {
        FieldValidator.requiresNonNull("standByDates", standByDates);
        this.verifyIsAlreadyStandBy(standByDates);

        this.standByDates.add(standByDates);
    }

    public void changeToStandBy() {
        this.changeStatus(AgendaStatus.STAND_BY);
        this.setStandByAt(OffsetDateTime.now());
    }

    private void changeToblock() {
        changeStatus(AgendaStatus.BLOCKED);
    }

    public ScheduledTime findWorkingDay(WorkingDayId workingDayId) {
        return this.scheduledTimes.stream().filter(scheduledTime ->
                scheduledTime.id().equals(workingDayId))
                .findFirst()
                .orElseThrow(() -> new WorkinDayNotFoundException());
    }

    public void verifyIsAlreadyStandBy(StandByDates standByDates) {
        this.standByDates.stream()
                .filter(dates ->
                        dates.isBetween(standByDates.startDate()) || dates.isBetween(standByDates.endDate()))
                .findAny()
                .ifPresent(dates -> {
                    throw new RuntimeException("dates: " + dates.startDate() + " e " + dates.endDate());
                });
    }

    public void verifyExistingWorkingDay(DayOfWeek dayOfWeek) {
        var workingDayExisting = this.scheduledTimes.stream().filter(scheduledTime ->
                        scheduledTime.dayOfWeek().equals(dayOfWeek))
                .findFirst();

        if (workingDayExisting.isPresent())
            throw new RuntimeException();
    }

    public List<AvailableDateTimes> avaliableDatesTimes() {
        var date = LocalDate.now().plusDays(1);

        return IntStream.range(0, this.bookingWindow.value())
                .mapToObj(date::plusDays)
                .filter(this::isNotStandBy)
                .map(this::filterWorkingDay)
                .filter(Optional::isPresent)
                .map(workingDay ->
                        new AvailableDateTimes(date, workingDay.get().availableTimes(this.minuteInterval)))
                .toList();
    }

    public boolean isDraft() {
        return AgendaStatus.DRAFT.equals(this.status);
    }

    public boolean isActived() {
        return AgendaStatus.ACTIVED.equals(this.status);
    }

    public boolean isStantBy() {
        return AgendaStatus.STAND_BY.equals(this.status);
    }

    public boolean isblocked() {
        return AgendaStatus.BLOCKED.equals(this.status);
    }

    public boolean isNotStandBy(LocalDate date) {
        return this.standByDates.stream().allMatch(dates -> dates.isNotBetween(date));
    }

    private void changeStatus(AgendaStatus status) {
        if (this.status.canNotChange(status))
            throw new RuntimeException();

        this.setStatus(status);
    }

    private void verifyIfChangeAble() {
        if (!isDraft())
            throw new AgendaIsNotDraftToChangeException(this.id.toString());
    }

    private Optional<ScheduledTime> filterWorkingDay(LocalDate date) {
        return this.scheduledTimes.stream()
                .filter(scheduledTime -> scheduledTime.isWorkingDay(date))
                .findFirst();
    }

    @Override
    public AgendaId id() {
        return id;
    }

    public CompanyId companyId() {
        return companyId;
    }

    public AgendaName name() {
        return name;
    }

    public ServiceSubCategory serviceSubCategory() {
        return serviceSubCategory;
    }

    public AgendaStatus status() {
        return status;
    }

    public Set<ScheduledTime> workingDays() {
        return scheduledTimes;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    public OffsetDateTime activedAt() {
        return activedAt;
    }

    public OffsetDateTime standByAt() {
        return standByAt;
    }

    public OffsetDateTime blockedAt() {
        return blockedAt;
    }

    public Set<StandByDates> standByDates() {
        return standByDates;
    }

    public BookingWindow bookingWindow() {
        return bookingWindow;
    }

    public MinuteInterval minuteInterval() {
        return minuteInterval;
    }

    private Optional<StandByDates> currentStandBy() {
        return this.standByDates.stream()
                .filter(StandByDates::isCurrent)
                .findFirst();
    }

    private void setId(AgendaId id) {
        FieldValidator.requiresNonNull("Agenda id", id);
        this.id = id;
    }

    private void setCompanyId(CompanyId companyId) {
        FieldValidator.requiresNonNull("Company id", id);
        this.companyId = companyId;
    }

    private void setName(AgendaName name) {
        FieldValidator.requiresNonNull("Agenda name", name);
        this.name = name;
    }

    private void setServiceSubcategory(ServiceSubCategory subCategory) {
        FieldValidator.requiresNonNull("subCategory", subCategory);
        this.serviceSubCategory = subCategory;
    }

    private void setStatus(AgendaStatus status) {
        FieldValidator.requiresNonNull("Agenda status", status);
        this.status = status;
    }

    private void setScheduledTimes(Set<ScheduledTime> scheduledTimes) {
        this.scheduledTimes = scheduledTimes;
    }

    private void setCreatedAt(OffsetDateTime createdAt) {
        FieldValidator.requiresNonNull("createdAt", createdAt);
        this.createdAt = createdAt;
    }

    private void setActivedAt(OffsetDateTime activedAt) {
        this.activedAt = activedAt;
    }

    private void setStandByAt(OffsetDateTime standByAt) {
        this.standByAt = standByAt;
    }

    private void setBlockedAt(OffsetDateTime blockedAt) {
        this.blockedAt = blockedAt;
    }

    private void setStandByDates(Set<StandByDates> standByDates) {
        this.standByDates = standByDates;
    }

    private void setBookingWindow(BookingWindow bookingWindow) {
        FieldValidator.requiresNonNull("bookingWindow", bookingWindow);
        this.bookingWindow = bookingWindow;
    }

    private void setMinuteInterval(MinuteInterval minuteInterval) {
        FieldValidator.requiresNonNull("minuteInterval", minuteInterval);
        this.minuteInterval = minuteInterval;
    }

}
