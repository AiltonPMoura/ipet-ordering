package br.com.ipet.ordering.domain.model.schedule;

public enum ScheduleStatus {
    DRAFT,
    ACTIVED,
    STAND_BY,
    BLOCKED;

    public boolean canChange(ScheduleStatus newStatus) {
         return switch (newStatus) {
             case ACTIVED -> this != ACTIVED;
             case STAND_BY -> this == ACTIVED;
             case BLOCKED -> this == ACTIVED || this == STAND_BY;
             default -> false;
         };
    }

    public boolean canNotChange(ScheduleStatus newStatus) {
        return !canChange(newStatus);
    }

}
