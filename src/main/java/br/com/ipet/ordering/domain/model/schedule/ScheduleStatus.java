package br.com.ipet.ordering.domain.model.schedule;

public enum ScheduleStatus {
    INACTIVE,
    ACTIVE;

    public boolean canChangeTo(ScheduleStatus newStatus) {
        return newStatus != null && this != newStatus;
    }

    public boolean canNotChangeTo(ScheduleStatus newStatus) {
        return !canChangeTo(newStatus);
    }
}
