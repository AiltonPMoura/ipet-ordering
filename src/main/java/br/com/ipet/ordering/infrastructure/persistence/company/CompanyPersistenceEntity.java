package br.com.ipet.ordering.infrastructure.persistence.company;

import br.com.ipet.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;
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
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "company_name")
    private String companyName;

    @Column
    private String document;

    @Column
    private String phone;

    @Column
    private String email;

    @Column
    private OffsetDateTime registeredAt;

    @Embedded
    private AddressEmbeddable address;

}
