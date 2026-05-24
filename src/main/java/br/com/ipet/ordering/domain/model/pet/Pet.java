package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.AbstractEventSourceEntity;
import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.commons.valueobject.Photo;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.FieldValidator;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.Objects;

public class Pet
        extends AbstractEventSourceEntity
        implements AggregateRoot<PetId> {
    private PetId id;
    private CustomerId customerId;
    private PetName name;
    private PetType petType;
    private Breed breed;
    private PetGender gender;
    private PetSize petSize;
    private PetWeight weight;
    private PetAge age;
    private Photo photo;

    @Builder(builderClassName = "createNewPetBuilder", builderMethodName = "createNew", access = AccessLevel.PACKAGE)
    private static Pet create(PetName name, CustomerId customerId,
                              PetType petType, Breed breed, PetGender gender,
                              PetSize size, PetWeight weight, PetAge age) {
        return new Pet(new PetId(), customerId, name, petType, breed, gender, size, weight, age);
    }

    @Builder(builderClassName = "createExistingPetBuilder", builderMethodName = "existing")
    private Pet(PetId id, CustomerId customerId,
                PetName name, PetType petType, Breed breed,
                PetGender gender, PetSize size, PetWeight weight, PetAge age) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setName(name);
        this.setPetType(petType);
        this.setBreed(breed);
        this.setPetGender(gender);
        this.setPetSize(size);
        this.setWeight(weight);
        this.setAge(age);
    }

    void changeName(PetName name) {
        this.setName(name);
    }

    void changeType(PetType petType) {
        this.setPetType(petType);
    }

    void changeBreed(Breed breed) {
        this.setBreed(breed);
    }

    void changeGender(PetGender petGender) {
        this.setPetGender(petGender);
    }

    void changeSize(PetSize petSize) {
        this.setPetSize(petSize);
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

    public PetType type() {
        return petType;
    }

    private void setPetType(PetType petType) {
        FieldValidator.requiresNonNull("pet type", petType);
        this.petType = petType;
    }

    public Breed breed() {
        return breed;
    }

    private void setBreed(Breed breed) {
        FieldValidator.requiresNonNull("pet breed", breed);
        this.breed = breed;
    }

    public PetGender gender() {
        return gender;
    }

    private void setPetGender(PetGender petGender) {
        FieldValidator.requiresNonNull("pet gender", gender);
        this.gender = gender;
    }

    public PetSize size() {
        return petSize;
    }

    private void setPetSize(PetSize size) {
        FieldValidator.requiresNonNull("pet size", size);
        this.petSize = size;
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
