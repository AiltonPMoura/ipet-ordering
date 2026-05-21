package br.com.ipet.ordering.domain.model.schedule.category;

import lombok.RequiredArgsConstructor;

import static br.com.ipet.ordering.domain.model.schedule.category.ServiceCategory.CAT;
import static br.com.ipet.ordering.domain.model.schedule.category.ServiceCategory.DOG;

@RequiredArgsConstructor
public enum ServiceSubcategory {
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
