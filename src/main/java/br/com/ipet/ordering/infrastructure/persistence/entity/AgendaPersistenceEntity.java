package br.com.ipet.ordering.infrastructure.persistence.entity;

import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.infrastructure.persistence.company.CompanyPersistenceEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.time.Period;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "agenda")
public class AgendaPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(optional = false)
    private CompanyPersistenceEntity company;

    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL)
    private Set<WorkingDayPersistenceEntity> workingDays;

    private String name;
    private String status;
    private OffsetDateTime activedAt;
    private OffsetDateTime standByAt;
    private OffsetDateTime blockedAt;
    private Period lockedDays;

    private OffsetDateTime createdAt;

    private OffsetDateTime modifiedAt;

}
