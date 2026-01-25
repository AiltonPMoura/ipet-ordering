package br.com.ipet.ordering.infrastructure.persistence.company;

import br.com.ipet.ordering.domain.model.commons.Address;
import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.infrastructure.persistence.embedded.AddressEmbeddable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CompanyPersistenceMapper {

    public CompanyPersistence toPersistence(Company company) {
        return CompanyPersistence.builder()
                .id(company.id().value())
                .name(company.name().value())
                .cnpj(company.cnpj().value())
                .email(company.email().value())
                .celPhone(company.celPhone().value())
                .address(fromAdressValueObject(company.address()))
                .build();
    }

    private AddressEmbeddable fromAdressValueObject(Address address) {
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
