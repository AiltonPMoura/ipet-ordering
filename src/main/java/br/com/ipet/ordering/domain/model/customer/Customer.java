package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.exception.PetNotFoundException;
import br.com.ipet.ordering.domain.model.pet.Breed;
import br.com.ipet.ordering.domain.model.pet.Gender;
import br.com.ipet.ordering.domain.model.pet.Pet;
import br.com.ipet.ordering.domain.model.pet.Size;
import br.com.ipet.ordering.domain.model.pet.Type;
import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.Address;
import br.com.ipet.ordering.domain.model.commons.CelPhone;
import br.com.ipet.ordering.domain.model.commons.CustomerId;
import br.com.ipet.ordering.domain.model.commons.Document;
import br.com.ipet.ordering.domain.model.commons.Email;
import br.com.ipet.ordering.domain.model.commons.FullName;
import br.com.ipet.ordering.domain.model.commons.PetId;
import br.com.ipet.ordering.domain.model.commons.PetName;
import br.com.ipet.ordering.domain.model.commons.Weight;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Customer implements AggregateRoot<CustomerId> {
    private CustomerId id;
    private FullName fullName;
    private Email email;
    private CelPhone celPhone;
    private Document document;
    private Address address;
    private Set<Pet> pets;
    private OffsetDateTime registerAt;

    @Builder(builderClassName = "CreateNewCustomerBuild", builderMethodName = "createNew")
    private static Customer create(FullName fullName, Email email,
                                   CelPhone celPhone, Document document, Address address) {
        return new Customer(new CustomerId(), fullName, email, celPhone,
                document, address, OffsetDateTime.now(), new HashSet<>());
    }

    @Builder(builderClassName = "CreateExistingCustomerBuild", builderMethodName = "existing")
    private Customer(CustomerId id, FullName fullName, Email email, CelPhone celPhone, Document document,
                     Address address, OffsetDateTime registerAt, Set<Pet> pets) {
        this.setId(id);
        this.setFullName(fullName);
        this.setEmail(email);
        this.setCelPhone(celPhone);
        this.setDocument(document);
        this.setAddress(address);
        this.setPet(pets);
        this.setRegisterAt(registerAt);
    }

    public void addPet(PetName petName, Type type, Breed breed,
                       Gender gender, Size size, Weight weight) {
        var pet = Pet.createNew()
                .customerId(this.id)
                .name(petName)
                .type(type)
                .breed(breed)
                .gender(gender)
                .size(size)
                .weight(weight)
                .build();

        this.pets.add(pet);
    }

    public void changeFullName(FullName fullName) {
        this.setFullName(fullName);
    }

    public void changeEmail(Email email) {
        this.setEmail(email);
    }

    public void changeCelPhone(CelPhone celPhone) {
        this.setCelPhone(celPhone);
    }

    public void changeDocument(Document document) {
        this.setDocument(document);
    }

    public void changeAddress(Address address) {
        this.setAddress(address);
    }

    public void changePetName(PetId id, PetName petName) {
        var pet = findPet(id);
        pet.changeName(petName);
    }

    public void changePetType(PetId id, Type type) {
        var pet = findPet(id);
        pet.changeType(type);
    }

    public void changePetBreed(PetId id, Breed breed) {
        var pet = findPet(id);
        pet.changeBreed(breed);
    }

    public void changePetGender(PetId id, Gender gender) {
        var pet = findPet(id);
        pet.changeGender(gender);
    }

    public void changePetName(PetId id, Size size) {
        var pet = findPet(id);
        pet.changeSize(size);
    }

    public void changePetWeight(PetId id, Weight weight) {
        var pet = findPet(id);
        pet.changeWeight(weight);
    }

    public CustomerId id() {
        return id;
    }

    public FullName fullName() {
        return fullName;
    }

    public Email email() {
        return email;
    }

    public CelPhone celPhone() {
        return celPhone;
    }

    public Document document() {
        return document;
    }

    public Address address() {
        return address;
    }

    public Set<Pet> pets() {
        return Collections.unmodifiableSet(pets);
    }

    public OffsetDateTime registerAt() {
        return registerAt;
    }

    private Pet findPet(PetId id) {
        return this.pets.stream()
                .filter(pet -> pet.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new PetNotFoundException(this.id.value().toString(), id.toString()));
    }

    private void setId(CustomerId id) {
        FieldValidator.requiresNonNull("customer id", id);
        this.id = id;
    }

    private void setFullName(FullName fullName) {
        FieldValidator.requiresNonNull("customer fullName", fullName);
        this.fullName = fullName;
    }

    private void setEmail(Email email) {
        FieldValidator.requiresNonNull("customer email", email);
        this.email = email;
    }

    private void setCelPhone(CelPhone celPhone) {
        FieldValidator.requiresNonNull("customer celPhone", celPhone);
        this.celPhone = celPhone;
    }

    private void setDocument(Document document) {
        FieldValidator.requiresNonNull("customer document", document);
        this.document = document;
    }

    private void setAddress(Address address) {
        FieldValidator.requiresNonNull("customer address", address);
        this.address = address;
    }

    private void setPet(Set<Pet> pets) {
        FieldValidator.requiresNonNull("customer pets", pets);
        this.pets = pets;
    }

    private void setRegisterAt(OffsetDateTime registerAt) {
        FieldValidator.requiresNonNull("registerAt", registerAt);
        this.registerAt = registerAt;
    }
}
