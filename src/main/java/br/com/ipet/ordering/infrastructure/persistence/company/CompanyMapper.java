package br.com.ipet.ordering.infrastructure.persistence.company;

import br.com.ipet.ordering.domain.model.commons.Address;
import br.com.ipet.ordering.domain.model.commons.CelPhone;
import br.com.ipet.ordering.domain.model.commons.Cnpj;
import br.com.ipet.ordering.domain.model.commons.CompanyId;
import br.com.ipet.ordering.domain.model.commons.CompanyName;
import br.com.ipet.ordering.domain.model.commons.Email;
import br.com.ipet.ordering.domain.model.commons.ZipCode;
import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.infrastructure.persistence.embedded.AddressEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    public Company toDomain(CompanyPersistence companyPersistence) {
        return Company.existing()
                .id(new CompanyId(companyPersistence.getId()))
                .name(new CompanyName(companyPersistence.getName()))
                .cnpj(new Cnpj(companyPersistence.getCnpj()))
                .email(new Email(companyPersistence.getEmail()))
                .celPhone(new CelPhone(companyPersistence.getCelPhone()))
                .address(toAddress(companyPersistence.getAddress()))
                .registeredAt(companyPersistence.getRegisteredAt())
                .build();
    }

    private Address toAddress(AddressEmbeddable address) {
        return Address.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .complement(address.getComplement())
                .zipCode(new ZipCode(address.getZipCode()))
                .build();
    }

    /*private Set<Service> toServiceDomainEntity(Set<ServicePersistenceEntity> servicesPersistenceEntity) {
        return servicesPersistenceEntity.stream().map(servicePersistenceEntity -> Service.existing()
                        .id(new ServiceId(servicePersistenceEntity.getId()))
                        .companyId(new CompanyId(servicePersistenceEntity.getCompanyId()))
                        .name(new ServiceName(servicePersistenceEntity.getName()))
                        .description(new ServiceDescription(servicePersistenceEntity.getDescription()))
                        .price(new Money(servicePersistenceEntity.getPrice()))
                        .build())
                .collect(Collectors.toSet());
    }

    private Set<Product> toProductDomainEntity(Set<ProductPersistenceEntity> productsPersistenceEntity) {
        return productsPersistenceEntity.stream().map(productPersistenceEntity -> Product.existing()
                        .id(new ProductId(productPersistenceEntity.getId()))
                        .companyId(new CompanyId(productPersistenceEntity.getCompanyId()))
                        .name(new ProductName(productPersistenceEntity.getName()))
                        .description(new ProductDescription(productPersistenceEntity.getDescription()))
                        .price(new Money(productPersistenceEntity.getPrice()))
                        .build())
                .collect(Collectors.toSet());
    }*/

}
