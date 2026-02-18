package br.com.ipet.ordering.domain.model.agenda;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.booking.BookingWindow;
import br.com.ipet.ordering.domain.model.customer.CompanyId;
import lombok.Builder;

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
    private AgendaStatus status;
    private Set<WorkingDay> workingDays;
    private OffsetDateTime activedAt;
    private OffsetDateTime standByAt;
    private OffsetDateTime blockedAt;
    private BookingWindow bookingWindow;
    private Set<StandByDates> standByDates;
    private MinuteInterval minuteInterval;

    @Builder(builderClassName = "CreateNewAgendaBuilder", builderMethodName = "createNew")
    private static Agenda create(CompanyId companyId, AgendaName name, MinuteInterval minuteInterval) {
        return new Agenda(new AgendaId(), companyId, name, AgendaStatus.DRAFT, new HashSet<>(),
                null, null, null, new BookingWindow(7), new HashSet<>(), minuteInterval);
    }

    @Builder(builderClassName = "ExistingAgendaBuilder", builderMethodName = "existing")
    public Agenda(AgendaId id, CompanyId companyId, AgendaName name, AgendaStatus status, Set<WorkingDay> workingDays,
                  OffsetDateTime activedAt, OffsetDateTime standByAt, OffsetDateTime blockedAt,
                  BookingWindow bookingWindow, Set<StandByDates> standByDates, MinuteInterval minuteInterval) {
        this.setId(id);
        this.setCompanyId(companyId);
        this.setName(name);
        this.setStatus(status);
        this.setWorkingDays(workingDays);
        this.setActivedAt(activedAt);
        this.setStandByAt(standByAt);
        this.setBlockedAt(blockedAt);
        this.setBookingWindow(bookingWindow);
        this.setStandByDates(standByDates);
        this.setMinuteInterval(minuteInterval);
    }

    private void setMinuteInterval(MinuteInterval minuteInterval) {
        FieldValidator.requiresNonNull("minuteInterval", minuteInterval);
        this.minuteInterval = minuteInterval;
    }

    public void changeName(AgendaName name) {
        this.setName(name);
    }

    public void changeBookingBy(BookingWindow bookingWindow) {
        this.setBookingWindow(bookingWindow);
    }

    public void addWorkingDay(DayTime dayTime) {
        FieldValidator.requiresNonNull("dayTime", dayTime);

        verifyExistingWorkingDay(dayTime);

        var workingDay = WorkingDay.createNew()
                .agendaId(this.id)
                .dayTime(dayTime)
                .build();

        this.workingDays.add(workingDay);
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
        FieldValidator.requiresNonEmpty("workingDays", workingDays);
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

    public WorkingDay findWorkingDay(WorkingDayId workingDayId) {
        return this.workingDays.stream().filter(workingDay ->
                workingDay.id().equals(workingDayId))
                .findFirst()
                .orElseThrow();
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

    public void verifyExistingWorkingDay(DayTime dayTime) {
        var workingDayExisting = this.workingDays.stream().filter(workingDay ->
                        workingDay.dayTime().dayOfWeek().equals(dayTime.dayOfWeek()))
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

    private Optional<WorkingDay> filterWorkingDay(LocalDate date) {
        return this.workingDays.stream()
                .filter(workingDay -> workingDay.isWorkingDay(date))
                .findFirst();
    }



    public boolean isNotStandBy(LocalDate date) {
        return this.standByDates.stream().allMatch(dates -> dates.isNotBetween(date));
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

    public AgendaStatus status() {
        return status;
    }

    public Set<WorkingDay> workingDays() {
        return workingDays;
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

    private void changeStatus(AgendaStatus status) {
        if (this.status.canNotChange(status))
            throw new RuntimeException();

        this.setStatus(status);
    }

    private void verifyIfChangeAble() {
        if (!isDraft())
            throw new AgendaIsNotDraftToChangeException(this.id.toString());
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

    private void setStatus(AgendaStatus status) {
        FieldValidator.requiresNonNull("Agenda status", status);
        this.status = status;
    }

    private void setWorkingDays(Set<WorkingDay> workingDays) {
        this.workingDays = workingDays;
    }

    public void setActivedAt(OffsetDateTime activedAt) {
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

}
