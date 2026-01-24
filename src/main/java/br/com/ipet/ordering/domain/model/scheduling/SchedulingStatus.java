package br.com.ipet.ordering.domain.model.scheduling;

import java.util.Arrays;
import java.util.List;

public enum SchedulingStatus {
    DRAFT,
    PLACED(DRAFT),
    PAID(PLACED),
    SCHEDULED(PAID),
    FETCHING(SCHEDULED),
    DELIVERING(FETCHING),
    COMPLETED(DELIVERING),
    CANCELED(DRAFT, PLACED, PAID, SCHEDULED);

    private List<SchedulingStatus> previousStatus;

    SchedulingStatus(SchedulingStatus... previousStatus) {
        this.previousStatus = Arrays.asList(previousStatus);
    }

    public boolean canChange(SchedulingStatus newStatus) {
        var currentStatus = this;
        return newStatus.previousStatus.contains(currentStatus);
    }

    public boolean canNotChange(SchedulingStatus newStatus) {
        return !canChange(newStatus);
    }

}
