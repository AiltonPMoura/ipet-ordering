package br.com.ipet.ordering.domain.model.agenda;

public enum AgendaStatus {
    DRAFT,
    ACTIVED,
    STAND_BY,
    BLOCKED;

    public boolean canChange(AgendaStatus newStatus) {
         return switch (newStatus) {
             case ACTIVED -> this != ACTIVED;
             case STAND_BY -> this == ACTIVED;
             case BLOCKED -> this == ACTIVED || this == STAND_BY;
             default -> false;
         };
    }

    public boolean canNotChange(AgendaStatus newStatus) {
        return !canChange(newStatus);
    }

}
