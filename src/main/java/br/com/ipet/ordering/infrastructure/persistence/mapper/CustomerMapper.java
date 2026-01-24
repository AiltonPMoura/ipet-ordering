package br.com.ipet.ordering.infrastructure.persistence.mapper;

import br.com.ipet.ordering.domain.model.pet.Breed;
import br.com.ipet.ordering.domain.model.customer.Customer;
import br.com.ipet.ordering.domain.model.pet.Gender;
import br.com.ipet.ordering.domain.model.pet.Pet;
import br.com.ipet.ordering.domain.model.pet.Size;
import br.com.ipet.ordering.domain.model.commons.Address;
import br.com.ipet.ordering.domain.model.commons.CelPhone;
import br.com.ipet.ordering.domain.model.commons.CustomerId;
import br.com.ipet.ordering.domain.model.commons.Document;
import br.com.ipet.ordering.domain.model.commons.Email;
import br.com.ipet.ordering.domain.model.commons.FullName;
import br.com.ipet.ordering.domain.model.commons.PetId;
import br.com.ipet.ordering.domain.model.commons.PetName;
import br.com.ipet.ordering.domain.model.commons.Weight;
import br.com.ipet.ordering.domain.model.commons.ZipCode;
import br.com.ipet.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.entity.PetPersistenceEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class CustomerMapper {

    public Customer toDomainEntity(CustomerPersistenceEntity persistenceEntity) {
        return Customer.existing()
                .id(new CustomerId(persistenceEntity.getId()))
                .fullName(this.toFullName(persistenceEntity))
                .email(new Email(persistenceEntity.getEmail()))
                .celPhone(new CelPhone(persistenceEntity.getCelPhone()))
                .document(new Document(persistenceEntity.getDocument()))
                .address(this.toAddress(persistenceEntity))
                .pets(toDomainEntity(persistenceEntity.getPets()))
                .build();
    }

    private Set<Pet> toDomainEntity(Set<PetPersistenceEntity> petsPersistenceEntity) {
        return petsPersistenceEntity.stream().map(petPersistenceEntity -> Pet.existing()
                .id(new PetId(petPersistenceEntity.getId()))
                .customerId(new CustomerId(petPersistenceEntity.getCustomerId()))
                .name(new PetName(petPersistenceEntity.getName()))
                .breed(Breed.valueOf(petPersistenceEntity.getBreed()))
                .gender(Gender.valueOf(petPersistenceEntity.getGender()))
                .size(Size.valueOf(petPersistenceEntity.getSize()))
                .weight(new Weight(petPersistenceEntity.getWeight()))
                .build()
        ).collect(Collectors.toSet());
    }

    private FullName toFullName(CustomerPersistenceEntity persistenceEntity) {
        return FullName.builder()
                .firstName(persistenceEntity.getFirstName())
                .lastName(persistenceEntity.getLastName())
                .build();
    }

    private Address toAddress(CustomerPersistenceEntity persistenceEntity) {
        var address = persistenceEntity.getAddress();
        return Address.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .complement(address.getComplement())
                .zipCode(new ZipCode(address.getZipCode()))
                .build();
    }

}
