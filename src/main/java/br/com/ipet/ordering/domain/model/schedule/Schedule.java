package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.AbstractEventSourceEntity;
import br.com.ipet.ordering.domain.model.commons.exception.CannotChangeStatusException;
import br.com.ipet.ordering.domain.model.company.CompanyId;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

import static br.com.ipet.ordering.domain.model.FieldValidator.*;

public abstract class Schedule extends AbstractEventSourceEntity {
    private ScheduleId id;
    private CompanyId companyId;
    private ScheduleName name;
    private ScheduleStatus status;
    private Set<LockedDate> lockedDates;
    private LocalDate startedAt;
    private OffsetDateTime createdAt;

    protected Schedule(ScheduleId id, CompanyId companyId, ScheduleName name,
                       ScheduleStatus status, Set<LockedDate> lockedDates, LocalDate startedAt, OffsetDateTime createdAt) {
        this.setId(id);
        this.setCompanyId(companyId);
        this.setName(name);
        this.setStatus(status);
        this.setLockedDays(lockedDates);
        this.setStartedAt(startedAt);
        this.setCreatedAt(createdAt);
    }

    protected void addlockedDays(Set<LockedDate> lockedDates) {
        requiresNonNull("lockedDay", lockedDates);

        if (isDraft())
            throw new CannotAddLockedDateInStatusDraft("");

        if (isInactived())
            throw new CannotAddLockedDateInStatusInactived("");

        this.lockedDates.addAll(lockedDates);
    }

    protected void removelockedDays(Set<LockedDate> lockedDates) {
        requiresNonNull("lockedDays", lockedDates);
        this.lockedDates.removeAll(lockedDates);
    }

    protected void changeName(ScheduleName name) {
        this.setName(name);
    }

    protected void active(CompanyId companyId) {
        this.changeStatus(ScheduleStatus.ACTIVED);

        if (Objects.isNull(startedAt)) {
            this.setStartedAt(LocalDate.now());
        }
    }

    protected void lock() {
        this.changeStatus(ScheduleStatus.LOCKED);
    }

    protected void inactive() {
        this.changeStatus(ScheduleStatus.INACTIVED);
    }

    protected void verifyBelongToCompany(CompanyId companyId) {
        if (!this.companyId().equals(companyId))
            throw new ScheduleDoesNotBelongToCompany("");
    }

    public boolean isDraft() {
        return ScheduleStatus.DRAFT.equals(this.status);
    }

    public boolean isActived() {
        return ScheduleStatus.ACTIVED.equals(this.status);
    }

    public boolean isLocked() {
        return ScheduleStatus.LOCKED.equals(this.status);
    }

    public boolean isInactived() {
        return ScheduleStatus.INACTIVED.equals(this.status);
    }

    private void changeStatus(ScheduleStatus status) {
        if (this.status.canNotChange(status))
            throw new CannotChangeStatusException("", "");

        this.setStatus(status);
    }

    /*public List<AvailableDateTimes> avaliableDatesTimes() {
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

    public boolean isNotStandBy(LocalDate date) {
        return this.standByDates.stream().allMatch(dates -> dates.isNotBetween(date));
    }

    private Optional<DayTimeScheduled> filterWorkingDay(LocalDate date) {
        return this.scheduledTimes.stream()
                .filter(scheduledTime -> scheduledTime.isWorkingDay(date))
                .findFirst();
    }

    private Optional<StandByDates> currentStandBy() {
        return this.standByDates.stream()
                .filter(StandByDates::isCurrent)
                .findFirst();
    }*/

    public ScheduleId id() {
        return id;
    }

    private void setId(ScheduleId id) {
        requiresNonNull("id", id);
        this.id = id;
    }

    public CompanyId companyId() {
        return companyId;
    }

    private void setCompanyId(CompanyId companyId) {
        requiresNonNull("company id", id);
        this.companyId = companyId;
    }

    public ScheduleName name() {
        return name;
    }

    private void setName(ScheduleName name) {
        requiresNonNull("name", name);
        this.name = name;
    }

    public ScheduleStatus status() {
        return status;
    }

    private void setStatus(ScheduleStatus status) {
        requiresNonNull("status", status);
        this.status = status;
    }

    public Set<LockedDate> lockedDays() {
        return lockedDates;
    }

    private void setLockedDays(Set<LockedDate> lockedDates) {
        this.lockedDates = lockedDates;
    }

    public LocalDate startedAt() {
        return startedAt;
    }

    private void setStartedAt(LocalDate startedAt) {
        this.startedAt = startedAt;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    private void setCreatedAt(OffsetDateTime createdAt) {
        requiresNonNull("createdAt", createdAt);
        this.createdAt = createdAt;
    }


}
