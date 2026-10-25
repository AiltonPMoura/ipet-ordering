package br.com.ipet.ordering.domain.model.commons.enums;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum PaymentStatus {
    PENDING,
    PAID,
    REJECTED,
    REFUNDED;

    private static final Map<PaymentStatus, Set<PaymentStatus>> ALLOWED_TRANSITIONS;

    static {
        var statusAllowed = new EnumMap<PaymentStatus, Set<PaymentStatus>>(PaymentStatus.class);
        statusAllowed.put(PENDING, EnumSet.of(PAID, REJECTED));
        statusAllowed.put(PAID, EnumSet.of(REFUNDED));
        statusAllowed.put(REJECTED, EnumSet.of(PAID, PENDING));
        statusAllowed.put(REFUNDED, EnumSet.noneOf(PaymentStatus.class));
        ALLOWED_TRANSITIONS = Collections.unmodifiableMap(statusAllowed);
    }

    public boolean canChangeTo(PaymentStatus newStatus) {
        if (newStatus == null) return false;
        return ALLOWED_TRANSITIONS.getOrDefault(this, Collections.emptySet()).contains(newStatus);
    }

    public boolean canNotChangeTo(PaymentStatus newStatus) {
        return !canChangeTo(newStatus);
    }
}
