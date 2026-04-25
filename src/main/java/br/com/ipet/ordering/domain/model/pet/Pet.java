package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.FieldValidator;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.Objects;

public class Pet implements AggregateRoot<PetId> {
    private PetId id;
    private CustomerId customerId;
    private PetName name;
    private Type type;
    private Breed breed;
    private Gender gender;
    private Size size;
    private PetWeight weight;
    private PetAge age;

    @Builder(builderClassName = "createNewPetBuilder", builderMethodName = "createNew", access = AccessLevel.PACKAGE)
    private static Pet create(PetName name, CustomerId customerId,
                              Type type, Breed breed, Gender gender,
                              Size size, PetWeight weight, PetAge age) {
        return new Pet(new PetId(), customerId, name, type, breed, gender, size, weight, age);
    }

    @Builder(builderClassName = "createExistingPetBuilder", builderMethodName = "existing")
    private Pet(PetId id, CustomerId customerId,
                PetName name, Type type, Breed breed,
                Gender gender, Size size, PetWeight weight, PetAge age) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setName(name);
        this.setType(type);
        this.setBreed(breed);
        this.setGender(gender);
        this.setSize(size);
        this.setWeight(weight);
        this.setAge(age);
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

    void changeWeight(PetWeight weight) {
        FieldValidator.requiresNonNull("pet weight", weight);
        this.setWeight(weight);
    }

    void changeAge(PetAge age) {
        FieldValidator.requiresNonNull("pet age", age);
        this.setAge(age);
    }

    public PetId id() {
        return id;
    }

    private void setId(PetId id) {
        FieldValidator.requiresNonNull("pet id", id);
        this.id = id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    private void setCustomerId(CustomerId customerId) {
        FieldValidator.requiresNonNull("customer id", customerId);
        this.customerId = customerId;
    }

    public PetName name() {
        return name;
    }

    private void setName(PetName name) {
        FieldValidator.requiresNonNull("pet name", name);
        this.name = name;
    }

    public Type type() {
        return type;
    }

    private void setType(Type type) {
        FieldValidator.requiresNonNull("pet type", type);
        this.type = type;
    }

    public Breed breed() {
        return breed;
    }

    private void setBreed(Breed breed) {
        FieldValidator.requiresNonNull("pet breed", breed);
        this.breed = breed;
    }

    public Gender gender() {
        return gender;
    }

    private void setGender(Gender gender) {
        FieldValidator.requiresNonNull("pet gender", gender);
        this.gender = gender;
    }

    public Size size() {
        return size;
    }

    private void setSize(Size size) {
        FieldValidator.requiresNonNull("pet size", size);
        this.size = size;
    }

    public PetWeight weight() {
        return weight;
    }

    private void setWeight(PetWeight weight) {
        this.weight = weight;
    }

    private void setAge(PetAge age) {
        this.age = age;
    }

    public PetAge age() {
        return age;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Pet pet)) return false;
        return Objects.equals(id, pet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
