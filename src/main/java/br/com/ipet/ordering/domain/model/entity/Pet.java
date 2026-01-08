package br.com.ipet.ordering.domain.model.entity;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.valueobject.CustomerId;
import br.com.ipet.ordering.domain.model.valueobject.PetName;
import br.com.ipet.ordering.domain.model.valueobject.PetId;
import br.com.ipet.ordering.domain.model.valueobject.Weight;
import lombok.Builder;

public class Pet {
    private PetId id;
    private CustomerId customerId;
    private PetName name;
    private Type type;
    private Breed breed;
    private Gender gender;
    private Size size;
    private Weight weight;

    @Builder(builderClassName = "createNewPetBuilder", builderMethodName = "createNew")
    private static Pet create(PetName name, CustomerId customerId,
                              Type type, Breed breed, Gender gender,
                              Size size, Weight weight) {
        return new Pet(new PetId(), customerId, name, type, breed, gender, size, weight);
    }

    @Builder(builderClassName = "createExistingPetBuilder", builderMethodName = "existing")
    private Pet(PetId id, CustomerId customerId,
                PetName name, Type type, Breed breed,
                Gender gender, Size size, Weight weight) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setName(name);
        this.setType(type);
        this.setBreed(breed);
        this.setGender(gender);
        this.setSize(size);
        this.setWeight(weight);
    }

    void changeName(PetName name) {
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

    public CustomerId custumerId() {
        return customerId;
    }

    public PetName name() {
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
        FieldValidator.requiresNonNull("petId", id);
        this.id = id;
    }

    private void setCustomerId(CustomerId customerId) {
        FieldValidator.requiresNonNull("customerId", customerId);
        this.customerId = customerId;
    }

    private void setName(PetName name) {
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
