package br.com.ipet.ordering.infrastructure.persistence.mapper;

import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.domain.model.entity.Product;
import br.com.ipet.ordering.domain.model.entity.Service;
import br.com.ipet.ordering.domain.model.commons.Address;
import br.com.ipet.ordering.domain.model.commons.CelPhone;
import br.com.ipet.ordering.domain.model.commons.Cnpj;
import br.com.ipet.ordering.domain.model.commons.CompanyId;
import br.com.ipet.ordering.domain.model.commons.CompanyName;
import br.com.ipet.ordering.domain.model.commons.Email;
import br.com.ipet.ordering.domain.model.commons.Money;
import br.com.ipet.ordering.domain.model.commons.ProductDescription;
import br.com.ipet.ordering.domain.model.commons.ProductId;
import br.com.ipet.ordering.domain.model.commons.ProductName;
import br.com.ipet.ordering.domain.model.commons.ServiceDescription;
import br.com.ipet.ordering.domain.model.commons.ServiceId;
import br.com.ipet.ordering.domain.model.commons.ServiceName;
import br.com.ipet.ordering.domain.model.commons.ZipCode;
import br.com.ipet.ordering.infrastructure.persistence.embedded.AddressEmbeddable;
import br.com.ipet.ordering.infrastructure.persistence.entity.CompanyPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.entity.ProductPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.entity.ServicePersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CompanyMapper {

    public Company toDomainEntity(CompanyPersistenceEntity companyPersistenceEntity) {
        return Company.existing()
                .id(new CompanyId(companyPersistenceEntity.getId()))
                .companyName(new CompanyName(companyPersistenceEntity.getName()))
                .cnpj(new Cnpj(companyPersistenceEntity.getCnpj()))
                .email(new Email(companyPersistenceEntity.getEmail()))
                .celPhone(new CelPhone(companyPersistenceEntity.getCelPhone()))
                .address(toAddressValueObject(companyPersistenceEntity.getAddress()))
                .build();
    }

    private Address toAddressValueObject(AddressEmbeddable address) {
        return Address.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .complement(address.getComplement())
                .zipCode(new ZipCode(address.getZipCode()))
                .build();
    }

    private Set<Service> toServiceDomainEntity(Set<ServicePersistenceEntity> servicesPersistenceEntity) {
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
    }

}
