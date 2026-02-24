package br.com.ipet.ordering.domain.model.schedule;

import lombok.RequiredArgsConstructor;

import java.util.List;

import static br.com.ipet.ordering.domain.model.schedule.ServiceSubCategory.CAR_HEALTH;
import static br.com.ipet.ordering.domain.model.schedule.ServiceSubCategory.CAT_HYGIENE;
import static br.com.ipet.ordering.domain.model.schedule.ServiceSubCategory.DOG_DAYCARE;
import static br.com.ipet.ordering.domain.model.schedule.ServiceSubCategory.DOG_HEALTH;
import static br.com.ipet.ordering.domain.model.schedule.ServiceSubCategory.DOG_HOSTING;
import static br.com.ipet.ordering.domain.model.schedule.ServiceSubCategory.DOG_HYGIENE;

@RequiredArgsConstructor
public enum ServiceCategory {
    DOG(1, "Cachorro", List.of(DOG_HYGIENE, DOG_HEALTH, DOG_DAYCARE, DOG_HOSTING)),
    CAT(2, "Gato", List.of(CAT_HYGIENE, CAR_HEALTH));

    private final Integer id;
    private final String description;
    private final List<ServiceSubCategory> subCategories;

    public Integer id() {
        return this.id;
    }
    public String description() {
        return this.description;
    }
    public List<ServiceSubCategory> subCategories() {
        return this.subCategories;
    }
}
