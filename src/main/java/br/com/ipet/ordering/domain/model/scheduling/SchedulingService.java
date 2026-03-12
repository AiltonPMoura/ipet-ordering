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
import br.com.ipet.ordering.domain.model.pet.Size;
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
                               String ServiceType) {
        verifyCustomerExists(customerId);
        verifyCompanyExists(companyId);

        var scheduling = Scheduling.createNew()
                .customerId(customerId)
                .companyId(companyId)
                .build();

        var petsOfCustomer = petsOfCustomer(customerId, petIds);
        var services = serviceDomain.findByServiceType(ServiceType);

        petsOfCustomer.forEach(pet -> {
            var service = serviceBySize(pet.size(), services);
            scheduling.addPet(pet.id(), service);
        });

        return scheduling;
    }

    private Service serviceBySize(Size size, Set<Service> services) {
        return services.stream()
                .filter(service -> size.equals(service.size()))
                .findFirst()
                .orElseThrow(() -> new ServiceUnavaliableForThisPetSize());
    }

    private Set<Pet> petsOfCustomer(CustomerId customerId, Set<PetId> petIds) {
        var petsOfCustomer = this.pets.ofCustomer(customerId);

        if (petsOfCustomer.isEmpty())
            throw new CustomerDoesNotContainAnyPet();

        verifyPetsBelongsToTheCustomer(petsOfCustomer, petIds);

        return petsOfCustomer.stream()
                .filter(pet -> petIds.contains(pet.id()))
                .collect(Collectors.toSet());
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
