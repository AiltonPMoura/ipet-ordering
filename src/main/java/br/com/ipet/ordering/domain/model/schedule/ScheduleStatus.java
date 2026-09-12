package br.com.ipet.ordering.domain.model.schedule;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum ScheduleStatus {
    INACTIVE,
    ACTIVE,
    LOCKED;

    private static final Map<ScheduleStatus, Set<ScheduleStatus>> ALLOWED_TRANSITIONS;

    static {
        var statusAllowed = new EnumMap<ScheduleStatus, Set<ScheduleStatus>>(ScheduleStatus.class);
        statusAllowed.put(INACTIVE, EnumSet.of(ACTIVE));
        statusAllowed.put(ACTIVE, EnumSet.of(LOCKED, INACTIVE));
        statusAllowed.put(LOCKED, EnumSet.of(ACTIVE));
        ALLOWED_TRANSITIONS = Collections.unmodifiableMap(statusAllowed);
    }

    public boolean canChangeTo(ScheduleStatus newStatus) {
        if (newStatus == null) return false;
        return ALLOWED_TRANSITIONS.getOrDefault(this, Collections.emptySet()).contains(newStatus);
    }

    public boolean canNotChangeTo(ScheduleStatus newStatus) {
        return !canChangeTo(newStatus);
    }
}
