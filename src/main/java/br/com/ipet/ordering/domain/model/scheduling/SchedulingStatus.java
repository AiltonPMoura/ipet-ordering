package br.com.ipet.ordering.domain.model.scheduling;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum SchedulingStatus {
    DRAFT,
    PLACED,
    PAID,
    WAITING_CONFIRMATION,
    SCHEDULED,
    WAITING_RESCHEDULING,
    IN_PROGRESS,
    COMPLETED,
    CANCELED;

    private static final Map<SchedulingStatus, Set<SchedulingStatus>> STATUS_ALLOWED;

    static {
        var statusAllowed = new EnumMap<SchedulingStatus, Set<SchedulingStatus>>(SchedulingStatus.class);
        statusAllowed.put(DRAFT, EnumSet.of(PLACED));
        statusAllowed.put(PLACED, EnumSet.of(DRAFT, PAID));
        statusAllowed.put(PAID, EnumSet.of(WAITING_CONFIRMATION, CANCELED));
        statusAllowed.put(WAITING_CONFIRMATION, EnumSet.of(SCHEDULED, CANCELED));
        statusAllowed.put(SCHEDULED, EnumSet.of(WAITING_RESCHEDULING, IN_PROGRESS, CANCELED));
        statusAllowed.put(WAITING_RESCHEDULING, EnumSet.of(SCHEDULED, CANCELED));
        statusAllowed.put(IN_PROGRESS, EnumSet.of(COMPLETED, CANCELED));
        statusAllowed.put(COMPLETED, EnumSet.noneOf(SchedulingStatus.class));
        statusAllowed.put(CANCELED, EnumSet.noneOf(SchedulingStatus.class));
        STATUS_ALLOWED = Collections.unmodifiableMap(statusAllowed);
    }

    public boolean canChangeTo(SchedulingStatus newStatus) {
        if (newStatus == null) return false;
        return STATUS_ALLOWED.getOrDefault(this, EnumSet.noneOf(SchedulingStatus.class)).contains(newStatus);
    }

    public boolean canNotChangeTo(SchedulingStatus newStatus) {
        return !canChangeTo(newStatus);
    }

}
