/*
package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.company.CompanyNotFoundException;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerDoesNotContainAnyPet;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.customer.CustomerNotFoundException;
import br.com.ipet.ordering.domain.model.customer.Customers;
import br.com.ipet.ordering.domain.model.pet.Pet;
import br.com.ipet.ordering.domain.model.pet.PetDoesNotBelongToTheCustomer;
import br.com.ipet.ordering.domain.model.pet.PetId;
import br.com.ipet.ordering.domain.model.pet.Pets;
import br.com.ipet.ordering.domain.model.pet.Size;
import br.com.ipet.ordering.domain.model.schedule.category.SubcategoryService;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class SchedulingStartService {

    private Customers customers;
    private Companies companies;
    private Pets pets;
    private SubcategoryService subcategoryService;

    public Booking start(CustomerId customerId, CompanyId companyId) {

        if(!customers.exists(customerId))
            throw new  CustomerNotFoundException("");

        if (!companies.exists(companyId))
            throw new CompanyNotFoundException("");

        var scheduling = Booking.create(customerId, companyId);

        var petsOfCustomer = petsOfCustomer(customerId, petIds);
        var services = subcategoryService.findByServiceType(ServiceType);

        petsOfCustomer.forEach(pet -> {
            var service = serviceBySize(pet.size(), services);
            scheduling.addPet(pet.id(), service);
        });

        return scheduling;
    }

    public

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

}
*/
