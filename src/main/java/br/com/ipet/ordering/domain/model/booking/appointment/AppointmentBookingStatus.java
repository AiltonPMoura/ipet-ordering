package br.com.ipet.ordering.domain.model.booking.appointment;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum AppointmentBookingStatus {
    DRAFT,
    REQUESTED,
    PENDING_APPROVAL,
    SCHEDULED,
    PICKING_UP,
    IN_PROGRESS,
    READY,
    DROPPING_OFF,
    RETURNED,
    COMPLETED,
    CANCELED;

    private static final Map<AppointmentBookingStatus, Set<AppointmentBookingStatus>> ALLOWED_TRANSITIONS;

    static {
        var statusAllowed = new EnumMap<AppointmentBookingStatus, Set<AppointmentBookingStatus>>(AppointmentBookingStatus.class);
        statusAllowed.put(DRAFT, EnumSet.of(REQUESTED));
        statusAllowed.put(REQUESTED, EnumSet.of(PENDING_APPROVAL));
        statusAllowed.put(PENDING_APPROVAL, EnumSet.of(SCHEDULED, CANCELED));
        statusAllowed.put(SCHEDULED, EnumSet.of(PICKING_UP, IN_PROGRESS, CANCELED));
        statusAllowed.put(PICKING_UP, EnumSet.of(IN_PROGRESS, CANCELED));
        statusAllowed.put(IN_PROGRESS, EnumSet.of(READY, DROPPING_OFF, CANCELED));
        statusAllowed.put(READY, EnumSet.of(DROPPING_OFF, RETURNED));
        statusAllowed.put(DROPPING_OFF, EnumSet.of(RETURNED));
        statusAllowed.put(RETURNED, EnumSet.of(COMPLETED));
        statusAllowed.put(COMPLETED, EnumSet.noneOf(AppointmentBookingStatus.class));
        ALLOWED_TRANSITIONS = Collections.unmodifiableMap(statusAllowed);
    }

    public boolean canChangeTo(AppointmentBookingStatus newStatus) {
        if (newStatus == null) return false;
        return ALLOWED_TRANSITIONS.getOrDefault(this, Collections.emptySet()).contains(newStatus);
    }

    public boolean canNotChangeTo(AppointmentBookingStatus newStatus) {
        return !canChangeTo(newStatus);
    }

}
