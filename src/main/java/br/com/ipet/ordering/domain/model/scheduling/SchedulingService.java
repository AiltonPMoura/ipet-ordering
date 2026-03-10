package br.com.ipet.ordering.domain.model.scheduling;

import br.com.ipet.ordering.domain.model.commons.valueobject.Service;
import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.company.CompanyNotFoundException;
import br.com.ipet.ordering.domain.model.customer.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerDoesNotContainAnyPet;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.customer.CustomerNotFoundException;
import br.com.ipet.ordering.domain.model.customer.Customers;
import br.com.ipet.ordering.domain.model.pet.Pet;
import br.com.ipet.ordering.domain.model.pet.PetDoesNotBelongToTheCustomer;
import br.com.ipet.ordering.domain.model.pet.PetId;
import br.com.ipet.ordering.domain.model.pet.Pets;
import br.com.ipet.ordering.domain.model.service.ServiceDomain;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class SchedulingService {

    private Customers customers;
    private Companies companies;
    private Pets pets;
    private ServiceDomain serviceDomain;

    public Scheduling generate(CustomerId customerId,
                               CompanyId companyId,
                               Set<PetId> petIds,
                               Service service) {

        var pets = petsOfCustomer(customerId);

        verifyCustomerExists(customerId);
        verifyCompanyExists(companyId);
        verifyPetsBelongsToTheCustomer(pets, petIds);
        verifyServiceSuportPetSize(pets, petIds, service);

        //TODO Create PetScheduling
        return Scheduling.createNew()
                .customerId(customerId)
                .companyId(companyId)
                .build();
    }

    private Set<Pet> petsOfCustomer(CustomerId customerId) {
        var pets = this.pets.ofCustomer(customerId);

        if (pets.isEmpty())
            throw new CustomerDoesNotContainAnyPet();

        return pets;
    }

    private void verifyServiceSuportPetSize(Set<Pet> pets, Set<PetId> petIds, Service service) {
        var avaliableSizes = serviceDomain.servicesByServiceType(service.type()).stream()
                .map(Service::size).collect(Collectors.toSet());

        var unavaliableSize = pets.stream().filter(pet -> petIds.contains(pet.id()))
                .map(Pet::size)
                .allMatch(size -> avaliableSizes.contains(size.name()));

        if (unavaliableSize)
            throw new ServiceUnavaliableForThisPetSize();
    }

    private void verifyPetsBelongsToTheCustomer(Set<Pet> pets, Set<PetId> petIds) {
        if (pets.stream().noneMatch(pet -> petIds.contains(pet.id())))
            throw new PetDoesNotBelongToTheCustomer();
    }

    private void verifyCompanyExists(CompanyId companyId) {
        if (!companies.exists(companyId))
            throw new CompanyNotFoundException();
    }

    private void verifyCustomerExists(CustomerId customerId) {
        if(!customers.exists(customerId))
            throw new  CustomerNotFoundException();
    }

}
