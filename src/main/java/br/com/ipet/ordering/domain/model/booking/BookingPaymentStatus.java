package br.com.ipet.ordering.domain.model.booking;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum BookingPaymentStatus {
    PENDING,
    PAID,
    REJECTED,
    REFUNDED;

    private static final Map<BookingPaymentStatus, Set<BookingPaymentStatus>> ALLOWED_TRANSITIONS;

    static {
        var statusAllowed = new EnumMap<BookingPaymentStatus, Set<BookingPaymentStatus>>(BookingPaymentStatus.class);
        statusAllowed.put(PENDING, EnumSet.of(PAID, REJECTED));
        statusAllowed.put(PAID, EnumSet.of(REFUNDED));
        statusAllowed.put(REJECTED, EnumSet.of(PAID, PENDING));
        statusAllowed.put(REFUNDED, EnumSet.noneOf(BookingPaymentStatus.class));
        ALLOWED_TRANSITIONS = Collections.unmodifiableMap(statusAllowed);
    }

    public boolean canChangeTo(BookingPaymentStatus newStatus) {
        if (newStatus == null) return false;
        return ALLOWED_TRANSITIONS.getOrDefault(this, Collections.emptySet()).contains(newStatus);
    }

    public boolean canNotChangeTo(BookingPaymentStatus newStatus) {
        return !canChangeTo(newStatus);
    }
}
