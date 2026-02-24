package br.com.ipet.ordering.domain.model.schedule;

import lombok.RequiredArgsConstructor;

import static br.com.ipet.ordering.domain.model.schedule.ServiceCategory.CAT;
import static br.com.ipet.ordering.domain.model.schedule.ServiceCategory.DOG;
import static br.com.ipet.ordering.domain.model.schedule.WorkShift.*;

@RequiredArgsConstructor
public enum ServiceSubCategory {
    DOG_HYGIENE("Higienização para cães", DOG),
    DOG_HEALTH("Saúde para cães", DOG),
    DOG_DAYCARE("Creche para cães", DOG),
    DOG_HOSTING("Hospedagem para cães", DOG),

    CAT_HYGIENE("Higienização para gatos", CAT),
    CAR_HEALTH("Saúde para gatos", CAT);

    private final String description;
    private final ServiceCategory serviceCategory;

    public String description() {
        return description;
    }

    public ServiceCategory serviceCategory() {
        return this.serviceCategory;
    }

}
