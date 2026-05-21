package br.com.ipet.ordering.domain.model.schedule.category;

import lombok.RequiredArgsConstructor;

import java.util.List;

import static br.com.ipet.ordering.domain.model.schedule.category.ServiceSubcategory.CAR_HEALTH;
import static br.com.ipet.ordering.domain.model.schedule.category.ServiceSubcategory.CAT_HYGIENE;
import static br.com.ipet.ordering.domain.model.schedule.category.ServiceSubcategory.DOG_DAYCARE;
import static br.com.ipet.ordering.domain.model.schedule.category.ServiceSubcategory.DOG_HEALTH;
import static br.com.ipet.ordering.domain.model.schedule.category.ServiceSubcategory.DOG_HOSTING;
import static br.com.ipet.ordering.domain.model.schedule.category.ServiceSubcategory.DOG_HYGIENE;

@RequiredArgsConstructor
public enum ServiceCategory {
    DOG(1, "Cachorro", List.of(DOG_HYGIENE, DOG_HEALTH, DOG_DAYCARE, DOG_HOSTING)),
    CAT(2, "Gato", List.of(CAT_HYGIENE, CAR_HEALTH));

    private final Integer id;
    private final String description;
    private final List<ServiceSubcategory> subCategories;

    public Integer id() {
        return this.id;
    }
    public String description() {
        return this.description;
    }
    public List<ServiceSubcategory> subCategories() {
        return this.subCategories;
    }
}
