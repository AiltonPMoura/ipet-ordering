package br.com.ipet.ordering.infrastructure.persistence.mapper;

import br.com.ipet.ordering.domain.model.entity.Breed;
import br.com.ipet.ordering.domain.model.entity.Customer;
import br.com.ipet.ordering.domain.model.entity.Gender;
import br.com.ipet.ordering.domain.model.entity.Pet;
import br.com.ipet.ordering.domain.model.entity.Size;
import br.com.ipet.ordering.domain.model.valueobject.Address;
import br.com.ipet.ordering.domain.model.valueobject.CelPhone;
import br.com.ipet.ordering.domain.model.valueobject.CustomerId;
import br.com.ipet.ordering.domain.model.valueobject.Document;
import br.com.ipet.ordering.domain.model.valueobject.Email;
import br.com.ipet.ordering.domain.model.valueobject.FullName;
import br.com.ipet.ordering.domain.model.valueobject.PetId;
import br.com.ipet.ordering.domain.model.valueobject.PetName;
import br.com.ipet.ordering.domain.model.valueobject.Weight;
import br.com.ipet.ordering.domain.model.valueobject.ZipCode;
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
