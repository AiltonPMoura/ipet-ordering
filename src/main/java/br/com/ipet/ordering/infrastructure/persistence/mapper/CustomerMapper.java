package br.com.ipet.ordering.infrastructure.persistence.mapper;

import br.com.ipet.ordering.domain.model.pet.Breed;
import br.com.ipet.ordering.domain.model.customer.Customer;
import br.com.ipet.ordering.domain.model.pet.Gender;
import br.com.ipet.ordering.domain.model.pet.Pet;
import br.com.ipet.ordering.domain.model.pet.Size;
import br.com.ipet.ordering.domain.model.commons.Address;
import br.com.ipet.ordering.domain.model.commons.CelPhone;
import br.com.ipet.ordering.domain.model.commons.CustomerId;
import br.com.ipet.ordering.domain.model.commons.Cpf;
import br.com.ipet.ordering.domain.model.commons.Email;
import br.com.ipet.ordering.domain.model.commons.FullName;
import br.com.ipet.ordering.domain.model.commons.PetId;
import br.com.ipet.ordering.domain.model.commons.PetName;
import br.com.ipet.ordering.domain.model.commons.Weight;
import br.com.ipet.ordering.domain.model.commons.ZipCode;
import br.com.ipet.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.pet.PetPersistenceEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class CustomerMapper {

    public Customer toDomainEntity(CustomerPersistenceEntity persistenceEntity) {
        return Customer.existing()
                .id(new CustomerId(persistenceEntity.getId()))
                .fullName(this.toFullName(persistenceEntity))
                .email(new Email(persistenceEntity.getEmail()))
                .celPhone(new CelPhone(persistenceEntity.getCelPhone()))
                .cpf(new Cpf(persistenceEntity.getDocument()))
                .address(this.toAddress(persistenceEntity))
                .build();
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
