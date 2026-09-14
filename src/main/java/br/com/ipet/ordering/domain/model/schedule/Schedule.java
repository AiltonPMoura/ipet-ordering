package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.AbstractEventSourceEntity;
import br.com.ipet.ordering.domain.model.commons.exception.CannotChangeStatusException;
import br.com.ipet.ordering.domain.model.company.CompanyId;

import java.time.OffsetDateTime;
import java.util.Set;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;

public abstract class Schedule extends AbstractEventSourceEntity {
    private ScheduleId id;
    private CompanyId companyId;
    private ScheduleName name;
    private ServiceCategory serviceCategory;
    private ScheduleStatus status;
    private Set<LockedDate> lockedDates;
    private OffsetDateTime createdAt;

    protected Schedule(ScheduleId id, CompanyId companyId,
                       ScheduleName name, ServiceCategory serviceCategory,
                       ScheduleStatus status, Set<LockedDate> lockedDates,
                       OffsetDateTime createdAt) {
        this.setId(id);
        this.setCompanyId(companyId);
        this.setName(name);
        this.setServiceCategory(serviceCategory);
        this.setStatus(status);
        this.setLockedDates(lockedDates);
        this.setCreatedAt(createdAt);
    }

    protected void addlockedDays(Set<LockedDate> lockedDates) {
        requiresNonNull("lockedDates", lockedDates);
        this.lockedDates.addAll(lockedDates);
    }

    protected void removelockedDays(Set<LockedDate> lockedDates) {
        requiresNonNull("lockedDates", lockedDates);
        this.lockedDates.removeAll(lockedDates);
    }

    protected void changeName(ScheduleName name) {
        this.setName(name);
    }

    protected void active(CompanyId companyId) {
        this.changeStatus(ScheduleStatus.ACTIVE);
    }

    protected void lock() {
        this.changeStatus(ScheduleStatus.LOCKED);
    }

    protected void inactive() {
        this.changeStatus(ScheduleStatus.INACTIVE);
    }

    protected void verifyBelongToCompany(CompanyId companyId) {
        if (!this.companyId().equals(companyId))
            throw new ScheduleDoesNotBelongToCompany("");
    }

    public boolean isActive() {
        return ScheduleStatus.ACTIVE.equals(this.status);
    }

    public boolean isLocked() {
        return ScheduleStatus.LOCKED.equals(this.status);
    }

    public boolean isInactive() {
        return ScheduleStatus.INACTIVE.equals(this.status);
    }

    private void changeStatus(ScheduleStatus status) {
        if (this.status.canNotChangeTo(status))
            throw new CannotChangeStatusException("", "");

        this.setStatus(status);
    }

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
        requiresNonNull("company id", companyId);
        this.companyId = companyId;
    }

    public ScheduleName name() {
        return name;
    }

    private void setName(ScheduleName name) {
        requiresNonNull("name", name);
        this.name = name;
    }

    public ServiceCategory serviceCategory() {
        return serviceCategory;
    }

    private void setServiceCategory(ServiceCategory serviceCategory) {
        requiresNonNull("serviceCategory", serviceCategory);
        this.serviceCategory = serviceCategory;
    }

    public ScheduleStatus status() {
        return status;
    }

    private void setStatus(ScheduleStatus status) {
        requiresNonNull("status", status);
        this.status = status;
    }

    public Set<LockedDate> lockedDates() {
        return lockedDates;
    }

    private void setLockedDates(Set<LockedDate> lockedDates) {
        requiresNonNull("lockedDates", lockedDates);
        this.lockedDates = lockedDates;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    private void setCreatedAt(OffsetDateTime createdAt) {
        requiresNonNull("createdAt", createdAt);
        this.createdAt = createdAt;
    }


}
