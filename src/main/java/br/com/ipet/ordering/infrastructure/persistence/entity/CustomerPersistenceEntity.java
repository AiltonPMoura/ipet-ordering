package br.com.ipet.ordering.infrastructure.persistence.entity;

import br.com.ipet.ordering.infrastructure.persistence.embedded.AddressEmbeddable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
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

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "customer")
public class CustomerPersistenceEntity {

    @Id
    private UUID id;

    private String firstName;
    private String lastName;
    private String email;
    private Integer celPhone;
    private String document;

    @Embedded
    private AddressEmbeddable address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<PetPersistenceEntity> pets = new HashSet<>();

    private OffsetDateTime registerAt;

}
