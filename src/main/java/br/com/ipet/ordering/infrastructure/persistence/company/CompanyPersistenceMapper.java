package br.com.ipet.ordering.infrastructure.persistence.company;

import br.com.ipet.ordering.domain.model.commons.Address;
import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CompanyPersistenceMapper {

    public CompanyPersistenceEntity toPersistence(Company company) {
        return merge(new CompanyPersistenceEntity(), company);
    }

    public CompanyPersistenceEntity merge(CompanyPersistenceEntity companyPersistenceEntity, Company company) {
        companyPersistenceEntity.setId(company.id().value());
        companyPersistenceEntity.setName(company.name().value());
        companyPersistenceEntity.setCnpj(company.cnpj().value());
        companyPersistenceEntity.setEmail(company.email().value());
        companyPersistenceEntity.setCelPhone(company.celPhone().value());
        companyPersistenceEntity.setAddress(toAdressEmbeddable(company.address()));
        return companyPersistenceEntity;
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
