package br.com.ipet.ordering.domain.model.booking.appointment;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum AppointmentStatus {
    DRAFT,
    REQUESTED,
    PAID,
    SCHEDULED,
    PICKING_UP,
    IN_PROGRESS,
    READY,
    DROPPING_OFF,
    RETURNED,
    COMPLETED,
    CANCELED,
    REFUNDED;

    private static final Map<AppointmentStatus, Set<AppointmentStatus>> ALLOWED_TRANSITIONS;

    static {
        var statusAllowed = new EnumMap<AppointmentStatus, Set<AppointmentStatus>>(AppointmentStatus.class);
        statusAllowed.put(DRAFT, EnumSet.of(REQUESTED));
        statusAllowed.put(REQUESTED, EnumSet.of(PAID, CANCELED));
        statusAllowed.put(PAID, EnumSet.of(SCHEDULED, CANCELED));
        statusAllowed.put(SCHEDULED, EnumSet.of(PICKING_UP, IN_PROGRESS, CANCELED));
        statusAllowed.put(PICKING_UP, EnumSet.of(IN_PROGRESS, CANCELED));
        statusAllowed.put(IN_PROGRESS, EnumSet.of(READY, DROPPING_OFF, CANCELED));
        statusAllowed.put(READY, EnumSet.of(DROPPING_OFF, RETURNED));
        statusAllowed.put(DROPPING_OFF, EnumSet.of(RETURNED));
        statusAllowed.put(RETURNED, EnumSet.of(COMPLETED, REFUNDED));
        statusAllowed.put(COMPLETED, EnumSet.noneOf(AppointmentStatus.class));
        statusAllowed.put(CANCELED, EnumSet.of(REFUNDED));
        statusAllowed.put(REFUNDED, EnumSet.noneOf(AppointmentStatus.class));
        ALLOWED_TRANSITIONS = Collections.unmodifiableMap(statusAllowed);
    }

    public boolean canChangeTo(AppointmentStatus newStatus) {
        if (newStatus == null) return false;
        return ALLOWED_TRANSITIONS.getOrDefault(this, Collections.emptySet()).contains(newStatus);
    }

    public boolean canNotChangeTo(AppointmentStatus newStatus) {
        return !canChangeTo(newStatus);
    }

}
