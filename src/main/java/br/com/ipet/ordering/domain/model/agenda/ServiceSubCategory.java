package br.com.ipet.ordering.domain.model.agenda;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static br.com.ipet.ordering.domain.model.agenda.ServiceCategory.CAT;
import static br.com.ipet.ordering.domain.model.agenda.ServiceCategory.DOG;
import static br.com.ipet.ordering.domain.model.agenda.WorkShift.*;

@RequiredArgsConstructor
public enum ServiceSubCategory {
    DOG_HYGIENE("Higienização para cães", DOG, DAY_SHIFT),
    DOG_HEALTH("Saúde para cães", DOG, DAY_SHIFT),
    DOG_DAYCARE("Creche para cães", DOG, DAY_SHIFT),
    DOG_HOSTING("Hospedagem para cães", DOG, NIGHT_SHIFT),

    CAT_HYGIENE("Higienização para gatos", CAT, DAY_SHIFT),
    CAR_HEALTH("Saúde para gatos", CAT, DAY_SHIFT);

    private final String description;
    private final ServiceCategory serviceCategory;
    private final WorkShift workShift;

    public String description() {
        return description;
    }

    public ServiceCategory serviceCategory() {
        return this.serviceCategory;
    }

    public WorkShift workShift() {
        return workShift;
    }

    public boolean isNightShift() {
        return this.workShift.equals(NIGHT_SHIFT);
    }
}
