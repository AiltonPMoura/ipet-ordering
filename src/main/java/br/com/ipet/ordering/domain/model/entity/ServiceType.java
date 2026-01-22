package br.com.ipet.ordering.domain.model.entity;

import lombok.RequiredArgsConstructor;

import static br.com.ipet.ordering.domain.model.entity.ServiceSubCategory.*;

@RequiredArgsConstructor
public enum ServiceType {
    BATH("Banho", "Higienização completa", DOG_HYGIENE),
    BATH_GROOMING_SCISSOR("Banho e Tosa na tesoura", "Higienização completa e corte na tesoura", DOG_HYGIENE),
    BATH_GROOMING_CLIPPERS("Banho e Tosa com máquina", "Higienização completa e corte com máquina", DOG_HYGIENE),

    DOG_RABIES_VACCINE("Vacina da raiva", "Vacina da raiva para cães", DOG_HEALTH);

    private final String valueName;
    private final String description;
    private final ServiceSubCategory subCategory;

    public String valueName() {
        return this.valueName;
    }

    public String description() {
        return this.description;
    }

}
