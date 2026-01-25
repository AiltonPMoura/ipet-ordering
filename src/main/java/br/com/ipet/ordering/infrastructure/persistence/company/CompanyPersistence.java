package br.com.ipet.ordering.infrastructure.persistence.company;

import br.com.ipet.ordering.infrastructure.persistence.embedded.AddressEmbeddable;
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
public class CompanyPersistence {

    @Id
    private UUID id;

    private String name;
    private String cnpj;
    private Integer celPhone;
    private String email;
    private OffsetDateTime registeredAt;

    @Embedded
    private AddressEmbeddable address;

}
