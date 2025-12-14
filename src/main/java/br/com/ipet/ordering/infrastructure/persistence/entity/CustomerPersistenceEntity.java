package br.com.ipet.ordering.infrastructure.persistence.entity;

import br.com.ipet.ordering.domain.model.entity.Pet;
import br.com.ipet.ordering.domain.model.valueobject.Address;
import br.com.ipet.ordering.domain.model.valueobject.CelPhone;
import br.com.ipet.ordering.domain.model.valueobject.CustumerId;
import br.com.ipet.ordering.domain.model.valueobject.Document;
import br.com.ipet.ordering.domain.model.valueobject.Email;
import br.com.ipet.ordering.domain.model.valueobject.FullName;
import br.com.ipet.ordering.infrastructure.persistence.embedded.AddressEmbedded;
import br.com.ipet.ordering.infrastructure.persistence.embedded.FullNameEmbedded;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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

    private UUID id;

    private String email;
    private Integer celPhone;
    private String document;

    @Embedded
    private FullNameEmbedded fullName;

    @Embedded
    private AddressEmbedded address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<PetPersistenceEntity> pets = new HashSet<>();

    private OffsetDateTime registerAt;

}
