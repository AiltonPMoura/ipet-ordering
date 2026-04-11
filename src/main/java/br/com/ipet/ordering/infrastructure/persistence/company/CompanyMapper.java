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

    private final DocumentFactory documentFactory;

    public Company toDomain(CompanyPersistenceEntity companyPersistenceEntity) {
        return Company.existing()
                .id(new CompanyId(companyPersistenceEntity.getId()))
                .name(new CompanyName(companyPersistenceEntity.getCompanyName()))
                .document(documentFactory.from(companyPersistenceEntity.getDocument()))
                .email(new Email(companyPersistenceEntity.getEmail()))
                .phone(new Phone(companyPersistenceEntity.getPhone()))
                .address(this.toAddress(companyPersistenceEntity.getAddress()))
                .registeredAt(companyPersistenceEntity.getRegisteredAt())
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
