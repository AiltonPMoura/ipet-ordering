package br.com.ipet.ordering.domain.model.entity;

import lombok.RequiredArgsConstructor;

import java.util.List;

import static br.com.ipet.ordering.domain.model.entity.ServiceSubCategory.*;

@RequiredArgsConstructor
public enum ServiceCategory {
    DOG(1, "Cachorro", List.of(DOG_HYGIENE, DOG_HEALTH, DOG_DAYCARE, DOG_HOSTING)),
    CAT(2, "Gato", List.of(CAT_HYGIENE, CAR_HEALTH));

    public String nameValue() {
        return this.nameValue;
    }

    public Integer id() {
        return this.id;
    }

    private final Integer id;
    private final String nameValue;
    private final List<ServiceSubCategory> subCategories;

    public List<ServiceSubCategory> subCategories() {
        return this.subCategories;
    }
}
