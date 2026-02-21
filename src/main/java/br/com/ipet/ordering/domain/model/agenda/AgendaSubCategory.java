package br.com.ipet.ordering.domain.model.agenda;

import lombok.RequiredArgsConstructor;

import static br.com.ipet.ordering.domain.model.agenda.AgendaCategory.CAT;
import static br.com.ipet.ordering.domain.model.agenda.AgendaCategory.DOG;

@RequiredArgsConstructor
public enum AgendaSubCategory {
    DOG_HYGIENE("Higienização para cães", DOG),
    DOG_HEALTH("Saúde para cães", DOG),
    DOG_DAYCARE("Creche para cães", DOG),
    DOG_HOSTING("Hospedagem para cães", DOG),

    CAT_HYGIENE("Higienização para gatos", CAT),
    CAR_HEALTH("Saúde para gatos", CAT);

    private final String description;
    private final AgendaCategory categories;

    public AgendaCategory categories() {
        return this.categories;
    }
}
