package br.com.ipet.ordering.domain.model.schedule;

public enum ScheduleStatus {
    DRAFT,
    ACTIVED,
    LOCKED,
    INACTIVED;

    public boolean canChange(ScheduleStatus newStatus) {
         return switch (newStatus) {
             case ACTIVED -> this != ACTIVED;
             case LOCKED -> this == ACTIVED;
             case INACTIVED -> this == ACTIVED || this == LOCKED;
             default -> false;
         };
    }

    public boolean canNotChange(ScheduleStatus newStatus) {
        return !canChange(newStatus);
    }

}
