package br.com.ipet.ordering.infrastructure.persistence.company;

import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CompanyPersistenceMapper {

    public CompanyPersistenceEntity fromDomain(Company company) {
        return merge(new CompanyPersistenceEntity(), company);
    }

    public CompanyPersistenceEntity merge(CompanyPersistenceEntity companyPersistence, Company company) {
        companyPersistence.setId(company.id().value());
        companyPersistence.setCompanyName(company.name().value());
        companyPersistence.setDocument(company.document().value());
        companyPersistence.setPhone(company.phone().value());
        companyPersistence.setEmail(company.email().value());
        companyPersistence.setRegisteredAt(company.registeredAt());
        companyPersistence.setAddress(this.toAdressEmbeddable(company.address()));
        return companyPersistence;
    }

    private AddressEmbeddable toAdressEmbeddable(Address address) {
        return AddressEmbeddable.builder()
                .street(address.street())
                .number(address.number())
                .neighborhood(address.neighborhood())
                .city(address.city())
                .state(address.state())
                .complement(address.complement())
                .zipCode(address.zipCode().value())
                .build();
    }

}
