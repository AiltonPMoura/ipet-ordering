package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.AbstractEventSourceEntity;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.exception.CannotChangeStatusException;
import br.com.ipet.ordering.domain.model.company.CompanyId;

import java.time.OffsetDateTime;

public abstract class Schedule extends AbstractEventSourceEntity {
    private ScheduleId id;
    private CompanyId companyId;
    private ScheduleName name;
    private ScheduleStatus status;
    private OffsetDateTime createdAt;

    protected Schedule(ScheduleId id, CompanyId companyId, ScheduleName name, ScheduleStatus status, OffsetDateTime createdAt) {
        this.setId(id);
        this.setCompanyId(companyId);
        this.setName(name);
        this.setStatus(status);
        this.setCreatedAt(createdAt);
    }

    protected void changeName(ScheduleName name) {
        this.setName(name);
    }

    protected void active() {
        this.changeStatus(ScheduleStatus.ACTIVED);
    }

    protected void changeToStandBy() {
        this.changeStatus(ScheduleStatus.STAND_BY);
    }

    protected void changeToblock() {
        this.changeStatus(ScheduleStatus.BLOCKED);
    }

    public boolean isDraft() {
        return ScheduleStatus.DRAFT.equals(this.status);
    }

    public boolean isActived() {
        return ScheduleStatus.ACTIVED.equals(this.status);
    }

    public boolean isStantBy() {
        return ScheduleStatus.STAND_BY.equals(this.status);
    }

    public boolean isblocked() {
        return ScheduleStatus.BLOCKED.equals(this.status);
    }

    private void changeStatus(ScheduleStatus status) {
        if (this.status.canNotChange(status))
            throw new CannotChangeStatusException("", "");

        this.setStatus(status);
    }



    /*public void addStandByDates(StandByDates standByDates) {
        FieldValidator.requiresNonNull("standByDates", standByDates);
        this.verifyIsAlreadyStandBy(standByDates);

        this.standByDates.add(standByDates);
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
        FieldValidator.requiresNonNull("id", id);
        this.id = id;
    }

    public CompanyId companyId() {
        return companyId;
    }

    private void setCompanyId(CompanyId companyId) {
        FieldValidator.requiresNonNull("company id", id);
        this.companyId = companyId;
    }

    public ScheduleName name() {
        return name;
    }

    private void setName(ScheduleName name) {
        FieldValidator.requiresNonNull("name", name);
        this.name = name;
    }

    public ScheduleStatus status() {
        return status;
    }

    private void setStatus(ScheduleStatus status) {
        FieldValidator.requiresNonNull("status", status);
        this.status = status;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    private void setCreatedAt(OffsetDateTime createdAt) {
        FieldValidator.requiresNonNull("createdAt", createdAt);
        this.createdAt = createdAt;
    }


}
