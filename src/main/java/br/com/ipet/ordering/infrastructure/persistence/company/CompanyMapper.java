package br.com.ipet.ordering.infrastructure.persistence.company;

import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Phone;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.company.CompanyName;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.ZipCode;
import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import br.com.ipet.ordering.domain.model.commons.document.DocumentFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompanyMapper {

    public Company toDomain(CompanyPersistenceEntity companyPersistence) {
        return Company.existing()
                .id(new CompanyId(companyPersistence.getId()))
                .name(new CompanyName(companyPersistence.getCompanyName()))
                .document(DocumentFactory.from(companyPersistence.getDocument()))
                .email(new Email(companyPersistence.getEmail()))
                .phone(new Phone(companyPersistence.getPhone()))
                .address(this.toAddress(companyPersistence.getAddress()))
                .registeredAt(companyPersistence.getRegisteredAt())
                .build();
    }

    private Address toAddress(AddressEmbeddable addressEmbeddable) {
        return Address.builder()
                .street(addressEmbeddable.getStreet())
                .number(addressEmbeddable.getNumber())
                .neighborhood(addressEmbeddable.getNeighborhood())
                .city(addressEmbeddable.getCity())
                .complement(addressEmbeddable.getComplement())
                .zipCode(new ZipCode(addressEmbeddable.getZipCode()))
                .build();
    }

}
