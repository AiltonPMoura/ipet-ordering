package br.com.ipet.ordering.domain.model.entity;

import lombok.RequiredArgsConstructor;

import java.util.List;

import static br.com.ipet.ordering.domain.model.entity.ProductSubCategory.*;

@RequiredArgsConstructor
public enum ProductCategory {
    DOG(1, "Cachorro", List.of(DOG_FOOD, DOG_SNAKE)),
    CAT(2, "Gato", List.of(CAT_FOOD, CAT_SNAKE));

    public String nameValue() {
        return this.nameValue;
    }

    public Integer id() {
        return this.id;
    }

    public List<ProductSubCategory> subCategories() {
        return this.subCategories;
    }

    private final Integer id;
    private final String nameValue;
    private final List<ProductSubCategory> subCategories;
}
