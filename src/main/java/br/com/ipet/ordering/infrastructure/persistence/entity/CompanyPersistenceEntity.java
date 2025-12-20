package br.com.ipet.ordering.infrastructure.persistence.entity;

import br.com.ipet.ordering.infrastructure.persistence.embedded.AddressEmbeddable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "company")
public class CompanyPersistenceEntity {

    @Id
    private UUID id;

    private String companyName;
    private String cnpj;
    private Integer celPhone;
    private String email;
    private AddressEmbeddable address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<ServicePersistenceEntity> services;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<ProductPersistenceEntity> products;

}
