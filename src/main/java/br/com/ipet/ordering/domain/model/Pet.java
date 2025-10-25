package br.com.ipet.ordering.domain.model;

import br.com.ipet.ordering.domain.util.FieldValidator;
import br.com.ipet.ordering.domain.valueobject.Name;
import br.com.ipet.ordering.domain.valueobject.PetId;
import br.com.ipet.ordering.domain.valueobject.Weight;
import lombok.Builder;

public class Pet {
    private PetId id;
    private Name name;
    private Type type;
    private Breed breed;
    private Gender gender;
    private Size size;
    private Weight weight;

    @Builder(builderClassName = "createNewPetBuilder", builderMethodName = "createNew")
    private static Pet create(Name name, Type type, Breed breed, Gender gender, Size size, Weight weight) {
        return new Pet(new PetId(), name, type, breed, gender, size, weight);
    }

    @Builder(builderClassName = "createExistingPetBuilder", builderMethodName = "existing")
    private Pet(PetId id, Name name, Type type, Breed breed, Gender gender, Size size, Weight weight) {
        this.setId(id);
        this.setName(name);
        this.setType(type);
        this.setBreed(breed);
        this.setGender(gender);
        this.setSize(size);
        this.setWeight(weight);
    }

    void changeName(Name name) {
        this.setName(name);
    }

    void changeType(Type type) {
        this.setType(type);
    }

    void changeBreed(Breed breed) {
        this.setBreed(breed);
    }

    void changeGender(Gender gender) {
        this.setGender(gender);
    }

    void changeSize(Size size) {
        this.setSize(size);
    }

    void changeWeight(Weight weight) {
        FieldValidator.requiresNonNull("pet wheight", weight);
    }

    public PetId id() {
        return id;
    }

    public Name name() {
        return name;
    }

    public Type type() {
        return type;
    }

    public Breed breed() {
        return breed;
    }

    public Gender gender() {
        return gender;
    }

    public Size size() {
        return size;
    }

    public Weight weight() {
        return weight;
    }

    private void setId(PetId id) {
        FieldValidator.requiresNonNull("pet id", id);
        this.id = id;
    }

    private void setName(Name name) {
        FieldValidator.requiresNonNull("pet name", name);
        this.name = name;
    }

    private void setType(Type type) {
        FieldValidator.requiresNonNull("pet type", type);
        this.type = type;
    }

    private void setBreed(Breed breed) {
        FieldValidator.requiresNonNull("pet breed", breed);
        this.breed = breed;
    }

    private void setGender(Gender gender) {
        FieldValidator.requiresNonNull("pet gender", gender);
        this.gender = gender;
    }

    private void setSize(Size size) {
        FieldValidator.requiresNonNull("pet size", size);
        this.size = size;
    }

    private void setWeight(Weight weight) {
        this.weight = weight;
    }
}
