package br.com.ipet.ordering.infrastructure.persistence.company;

import br.com.ipet.ordering.domain.model.commons.Address;
import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.infrastructure.persistence.embedded.AddressEmbeddable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CompanyPersistenceMapper {

    public CompanyPersistence toPersistence(Company company) {
        return merge(new CompanyPersistence(), company);
    }

    public CompanyPersistence merge(CompanyPersistence companyPersistence, Company company) {
        companyPersistence.setId(company.id().value());
        companyPersistence.setName(company.name().value());
        companyPersistence.setCnpj(company.cnpj().value());
        companyPersistence.setEmail(company.email().value());
        companyPersistence.setCelPhone(company.celPhone().value());
        companyPersistence.setAddress(toAdressEmbeddable(company.address()));
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
